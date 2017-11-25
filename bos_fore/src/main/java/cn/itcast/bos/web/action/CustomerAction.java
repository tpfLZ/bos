package cn.itcast.bos.web.action;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.utils.MailUtils;
import cn.itcast.crm.domain.Customer;

@Namespace("/")
@ParentPackage("struts-default")
@Controller("customerAction")
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {

    static {
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
    }

    private Customer customer = new Customer();
    // 注入redis对象
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> redisTemplate;
    // 注入jms对象
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Override
    public Customer getModel() {
        // TODO Auto-generated method stub
        return customer;
    }

    // 发送验证码
    @Action(value = "customer_sendSms")
    public String sendSms() throws Exception {
        // 调用字符串工具生成短信验证码
        final String randomCode = RandomStringUtils.randomNumeric(4);
        // 将短信验证码保存到session中
        ServletActionContext.getRequest().getSession().setAttribute(customer.getTelephone(), randomCode);
        System.out.println("生成的短信验证码为:" + randomCode);
        // 调用MQ服务，发送一条消息到中间件
        jmsTemplate.send("bos_sms", new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", customer.getTelephone());
                mapMessage.setString("randomCode", randomCode);
                return mapMessage;
            }
        });
        return NONE;
    }

    // 用户注册
    private String checkCode;

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Action(value = "customer_regist", results = {
            @Result(name = "success", location = "signup-success.html", type = "redirect"),
            @Result(name = "input", location = "signup.html", type = "redirect") })
    public String regist() {
        // 获取存储在session中的验证码是否一致
        String sessionCheckCode =
                (String) ServletActionContext.getRequest().getSession().getAttribute(customer.getTelephone());
        // 进行判断
        if (checkCode == null || !checkCode.equals(sessionCheckCode)) {
            return INPUT;
        }

        // 如果验证一致，调crm系统插入一条用户数据
        WebClient.create("http://localhost:9090/crm_management/services/customerService/insertNewCustomer")
                .type(MediaType.APPLICATION_JSON).post(customer);

        // 发送一封激活邮件
        // 生成激活码
        String activeCode = RandomStringUtils.randomNumeric(32);
        // 将生成的激活码保存到redis数据库中，设置24小时有效
        redisTemplate.opsForValue().set(customer.getTelephone(), activeCode, 24, TimeUnit.HOURS);
        // 调用MailUtils发送一封激活邮件
        String content = "尊敬的客户您好，请于24小时内绑定邮箱，请点击下面邮箱的地址进行绑定：<br/><a href='" + MailUtils.activeUrl + "?telephone="
                + customer.getTelephone() + "&activeCode=" + activeCode + "'>速运邮箱绑定地址</a>";
        MailUtils.sendMail("速运快递激活邮件", content, customer.getTelephone());
        return SUCCESS;
    }

    // 用户绑定邮箱验证
    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    @Action(value = "customer_activeMail")
    public void activeMail() throws Exception {
        // 让页面能够解析中文
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        // 判断激活码是否有效，如果激活码无效，提示用户
        String redisActiveCode = redisTemplate.opsForValue().get(customer.getTelephone());
        if (redisActiveCode == null || !activeCode.equals(redisActiveCode)) {
            ServletActionContext.getResponse().getWriter().println("您的激活码已经失效，请重新注册激活！");
        } else {
            // 如果激活码有效 ，判断是否在重复绑定，T_CUSTOMER 表 type 字段 为 1，绑定
            Customer c = WebClient
                    .create("http://localhost:9090/crm_management/services/customerService/isBindEmail/"
                            + customer.getTelephone())
                    .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(Customer.class);
            if (c.getType() == null || c.getType() != 1) {
                // 如果未绑定
                WebClient.create("http://localhost:9090/crm_management/services/customerService/bindEmail?telephone="
                        + customer.getTelephone()).type(MediaType.APPLICATION_JSON).put(null);
                ServletActionContext.getResponse().getWriter().println("邮箱绑定成功！");
            } else {
                ServletActionContext.getResponse().getWriter().println("邮箱已经绑定！");
            }
        }
        // 删除激活码
        redisTemplate.delete(customer.getTelephone());
    }

    // 用户登录
    @Action(value = "customer_login", results = {
            @Result(name = "success", type = "redirect", location = "index.html#/myhome"),
            @Result(name = "login", type = "redirect", location = "login.html") })
    public String login() {
        Customer loginCustomer = WebClient
                .create("http://localhost:9090/crm_management/services/customerService/customer/login?telephone="
                        + customer.getTelephone() + "&password=" + customer.getPassword())
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(Customer.class);
        if (loginCustomer == null) {
            return LOGIN;
        } else {
            // 登录成功
            ServletActionContext.getRequest().getSession().setAttribute("loginCustomer", loginCustomer);
            return SUCCESS;
        }

    }
}

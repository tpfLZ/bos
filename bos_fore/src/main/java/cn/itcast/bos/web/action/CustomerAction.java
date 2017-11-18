package cn.itcast.bos.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.utils.AlibabaSmsUtils;
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

    @Override
    public Customer getModel() {
        // TODO Auto-generated method stub
        return customer;
    }

    // 发送验证码
    @Action(value = "customer_sendSms")
    public String sendSms() throws Exception {
        // 调用字符串工具生成短信验证码
        String randomCode = RandomStringUtils.randomNumeric(4);
        // 将短信验证码保存到session中
        ServletActionContext.getRequest().getSession().setAttribute(customer.getTelephone(), randomCode);
        System.out.println("生成的短信验证码为:" + randomCode);
        // 发送短信
        boolean flage = AlibabaSmsUtils.sendSms(customer.getTelephone(), "小米", randomCode);
        // 判断短信是否成功发送
        if (flage) {
            // 发送成功
            return NONE;
        } else {
            // 发送失败
            throw new RuntimeException("发送短信出错！");
        }
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
        return SUCCESS;
    }
}

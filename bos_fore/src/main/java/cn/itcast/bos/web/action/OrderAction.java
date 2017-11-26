package cn.itcast.bos.web.action;

import javax.ws.rs.core.MediaType;

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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order> {

    private Order order = new Order();
    private String sendAreaInfo;
    private String recAreaInfo;

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    @Override
    public Order getModel() {
        return order;
    }

    @Action(value = "order_add", results = { @Result(name = "success", type = "redirect", location = "index.html") })
    public String add() {
        // 手动封装地址信息数据
        Area sendArea = new Area();
        String[] sendAreaData = sendAreaInfo.split("/");
        sendArea.setProvince(sendAreaData[0]);
        sendArea.setCity(sendAreaData[1]);
        sendArea.setDistrict(sendAreaData[2]);

        Area recArea = new Area();
        String[] recAreaData = recAreaInfo.split("/");
        recArea.setProvince(recAreaData[0]);
        recArea.setCity(recAreaData[1]);
        recArea.setDistrict(recAreaData[2]);

        order.setSendArea(sendArea);
        order.setRecArea(recArea);

        // 关联当前登录客户
        Customer loginCustomer =
                (Customer) ServletActionContext.getRequest().getSession().getAttribute("loginCustomer");
        order.setCustomer_id(loginCustomer.getId());

        // 调用webservice，将当前对象传递给bos_management,保存订单
        WebClient.create("http://localhost:8080/bos_management/services/orderService/order")
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(order);
        return SUCCESS;
    }
}

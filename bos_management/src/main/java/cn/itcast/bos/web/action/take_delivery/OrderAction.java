package cn.itcast.bos.web.action.take_delivery;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.IOrderService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller("orderAction")
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order> {

    private Order order = new Order();

    @Autowired
    @Qualifier("orderService")
    private IOrderService orderService;

    @Override
    public Order getModel() {
        return order;
    }

    @Action(value = "order_findByOrderNum")
    public void findByOrderNum() {
        Order orderData = orderService.findByOrderNum(order.getOrderNum());
        Map<String, Object> result = new HashMap<String, Object>();
        if (orderData == null) {
            result.put("success", false);
        } else {
            result.put("success", true);
            result.put("orderData", orderData);
        }

        String json = JSONObject.toJSONString(result);
        try {
            ServletActionContext.getResponse().setCharacterEncoding("utf-8");
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

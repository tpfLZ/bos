package cn.itheima.action;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itheima.domain.Order;
import cn.itheima.domain.PageBean;
import cn.itheima.service.IOrderService;

@Controller("orderAction")
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class OrderAction extends ActionSupport implements ModelDriven<PageBean> {

    private PageBean pageBean = new PageBean();

    @Autowired
    @Qualifier("orderService")
    private IOrderService orderService;

    @Override
    public PageBean getModel() {
        // TODO Auto-generated method stub
        return pageBean;
    }

    @Action(value = "findOrder")
    public void findOrder() {
        Integer customerId = Integer.parseInt(ServletActionContext.getRequest().getParameter("customerId"));
        int pageNum = pageBean.getPageNum();
        int currentCount = pageBean.getCurrentCount();
        PageBean<Order> ps = orderService.findAllOrdersByPage(customerId, pageNum, currentCount);
        // 过滤掉不必要的元素
        PropertyFilter propertyFilter = new PropertyFilter() {

            @Override
            public boolean apply(Object arg0, String fieldName, Object arg2) {
                if ("cusPhone".equalsIgnoreCase(fieldName)) {
                    return false;
                }
                if ("id".equalsIgnoreCase(fieldName)) {
                    return false;
                }
                if ("orders".equalsIgnoreCase(fieldName)) {
                    return false;
                }
                return true;
            }
        };
        String json = JSONObject.toJSONString(ps, propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(json);
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

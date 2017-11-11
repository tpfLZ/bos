package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.ICourierService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller("courierAction")
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

    private Courier courier = new Courier();
    @Autowired
    @Qualifier("courierService")
    private ICourierService courierService;
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public Courier getModel() {
        // TODO Auto-generated method stub
        return courier;
    }

    @Action(value = "courier_save", results = {
            @Result(name = "success", location = "./pages/base/courier.html", type = "redirect") })
    public String save() {
        courierService.save(courier);
        return SUCCESS;
    }

    // 快递员信息进行分页查询
    @Action(value = "courier_pageQuery")
    public void pageQuery() {
        // 查询所有的快递员信息
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Courier> pageData = courierService.findPageData(pageable);

        // 将得到的信息封装到一个map中
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());

        // 过滤掉不必要的属性
        PropertyFilter propertyFilter = new PropertyFilter() {

            @Override
            public boolean apply(Object object, String fieldName, Object value) {
                // TODO Auto-generated method stub
                if ("fixedAreas".equalsIgnoreCase(fieldName)) {
                    return false;
                }
                return true;
            }
        };

        // 将数据返回给浏览器
        String json = JSONObject.toJSONString(result, propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
        try {
            ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

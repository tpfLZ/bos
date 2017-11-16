package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.IFixedAreaService;
import cn.itcast.crm.domain.Customer;

@Namespace("/")
@ParentPackage("struts-default")
@Controller("fixedAreaAction")
@Scope("prototype")
public class FixedAreaAction extends ActionSupport implements ModelDriven<FixedArea> {

    private FixedArea fixedArea = new FixedArea();
    @Autowired
    @Qualifier("fixedAreaService")
    private IFixedAreaService fixedAreaService;

    @Override
    public FixedArea getModel() {
        // TODO Auto-generated method stub
        return fixedArea;
    }

    @Action(value = "fixedArea_save", results = {
            @Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect") })
    public String save() {
        fixedAreaService.save(fixedArea);
        return SUCCESS;
    }

    // 进行有条件分页查询
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "fixedArea_pageQuery")
    public void pageQuery() {
        // 创建pageAble对象，封装与分页有关的参数
        Pageable pageable = new PageRequest(page - 1, rows);
        // 创建specification对象，封装所有查询的条件
        Specification<FixedArea> specification = new Specification<FixedArea>() {

            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(fixedArea.getId())) {
                    Predicate p1 = cb.equal(root.get("id").as(String.class), fixedArea.getId());
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(fixedArea.getCompany())) {
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + fixedArea.getCompany() + "%");
                    list.add(p2);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层查询数据
        Page<FixedArea> pageData = fixedAreaService.pageQuery(specification, pageable);
        // 将查询结果用map封装指定的格式
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        // 过滤掉不要的属性
        PropertyFilter propertyFilter = new PropertyFilter() {

            @Override
            public boolean apply(Object object, String fieldName, Object value) {
                if ("subareas".equalsIgnoreCase(fieldName)) {
                    return false;
                }
                if ("couriers".equalsIgnoreCase(fieldName)) {
                    return false;
                }
                return true;
            }
        };
        String json = JSONObject.toJSONString(result, propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
        try {
            // 写回数据
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 未关联客户展示
    @Action(value = "fixedArea_findNoAssociationCustomers")
    public void findNoAssociationCustomers() {
        // 使用webClient调用webService接口
        Collection<? extends Customer> collection =
                WebClient.create("http://localhost:9090/crm_management/services/customerService/noAssociationCustomers")
                        .accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        String jsonString = JSONObject.toJSONString(collection);
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
        try {
            ServletActionContext.getResponse().getWriter().write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 已关联客户展示
    @Action(value = "fixedArea_findHasAssociationFixedAreaCustomers")
    public void findHasAssociationFixedAreaCustomers() {
        Collection<? extends Customer> collection = WebClient.create(
                "http://localhost:9090/crm_management/services/customerService/hasAssociationFixedAreaCustomers/"
                        + fixedArea.getId())
                .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        String json = JSONObject.toJSONString(collection);
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将以关联客户的id和当前选中的定区的id发到服务器
    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    @Action(value = "fixedArea_associationCustomersToFixedArea", results = {
            @Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect") })
    public String associationCustomersToFixedArea() {
        String customerIdStr = StringUtils.join(customerIds, ",");
        WebClient.create(
                "http://localhost:9090/crm_management/services/customerService/associationCustomersToFixedArea?customerIdStr="
                        + customerIdStr + "&fixedAreaId=" + fixedArea.getId())
                .put(null);
        return SUCCESS;
    }

    // 将快递员与时间和定区进行关联
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    @Action(value = "fixedArea_associationCourierToFixedArea", results = {
            @Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect") })
    public String associationCourierToFixedArea() {
        fixedAreaService.associationCourierToFixedArea(fixedArea.getId(), courierId, takeTimeId);
        return SUCCESS;
    }
}

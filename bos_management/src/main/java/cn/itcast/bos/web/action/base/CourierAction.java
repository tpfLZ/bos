package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
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
        // 根据查询条件，构造specification条件查询对象
        Specification<Courier> specification = new Specification<Courier>() {

            @Override
            // root 当前查询的根对象即Courier
            // CriteriaQuery构造简单查询条件返回,提供where方法
            // CriteriaBuilder构造Predicate对象，条件对象，实现负载查询条件结果
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                // 创建一个list集合，将需要查询的条件保存到其中
                List<Predicate> list = new ArrayList<Predicate>();
                // 判断查询表单发送过来的数据中是否有员工的编号
                // cb.equal(对应的实体类的字段名称，根据模型驱动从浏览器获得的数据)
                if (StringUtils.isNotBlank(courier.getCourierNum())) {
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
                    list.add(p1);
                }
                // 判断查询表单发送过来的数据是否包含所属单位进行模糊查询
                if (StringUtils.isNotBlank(courier.getCompany())) {
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
                    list.add(p2);
                }
                // 判断查询表单发送过来的数据是否包含快递员工类型
                if (StringUtils.isNotBlank(courier.getType())) {
                    Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
                    list.add(p3);
                }
                // 多表联合查询
                // 使用Courier(Root)，关联standard
                Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);
                // 首先判断Courier类中是否有standard这个类的属性，然后判断这个standard这个类的属性的名字是否不为空
                if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
                    Predicate p4 = cb.like(standardRoot.get("name").as(String.class),
                            "%" + courier.getStandard().getName() + "%");
                    list.add(p4);
                }
                // cb.and(需要一个Predicate类型的可变参数)
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 查询所有的快递员信息
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Courier> pageData = courierService.findPageData(pageable, specification);

        // 将得到的信息封装到一个map中
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());

        // 过滤掉不必要的属性
        PropertyFilter propertyFilter = new PropertyFilter() {

            @Override
            public boolean apply(Object object, String fieldName, Object value) {
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

    // 对快递员的信息进行批量更新
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Action(value = "/courier_updatDelTag", results = {
            @Result(name = "success", location = "./pages/base/courier.html", type = "redirect") })
    public String updatDelTag() {
        String[] idArray = ids.split(",");
        courierService.updateDelTag(idArray);
        return SUCCESS;
    }
}

package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.IStandardService;

@Controller("standardAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Actions
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    private Standard standard = new Standard();
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Autowired
    @Qualifier("standardService")
    private IStandardService standardService;

    @Override
    public Standard getModel() {
        // TODO Auto-generated method stub
        return standard;
    }

    @Action(value = "standard_save", results = {
            @Result(name = "success", location = "./pages/base/standard.html", type = "redirect") })
    public String save() {
        standardService.save(standard);
        return SUCCESS;
    }

    // 收派标准分页查询
    @Action(value = "standard_pageQuery")
    public void pageQuery() {
        // 调用业务层，查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Standard> pageData = standardService.findPageData(pageable);

        // 将得到的结果封装到一个hashmap中
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());

        // 使用fastjson将数据以json的格式返回给浏览器
        String json = JSONObject.toJSONString(result);
        try {
            ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 给快递员设置中返回取派标准设置
    @Action(value = "standard_findAll")
    public void findAll() {
        List<Standard> allData = standardService.findAll();
        String json = JSONObject.toJSONString(allData);
        try {
            ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package cn.itcast.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.IPermissionService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class PermissionAction extends ActionSupport implements ModelDriven<Permission> {

    private Permission permission = new Permission();

    @Autowired
    @Qualifier("permissionService")
    private IPermissionService permissionService;

    @Override
    public Permission getModel() {
        return permission;
    }

    @Action(value = "permission_list")
    public void list() {
        List<Permission> list = permissionService.findAll();
        String json = JSONObject.toJSONString(list);
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Action(value = "permission_save", results = {
            @Result(name = "success", type = "redirect", location = "pages/system/permission.html") })
    public String save() {
        permissionService.save(permission);
        return SUCCESS;
    }
}

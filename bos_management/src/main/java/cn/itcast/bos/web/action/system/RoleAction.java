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

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.IRoleService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class RoleAction extends ActionSupport implements ModelDriven<Role> {

    private Role role = new Role();

    @Autowired
    @Qualifier("roleService")
    private IRoleService roleService;

    @Override
    public Role getModel() {
        return role;
    }

    @Action(value = "role_list")
    public void list() {
        List<Role> list = roleService.findAll();
        String json = JSONObject.toJSONString(list);
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] permissionIds;
    private String menuIds;

    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    @Action(value = "role_save", results = {
            @Result(name = "success", type = "redirect", location = "pages/system/role.html") })
    public String save() {
        roleService.save(role, permissionIds, menuIds);
        return SUCCESS;
    }
}

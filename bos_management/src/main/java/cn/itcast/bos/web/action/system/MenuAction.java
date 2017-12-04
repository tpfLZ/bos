package cn.itcast.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.IMenuService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller()
@Scope("prototype")
public class MenuAction extends ActionSupport implements ModelDriven<Menu> {

    private Menu menu = new Menu();

    @Autowired
    @Qualifier("menuService")
    private IMenuService menuService;

    @Override
    public Menu getModel() {
        return menu;
    }

    @Action(value = "menu_list")
    public void list() {
        List<Menu> menus = menuService.findAll();
        String json = JSONObject.toJSONString(menus);
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Action(value = "menu_save", results = {
            @Result(name = "success", type = "redirect", location = "pages/system/menu.html") })
    public String save() {
        menuService.save(menu);
        return SUCCESS;
    }

    @Action(value = "menu_showMenu")
    public void showMenu() {
        // 调用业务层，查询具体的菜单列表
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        List<Menu> menus = menuService.findByUser(user);
        String json = JSONObject.toJSONString(menus);
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

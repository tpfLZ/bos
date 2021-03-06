package cn.itcast.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.IUserService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class UserAction extends ActionSupport implements ModelDriven<User> {

    private User user = new User();
    @Autowired
    private IUserService userService;

    @Override
    public User getModel() {
        return user;
    }

    @Action(value = "user_login", results = { @Result(name = "login", type = "redirect", location = "login.html"),
            @Result(name = "success", type = "redirect", location = "index.html") })
    public String login() {
        System.out.println(user.getPassword() + "   111");
        // 基于shiro实现登录
        Subject subject = SecurityUtils.getSubject();
        // 用户名和密码信息
        AuthenticationToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
            // 登录成功，将用户信息保存到session
            System.out.println(0);
            return SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println(1);
            return LOGIN;
        }
    }

    // 退出登录
    @Action(value = "user_logout", results = { @Result(name = "success", type = "redirect", location = "login.html") })
    public String logout() {
        // 基于shiro完成退出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }

    // 查询所有的用户
    @Action(value = "user_list")
    public void findAll() {
        List<User> users = userService.findAll();
        String json = JSONObject.toJSONString(users);
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

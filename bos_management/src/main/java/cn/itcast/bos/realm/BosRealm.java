package cn.itcast.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.IPermissionService;
import cn.itcast.bos.service.system.IRoleService;
import cn.itcast.bos.service.system.IUserService;

//@Service("bosRealm")
public class BosRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IRoleService roleServce;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        System.out.println("shiro授权管理！");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 根据当前用户查询对应的角色和权限
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        // 调用业务层，查询对应角色
        List<Role> roles = roleServce.findByUser(user.getId());
        for (Role role : roles) {
            simpleAuthorizationInfo.addRole(role.getKeyword());
        }
        // 调用业务层，查询权限
        List<Permission> permissions = permissionService.findByUser(user.getId());
        for (Permission permission : permissions) {
            simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("shiro认证管理");
        // 转换token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        // 根据用户名查询用户信息
        User user = userService.findByUsername(usernamePasswordToken.getUsername());
        System.out.println(usernamePasswordToken.getPassword());
        System.out.println(user.getPassword());
        if (user == null) {
            return null;
        } else {
            // 用户名存在
            // 当返回用户名和密码时，securityManager管理器，自动比较返回的密码和输入的用户密码是否一致
            // 如果密码一直登录成功，如果密码不一致，爆出密码错误异常
            return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        }
    }

}

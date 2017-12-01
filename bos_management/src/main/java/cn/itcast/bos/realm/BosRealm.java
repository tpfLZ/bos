package cn.itcast.bos.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.IUserService;

@Service("bosRealm")
public class BosRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        return null;
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

package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.IRoleRepository;
import cn.itcast.bos.dao.system.IUserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.IUserService;

@Service("userService")
@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user, String[] roleIds) {
        // 保存用户
        userRepository.save(user);
        // 授予角色
        for (String roleId : roleIds) {
            Role role = roleRepository.findOne(Integer.parseInt(roleId));
            user.getRoles().add(role);
        }
    }

}

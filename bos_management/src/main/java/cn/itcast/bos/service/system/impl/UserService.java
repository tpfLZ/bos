package cn.itcast.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.IUserRepository;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.IUserService;

@Service("userService")
@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}

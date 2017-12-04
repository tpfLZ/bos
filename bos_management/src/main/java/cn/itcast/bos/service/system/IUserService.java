package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.User;

public interface IUserService {

    public abstract User findByUsername(String username);

    public abstract List<User> findAll();

    public abstract void save(User user, String[] roleIds);
}

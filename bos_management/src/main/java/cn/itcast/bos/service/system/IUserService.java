package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.User;

public interface IUserService {

    public abstract User findByUsername(String username);
}

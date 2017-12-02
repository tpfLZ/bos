package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Role;

public interface IRoleService {

    public abstract List<Role> findByUser(Integer id);

    public abstract List<Role> findAll();
}

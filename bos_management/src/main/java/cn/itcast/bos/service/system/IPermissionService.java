package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Permission;

public interface IPermissionService {

    public abstract List<Permission> findByUser(Integer id);

    public abstract List<Permission> findAll();

    public abstract void save(Permission permission);
}

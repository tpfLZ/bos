package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.IPermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.IPermissionService;

@Service("permissionService")
@Transactional
public class PermissionService implements IPermissionService {

    @Autowired
    private IPermissionRepository permissionRepository;

    @Override
    public List<Permission> findByUser(Integer id) {
        return permissionRepository.findByUser(id);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public void save(Permission permission) {
        permissionRepository.save(permission);
    }

}

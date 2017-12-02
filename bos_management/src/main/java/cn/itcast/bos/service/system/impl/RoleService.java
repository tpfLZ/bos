package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.IRoleRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.IRoleService;

@Service("roleService")
@Transactional
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository rolePository;

    @Override
    public List<Role> findByUser(Integer id) {
        return rolePository.findByUser(id);
    }

    @Override
    public List<Role> findAll() {
        return rolePository.findAll();
    }

}

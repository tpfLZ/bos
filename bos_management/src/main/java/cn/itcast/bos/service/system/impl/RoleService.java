package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.IMenuRepository;
import cn.itcast.bos.dao.system.IPermissionRepository;
import cn.itcast.bos.dao.system.IRoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.IRoleService;

@Service("roleService")
@Transactional
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository rolePository;
    @Autowired
    private IPermissionRepository permissionRepository;
    @Autowired
    private IMenuRepository menuRepository;

    @Override
    public List<Role> findByUser(Integer id) {
        return rolePository.findByUser(id);
    }

    @Override
    public List<Role> findAll() {
        return rolePository.findAll();
    }

    @Override
    public void save(Role role, String[] permissionIds, String menuIds) {
        // 保存角色信息
        rolePository.save(role);
        // 关联权限
        if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
                role.getPermissions().add(permission);
            }
        }
        // 关联权限
        if (StringUtils.isNotBlank(menuIds)) {
            String[] menuIdArray = menuIds.split(",");
            for (String menuId : menuIdArray) {
                Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
                role.getMenus().add(menu);
            }
        }
    }

}

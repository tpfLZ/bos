package cn.itcast.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.Permission;

public interface IPermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("from Permission p inner join fetch p.roles r inner join fetch r.users u where u.id = ?")
    public abstract List<Permission> findByUser(Integer id);
}

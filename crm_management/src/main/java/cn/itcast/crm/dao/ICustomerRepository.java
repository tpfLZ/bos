package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {

    // 查询当前未关联的客户，即fixedAreaId为空
    public List<Customer> findByFixedAreaIdIsNull();

    // 查询id当前已经关联的客户，即FixedAreaId中有值
    public List<Customer> findByFixedAreaId(String fixedAreaId);

    // 根据客户的id值更新fixedAreaId的值
    @Query("update Customer set fixedAreaId = ? where id = ?")
    @Modifying
    public void updateFixedAreaId(String fixedAreaId, Integer id);
}

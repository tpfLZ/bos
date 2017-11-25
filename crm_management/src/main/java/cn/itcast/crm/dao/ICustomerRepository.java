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

    // 将表中的所有定区id都置空
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    public void clearFixedAreaId(String fixedAreaId);

    // 根据用户手机号查询用户是否已经绑定邮箱
    public Customer findByTelephone(String telephone);

    // 根据用户手机号来绑定当前邮箱
    @Query("update Customer set type = 1 where telephone = ?")
    @Modifying
    public void bindEmail(String telephone);

    // 根据手机号和密码判断是否有该用户
    public Customer findByTelephoneAndPassword(String telephone, String password);
}

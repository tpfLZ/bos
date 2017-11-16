package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

//实现有条件查询和根据id查询接口
public interface ICourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {

    // 根据id修改数据的是否作废状态
    @Query(value = "update Courier set deltag = '1' where id = ?")
    @Modifying
    public abstract void updateDelTag(Integer id);

}

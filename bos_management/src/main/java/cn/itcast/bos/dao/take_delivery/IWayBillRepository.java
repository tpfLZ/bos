package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface IWayBillRepository extends JpaRepository<WayBill, Integer> {

    public abstract WayBill findByWayBillNum(String wayBillNum);
}

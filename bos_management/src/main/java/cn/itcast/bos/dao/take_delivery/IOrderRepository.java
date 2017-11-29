package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.Order;

public interface IOrderRepository extends JpaRepository<Order, Integer> {

    public abstract Order findByOrderNum(String orderNum);
}

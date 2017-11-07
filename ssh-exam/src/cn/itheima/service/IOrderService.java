package cn.itheima.service;

import java.util.List;

import cn.itheima.domain.Order;
import cn.itheima.domain.PageBean;

public interface IOrderService {

    public abstract List<Order> findOrdersByCustomer(Integer customerId);

    public abstract PageBean<Order> findAllOrdersByPage(Integer customerId, int pageNum, int currentCount);

}

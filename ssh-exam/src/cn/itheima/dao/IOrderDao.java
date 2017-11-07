package cn.itheima.dao;

import java.util.List;

import cn.itheima.domain.Customer;
import cn.itheima.domain.Order;

public interface IOrderDao {

    public abstract List<Order> findOrdersByCustomer(Customer customer);

    public abstract List<Order> findAllOrdersByPage(Customer customer, int pageNum, int currentCount);

    public abstract int findTotalCount();
}

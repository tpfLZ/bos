package cn.itheima.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.dao.ICustomerDao;
import cn.itheima.dao.IOrderDao;
import cn.itheima.domain.Customer;
import cn.itheima.domain.Order;
import cn.itheima.domain.PageBean;
import cn.itheima.service.IOrderService;

@Service("orderService")
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    @Qualifier("orderDao")
    private IOrderDao orderDao;

    @Autowired
    @Qualifier("customerDao")
    private ICustomerDao customerDao;

    @Override
    public List<Order> findOrdersByCustomer(Integer customerId) {
        Customer customer = customerDao.findCustomerById(customerId);
        return orderDao.findOrdersByCustomer(customer);
    }

    @Override
    public PageBean<Order> findAllOrdersByPage(Integer customerId, int pageNum, int currentCount) {
        PageBean<Order> pageBean = new PageBean<Order>();
        // 设置当前页
        pageBean.setPageNum(pageNum);
        // 设置每页的数量
        pageBean.setCurrentCount(currentCount);
        // 设置总的数据条数
        int totalCount = orderDao.findTotalCount();
        pageBean.setTotalCount(totalCount);
        // 设置总的页数
        int totalPage = (int) Math.ceil(totalCount * 1.0 / currentCount);
        pageBean.setTotalPage(totalPage);
        // 封装当前页的所有数据
        Customer customer = customerDao.findCustomerById(customerId);
        List<Order> orders = orderDao.findAllOrdersByPage(customer, pageNum, currentCount);
        pageBean.setCurrentContent(orders);
        return pageBean;
    }

}

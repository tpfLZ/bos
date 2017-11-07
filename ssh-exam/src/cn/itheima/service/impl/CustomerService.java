package cn.itheima.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.dao.ICustomerDao;
import cn.itheima.domain.Customer;
import cn.itheima.service.ICustomerService;

@Service("customerService")
@Transactional
public class CustomerService implements ICustomerService {

    @Autowired
    @Qualifier("customerDao")
    private ICustomerDao customerDao;

    @Override
    public List<Customer> findAllCustomer() {
        return customerDao.findAllCustomer();
    }

    @Override
    public void addCustomer(Customer customer) {
        customerDao.addCustomer(customer);
    }

    @Override
    public void delCustomer(Customer customer) {
        customer = customerDao.findCustomerById(customer.getId());
        customerDao.delCustomer(customer);
    }

    @Override
    public Customer findCustomerById(Integer id) {
        return customerDao.findCustomerById(id);
    }

}

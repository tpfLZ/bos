package cn.itheima.service;

import java.util.List;

import cn.itheima.domain.Customer;

public interface ICustomerService {

    public abstract List<Customer> findAllCustomer();

    public abstract void addCustomer(Customer customer);

    public abstract void delCustomer(Customer customer);

    public abstract Customer findCustomerById(Integer id);

}

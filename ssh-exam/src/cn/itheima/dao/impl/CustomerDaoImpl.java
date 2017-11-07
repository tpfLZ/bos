package cn.itheima.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.itheima.dao.ICustomerDao;
import cn.itheima.domain.Customer;

@Repository("customerDao")
public class CustomerDaoImpl extends HibernateDaoSupport implements ICustomerDao {

    @Autowired
    @Qualifier("sessionFactory")
    public void setMySessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public List<Customer> findAllCustomer() {
        return (List<Customer>) this.getHibernateTemplate().find("from Customer");
    }

    @Override
    public void addCustomer(Customer customer) {
        this.getHibernateTemplate().save(customer);
    }

    @Override
    public void delCustomer(Customer customer) {
        this.getHibernateTemplate().delete(customer);
    }

    @Override
    public Customer findCustomerById(Integer id) {
        return (Customer) this.getHibernateTemplate().get(Customer.class, id);
    }

}

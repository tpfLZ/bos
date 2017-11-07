package cn.itheima.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.itheima.dao.IOrderDao;
import cn.itheima.domain.Customer;
import cn.itheima.domain.Order;

@Repository("orderDao")
public class OrderDaoImpl extends HibernateDaoSupport implements IOrderDao {

    @Autowired
    @Qualifier("sessionFactory")
    public void setMySessionFactory(SessionFactory sessionFacyory) {
        super.setSessionFactory(sessionFacyory);
    }

    @Override
    public List<Order> findOrdersByCustomer(Customer customer) {
        return (List<Order>) this.getHibernateTemplate().find("from Order o where o.customer = ? ", customer);
    }

    @Override
    public List<Order> findAllOrdersByPage(Customer customer, int pageNum, int currentCount) {
        DetachedCriteria dc = DetachedCriteria.forClass(Order.class);
        dc.add(Restrictions.eq("customer", customer));
        return (List<Order>) this.getHibernateTemplate().findByCriteria(dc, (pageNum - 1) * currentCount, currentCount);
    }

    @Override
    public int findTotalCount() {
        return ((Long) this.getHibernateTemplate().find("select count(*) from Order").iterator().next()).intValue();
    }

}

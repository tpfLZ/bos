package cn.itheima.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_order", catalog = "springtest")
public class Order {

    @Id
    @GenericGenerator(name = "myuuid", strategy = "uuid")
    @GeneratedValue(generator = "myuuid")
    private String orderNum;
    private String receiveInfo;
    @Column(precision = 23, scale = 2) // 整体精度是23位，保留小数点后两位
    private BigDecimal price;
    @ManyToOne(targetEntity = Customer.class)
    @Cascade(CascadeType.DELETE)
    @JoinColumn(name = "customer_id") // 由于在customer中配置了由order维护，所以由order生成外键名称
    private Customer customer;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getReceiveInfo() {
        return receiveInfo;
    }

    public void setReceiveInfo(String receiveInfo) {
        this.receiveInfo = receiveInfo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order [orderNum=" + orderNum + ", receiveInfo=" + receiveInfo + ", price=" + price + ", customer="
                + customer + "]";
    }

}

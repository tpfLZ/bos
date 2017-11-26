package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.IAreaRepository;
import cn.itcast.bos.dao.base.IFixedAreaRepository;
import cn.itcast.bos.dao.take_delivery.IOrderRepository;
import cn.itcast.bos.dao.take_delivery.IWorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.IOrderService;

@Service("orderService")
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    private IFixedAreaRepository fixedAreaRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IAreaRepository areaRepository;
    @Autowired
    private IWorkBillRepository workBillRepository;
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Override
    public void saveOrder(Order order) {
        order.setOrderNum(UUID.randomUUID().toString()); // 设置订单号
        order.setOrderTime(new Date());// 设置下单时间
        order.setStatus("1"); // 待取件

        // 寄件人 省市区
        Area area = order.getSendArea();
        Area persistArea =
                areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(), area.getCity(), area.getDistrict());
        // 收件人 省市区
        Area recArea = order.getSendArea();
        Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(),
                recArea.getDistrict());
        order.setSendArea(persistArea);
        order.setRecArea(persistRecArea);

        // 自动分单逻辑，基于crm地址库完全匹配，获取定区，分配快递员
        String fixedAreaId = WebClient.create(
                "http://localhost:9090/crm_management/services/customerService/customer/findFixedAreaIdByAddress?address="
                        + order.getSendAddress())
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(String.class);
        if (fixedAreaId != null) {
            // 根据定区查找到当前关联的快递员
            FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
            Courier courier = fixedArea.getCouriers().iterator().next();
            if (courier != null) {
                // 表示根据用户下单的地址找到了当前地区匹配的快递员
                System.out.println("自动分单成功1！");
                saveOrder(order, courier);
                return;
            }
        }

        // 自动分单逻辑，通过省市区，查询分区关键字，匹配地址，基于定区实现自动分单
        for (SubArea subArea : persistArea.getSubareas()) {
            // 当前客户的下单地址是否包含分区关键字
            if (order.getSendAddress().contains(subArea.getKeyWords())) {
                // 找到分区，找到定区，找到快递员
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        System.out.println("自动分单成功2！");
                        saveOrder(order, courier);
                        return;
                    }
                }
            }
        }

        for (SubArea subArea : persistArea.getSubareas()) {
            // 当前客户的下单地址是否包含分区辅助关键字
            if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
                // 找到分区，找到定区，找到快递员
                Courier courier = subArea.getFixedArea().getCouriers().iterator().next();
                if (courier != null) {
                    System.out.println("自动分单成功！3");
                    saveOrder(order, courier);
                    return;
                }
            }
        }

        // 进入人工分单
        order.setOrderType("2");
        orderRepository.save(order);
    }

    // 生成工单，发送短信
    private void generateWorkBill(final Order order) {
        // 生成工单
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        final String smsNumber = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(smsNumber);
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        workBillRepository.save(workBill);
        // 基于mq消息队列发送短信
        jmsTemplate.send("bos_sms", new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", order.getCourier().getTelephone());
                mapMessage.setString("msg", "短信序号：" + smsNumber + ",取件地址：" + order.getSendAddress() + ",联系人:"
                        + order.getSendName() + ",手机:" + order.getSendMobile() + "，快递员捎话：" + order.getSendMobileMsg());
                return mapMessage;
            }
        });
        // 修改工单状态
        workBill.setPickstate("已通知");
    }

    // 自动分单保存
    private void saveOrder(Order order, Courier courier) {
        // 将快递员关联订单上
        order.setCourier(courier);
        // 设置自动分单
        order.setOrderType("1");
        // 保存订单
        orderRepository.save(order);
        // 生成工单，发短信
        generateWorkBill(order);
    }
}

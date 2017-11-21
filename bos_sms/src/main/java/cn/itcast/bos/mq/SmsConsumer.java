package cn.itcast.bos.mq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import cn.itcast.bos.mq.utils.AlibabaSmsUtils;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            // 从消息队列中拿到传过来的值
            MapMessage mapMessage = (MapMessage) message;
            String phoneNum = mapMessage.getString("telephone");
            String code = mapMessage.getString("randomCode");
            // 调用阿里大鱼的第三方接口发送短信
            boolean flag = AlibabaSmsUtils.sendSms(phoneNum, phoneNum, code);
            if (flag) {
                // 可以记录日志等操作
                System.out.println("发送短信成功！");
            } else {
                throw new RuntimeException("发送短信异常！");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

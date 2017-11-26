package cn.itcast.bos.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import cn.itcast.bos.domain.take_delivery.Order;

public interface IOrderService {

    @Path("/order")
    @POST
    @Consumes({ "application/xml", "application/json" })
    public abstract void saveOrder(Order order);
}

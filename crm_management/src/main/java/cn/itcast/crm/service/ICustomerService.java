package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

public interface ICustomerService {

    // 查询所有未关联的客户列表
    @Path("/noAssociationCustomers")
    @GET
    @Produces({ "application/xml", "application/json" })
    public List<Customer> findNoAssociationCustomers();

    // 已经关联到指定定区的客户列表
    @Path("/hasAssociationFixedAreaCustomers/{fixedAreaId}")
    @GET
    @Produces({ "application/xml", "application/json" })
    public List<Customer> findHasAssociationFixedAreaCustomers(@PathParam("fixedAreaId") String fixedAreaId);

    // 将客户关联到定区上，将所有客户id拼成字符串1，2，3
    @Path("/associationCustomersToFixedArea")
    @PUT
    public void associationCustomersToFixedArea(@QueryParam("customerIdStr") String customerIdStr,
            @QueryParam("fixedAreaId") String fixedAreaId);

    // 插入一条新的客户数据(跨系统传递一个对象时使用consumes注解)
    @Path("/insertNewCustomer")
    @POST
    @Consumes({ "application/xml", "application/json" })
    public void regist(Customer customer);
}

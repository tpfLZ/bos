package cn.itcast.bos.service.take_delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface IPromotionService {

    // 保存新添加的宣传任务
    public abstract void save(Promotion promotion);

    // 宣传任务分页查询
    public abstract Page<Promotion> findPageData(Pageable pageable);

    // 根据page和rows返回分页数据
    @Path("/pageQuery")
    @GET
    @Produces({ "application/xml", "application/json" })
    public abstract PageBean<Promotion> findPageData(@QueryParam("page") int page, @QueryParam("rows") int rows);

    // 根据id查询宣传任务数据
    @Path("/promotion/{id}")
    @GET
    @Produces({ "application/xml", "application/json" })
    public abstract Promotion findById(@PathParam("id") Integer id);

    // 更新过期活动状态
    public abstract void updateStatus(Date date);
}

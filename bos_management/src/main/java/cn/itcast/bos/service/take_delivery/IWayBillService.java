package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface IWayBillService {

    // 保存简单的运单
    public abstract void save(WayBill wayBill);

    // 快速运单分页查询
    public abstract Page<WayBill> findPageData(WayBill wayBill, Pageable pageable);

    public abstract WayBill findWayBill(String wayBillNum);
}

package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.Promotion;

public interface IPromotionService {

    // 保存新添加的宣传任务
    public abstract void save(Promotion promotion);

    // 宣传任务分页查询
    public abstract Page<Promotion> findPageData(Pageable pageable);
}

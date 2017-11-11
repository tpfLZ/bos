package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Courier;

public interface ICourierService {

    public abstract void save(Courier courier);

    // 快递员信息分页查询
    public abstract Page<Courier> findPageData(Pageable pageable);
}

package cn.itcast.bos.service.base;

import java.util.List;

import cn.itcast.bos.domain.base.TakeTime;

public interface ITakeTimeService {

    // 查询所有的收派时间表
    public abstract List<TakeTime> findAll();
}

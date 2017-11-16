package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.ITakeTimeRepository;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.ITakeTimeService;

@Service("takeTimeService")
@Transactional
public class TakeTimeService implements ITakeTimeService {

    @Autowired
    private ITakeTimeRepository takeTimeRepository;

    @Override
    public List<TakeTime> findAll() {
        return takeTimeRepository.findAll();
    }

}

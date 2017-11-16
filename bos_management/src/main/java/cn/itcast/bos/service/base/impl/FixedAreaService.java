package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.ICourierRepository;
import cn.itcast.bos.dao.base.IFixedAreaRepository;
import cn.itcast.bos.dao.base.ITakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.IFixedAreaService;

@Service("fixedAreaService")
@Transactional
public class FixedAreaService implements IFixedAreaService {

    @Autowired
    private IFixedAreaRepository fixedAreaRepository;
    @Autowired
    private ICourierRepository courierRepository;
    @Autowired
    private ITakeTimeRepository takeTimeRepository;

    @Override
    public void save(FixedArea fixedArea) {
        fixedAreaRepository.save(fixedArea);
    }

    @Override
    public Page<FixedArea> pageQuery(Specification<FixedArea> specification, Pageable pageable) {
        return fixedAreaRepository.findAll(specification, pageable);
    }

    @Override
    public void associationCourierToFixedArea(String fixedAreaId, Integer courierId, Integer takeTimeId) {
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
        // 将快递员关联到定区上
        fixedArea.getCouriers().add(courier);
        // 将收派时间关联到快递员上
        courier.setTakeTime(takeTime);
    }

}

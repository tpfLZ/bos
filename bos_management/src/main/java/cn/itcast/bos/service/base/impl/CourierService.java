package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.ICourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.ICourierService;

@Service("courierService")
@Transactional
public class CourierService implements ICourierService {

    @Autowired
    private ICourierRepository courierRepository;

    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

    @Override
    public Page<Courier> findPageData(Pageable pageable) {
        return courierRepository.findAll(pageable);
    }

}

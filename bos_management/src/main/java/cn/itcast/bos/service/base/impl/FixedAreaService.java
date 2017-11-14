package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.IFixedAreaRepository;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.IFixedAreaService;

@Service("fixedAreaService")
@Transactional
public class FixedAreaService implements IFixedAreaService {

    @Autowired
    private IFixedAreaRepository fixedAreaRepository;

    @Override
    public void save(FixedArea fixedArea) {
        fixedAreaRepository.save(fixedArea);
    }

    @Override
    public Page<FixedArea> pageQuery(Specification<FixedArea> specification, Pageable pageable) {
        return fixedAreaRepository.findAll(specification, pageable);
    }

}

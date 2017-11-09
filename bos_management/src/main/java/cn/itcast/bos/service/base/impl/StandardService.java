package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.IStandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.IStandardService;

@Service("standardService")
@Transactional
public class StandardService implements IStandardService {

    @Autowired
    private IStandardRepository standardRepository;

    @Override
    public void save(Standard standard) {
        // TODO Auto-generated method stub
        standardRepository.save(standard);
    }

}

package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.IPromotionRepository;
import cn.itcast.bos.domain.take_delivery.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.IPromotionService;

@Service("promotionService")
@Transactional
public class PromotionService implements IPromotionService {

    @Autowired
    private IPromotionRepository promotionRepository;

    @Override
    public void save(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    @Override
    public Page<Promotion> findPageData(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    @Override
    public PageBean<Promotion> findPageData(int page, int rows) {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> pageData = promotionRepository.findAll(pageable);

        // 将得到的分页数据封装到自己的bean对象中
        PageBean<Promotion> pageBean = new PageBean<Promotion>();
        pageBean.setTotalCount(pageData.getTotalElements());
        pageBean.setPageData(pageData.getContent());

        return pageBean;
    }

    @Override
    public Promotion findById(Integer id) {
        return promotionRepository.findOne(id);
    }

    @Override
    public void updateStatus(Date date) {
        promotionRepository.updateStatus(date);
    }

}

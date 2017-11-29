package cn.itcast.bos.service.take_delivery.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.IWayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.IWayBillService;

@Service("wayBillService")
@Transactional
public class WayBillService implements IWayBillService {

    @Autowired
    private IWayBillRepository wayBillRepository;

    @Override
    public void save(WayBill wayBill) {
        // 判断运单号是否存在
        WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if (persistWayBill == null || persistWayBill.getId() == null) {
            // 运单不存在
            wayBillRepository.save(wayBill);
        } else {
            // 运单存在
            try {
                Integer id = persistWayBill.getId();
                // 将前台传过来的运单详情赋值给之前相同的快速运单，但是要保持id不能变，不然就成了一条新数据
                BeanUtils.copyProperties(persistWayBill, wayBill);
                persistWayBill.setId(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public Page<WayBill> findPageData(Pageable pageable) {
        return wayBillRepository.findAll(pageable);
    }

    @Override
    public WayBill findWayBill(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }

}

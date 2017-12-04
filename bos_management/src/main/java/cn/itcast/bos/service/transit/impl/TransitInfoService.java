package cn.itcast.bos.service.transit.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.IWayBillRepository;
import cn.itcast.bos.dao.transit.ITransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.ITransitInfoService;

@Service("transitInfoService")
@Transactional
public class TransitInfoService implements ITransitInfoService {

    @Autowired
    private ITransitInfoRepository transitInfoRepository;
    @Autowired
    private IWayBillRepository wayBillRepository;

    @Override
    public void createTransits(String wayBillIds) {
        if (StringUtils.isNotBlank(wayBillIds)) {
            // 处理运单
            for (String wayBillId : wayBillIds.split(",")) {
                WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
                // 判断运单状态是否为待发货状态
                if (wayBill.getSignStatus() == 1) {
                    // 代发货，生成tansitInfo信息
                    TransitInfo transitInfo = new TransitInfo();
                    transitInfo.setWayBill(wayBill);
                    transitInfo.setStatus("出入库中转");
                    transitInfoRepository.save(transitInfo);

                    // 更改运单状态
                    wayBill.setSignStatus(2);
                }
            }
        }
    }

}

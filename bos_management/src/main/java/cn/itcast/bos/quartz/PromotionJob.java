package cn.itcast.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.itcast.bos.service.take_delivery.IPromotionService;

public class PromotionJob implements Job {

    @Autowired
    @Qualifier("promotionService")
    private IPromotionService promotionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("活动过期程序处理执行。。。。。");
        promotionService.updateStatus(new Date());
    }

}

package com.wf.imaotai.task;

import com.wf.imaotai.service.IMTService;
import com.wf.imaotai.service.ShopService;
import com.wf.imaotai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class IMTTask {

    @Autowired
    public ShopService shopService;

    @Autowired
    public IMTService imtService;

    @Autowired
    public UserService userService;

    /**
     * 1：10 批量修改用户随机预约的时间
     */
    @Async
    @Scheduled(cron = "0 10 1 ? * * ")
    public void updateUserMinuteBatch() {
//        iUserService.updateUserMinuteBatch();
    }


    /**
     * 11点期间，每分钟执行一次批量获得旅行奖励
     */
    @Async
    @Scheduled(cron = "0 0/1 11 ? * *")
    public void travelRewardBatch() {
//        imtService.travelRewardBatch();

    }

    /**
     * 9点期间，每分钟执行一次
     */
    @Async
    @Scheduled(cron = "0 0/1 9 ? * *")
    public void reservationBatchTask() {
//        imtService.reservationBatch();

    }


    @Async
    @Scheduled(cron = "0 10,55 7,8 ? * * ")
    public void refresh() {
        log.info("「刷新数据」开始刷新版本号，预约item，门店shop列表  ");
        try {
            imtService.refreshAll();
        } catch (Exception e) {
            log.info("「刷新数据执行报错」%s", e.getMessage());
        }

    }

    /**
     * 18.05分获取申购结果
     */
    @Async
    @Scheduled(cron = "0 5 18 ? * * ")
    public void appointmentResults() {
        imtService.appointmentResults();
    }
}

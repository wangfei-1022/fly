package com.wf.imaotai.task;

import com.wf.imaotai.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class IMTTask {

    @Autowired
    public ShopService shopService;
    /**
     * 9点期间，每分钟执行一次
     */
    @Async
    @Scheduled(cron = "0 0/1 9 ? * *")
    public void reservationBatchTask() {
        shopService.reservationBatch();
    }
}

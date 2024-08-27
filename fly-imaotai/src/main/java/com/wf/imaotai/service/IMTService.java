package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;
import org.springframework.scheduling.annotation.Async;

public interface IMTService {

    String getMTVersion();

    void reservation(Long mobile);


    boolean sendCode(Long mobile, String deviceId);

    boolean login(Long mobile, String code, String deviceId);

    //获取申购耐力值
    String getEnergyAward(User iUser);

    void travelReward(Long mobile);

    @Async
    void reservationBatch();

    @Async
    void travelRewardBatch();

    void refreshAll();

    void appointmentResults();
}

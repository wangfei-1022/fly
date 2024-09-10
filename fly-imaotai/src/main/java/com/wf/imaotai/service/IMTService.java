package com.wf.imaotai.service;

import com.wf.imaotai.entity.User;
import org.springframework.scheduling.annotation.Async;

public interface IMTService {

    void reservation(User user);


    boolean sendCode(String mobile, String deviceId);

    boolean login(String mobile, String code, String deviceId);

    void travelReward(String mobile);

    @Async
    void reservationBatch();

    @Async
    void travelRewardBatch();

    void refreshAll();

    void appointmentResults();
}

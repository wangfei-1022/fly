package com.wf.imaotai.service;

public interface IMTService {

    String getMTVersion();

    void reservation(Long mobile);


    boolean sendCode(Long mobile, String deviceId);

    boolean login(Long mobile, String code, String deviceId);
}

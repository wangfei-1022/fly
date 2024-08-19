package com.wf.imaotai.service;

import com.wf.imaotai.entity.Log;
import com.wf.imaotai.entity.Shop;

import java.util.List;

public interface LogService {
    List<Log> list();
    int log(Log log);
    int logError(String message);
}

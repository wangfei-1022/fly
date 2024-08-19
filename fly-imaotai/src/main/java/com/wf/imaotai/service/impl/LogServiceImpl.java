package com.wf.imaotai.service.impl;

import com.wf.imaotai.entity.Log;
import com.wf.imaotai.service.LogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Override
    public List<Log> list() {
        return null;
    }

    @Override
    public int log(Log log) {
        return 0;
    }

    @Override
    public int logError(String message) {
        Log log = new Log();
        log(log);
        return 0;
    }
}

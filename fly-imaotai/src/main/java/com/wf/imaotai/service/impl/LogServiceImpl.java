package com.wf.imaotai.service.impl;

import com.github.pagehelper.PageHelper;
import com.wf.imaotai.entity.Log;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.mapper.LogMapper;
import com.wf.imaotai.model.request.LogRequest;
import com.wf.imaotai.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    public LogMapper logMapper;

    @Override
    public List<Log> list(LogRequest logRequest) {
        PageHelper.startPage(logRequest.initPage());
        List<Log> logs = logMapper.getList();
        return logs;
    }

    @Override
    public int record(User user, String content) {
        Log log = new Log();
        log.setContent(content);
        log.setCreateTime(new Date());
        log.setMobile(user.getMobile());
        log.setUserId(user.getUserId());
        return logMapper.insetLog(log);
    }
}

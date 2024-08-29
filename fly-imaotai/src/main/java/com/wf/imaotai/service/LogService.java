package com.wf.imaotai.service;

import com.wf.imaotai.entity.Log;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.model.request.LogRequest;

import java.util.List;

public interface LogService {
    List<Log> list(LogRequest logRequest);
    int record(User user, String content);
}

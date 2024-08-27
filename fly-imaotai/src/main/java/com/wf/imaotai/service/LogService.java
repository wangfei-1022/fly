package com.wf.imaotai.service;

import com.wf.imaotai.entity.Log;
import com.wf.imaotai.entity.User;

import java.util.List;

public interface LogService {
    List<Log> list();
    int record(User user, String content);
}

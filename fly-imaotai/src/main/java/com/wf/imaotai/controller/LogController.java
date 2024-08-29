package com.wf.imaotai.controller;

import com.github.pagehelper.PageInfo;
import com.wf.common.common.R;
import com.wf.imaotai.entity.Log;
import com.wf.imaotai.entity.User;
import com.wf.imaotai.model.request.LogRequest;
import com.wf.imaotai.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/imt/log")
public class LogController {

    @Autowired
    public LogService logService;

    @GetMapping("list")
    public R list(LogRequest logRequest) {
        List<Log> logs = logService.list(logRequest);
        PageInfo<Log> itemPageInfo = new PageInfo<>(logs);
        return R.success(itemPageInfo);
    }
}

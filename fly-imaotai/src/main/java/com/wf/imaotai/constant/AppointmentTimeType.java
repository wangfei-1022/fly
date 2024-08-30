package com.wf.imaotai.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppointmentTimeType {

    RANDOM(1, "随机时间"),
    TARGET(2, "指定时间");

    private int code;
    private String name;
}

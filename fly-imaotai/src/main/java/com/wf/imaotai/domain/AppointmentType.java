package com.wf.imaotai.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppointmentType {

    MAX(1, "预约本市量最大门店"),
    CLOSE(2, "预约最近门店"),
    TARGET(3, "指定城市门店");

    private int code;
    private String name;
}

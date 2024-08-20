package com.wf.imaotai.domain;

import com.wf.imaotai.model.dto.SelectionI;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType implements SelectionI {

    SEA_EXPORT("sea", "海运订单"),
    AIR_EXPORT("air", "海运订单"),

    ;


    private String code;

    private String name;


}

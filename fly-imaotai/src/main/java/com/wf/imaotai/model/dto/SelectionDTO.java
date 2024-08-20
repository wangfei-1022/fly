package com.wf.imaotai.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectionDTO<T> implements SelectionI<T> {

    private T code;

    private String name;


}

package com.wf.imaotai.model.request;

import com.wf.common.model.request.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest extends BasePageRequest {
    private Long mobile;
}
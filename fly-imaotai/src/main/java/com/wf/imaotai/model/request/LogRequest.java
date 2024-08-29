package com.wf.imaotai.model.request;

import com.wf.common.model.request.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogRequest extends BasePageRequest {
    private String mobile;
}

package com.wf.imaotai.model.request;

import com.wf.common.model.request.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopRequest extends BasePageRequest {

    private String provinceName;

    private String cityName;

    private String districtName;

    private String tenantName;
}

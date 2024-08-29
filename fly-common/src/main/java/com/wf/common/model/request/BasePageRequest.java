package com.wf.common.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class BasePageRequest extends PageRequest {
    private String createTimeStart;

    private String createTimeEnd;
    private List<Long> ids;
    private Long id;
    public Boolean isEmptyRequest() {
        return Objects.isNull(this.createTimeStart)
                && Objects.isNull(this.createTimeEnd)
                && Objects.isNull(this.ids)
                && Objects.isNull(this.id);
    }
}

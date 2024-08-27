package com.wf.imaotai.model.dto;

import lombok.Data;

@Data
public class MapPoint {
    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    public MapPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

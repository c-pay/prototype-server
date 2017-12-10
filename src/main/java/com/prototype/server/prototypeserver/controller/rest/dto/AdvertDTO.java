package com.prototype.server.prototypeserver.controller.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototype.server.prototypeserver.entity.Advert;

import java.util.List;

public class AdvertDTO {

    @JsonProperty("data")
    private List<Advert> data;


    public AdvertDTO() {
    }

    public AdvertDTO(List<Advert> data) {
        this.data = data;
    }

    public List<Advert> getData() {
        return data;
    }

    public void setData(List<Advert> data) {
        this.data = data;
    }
}

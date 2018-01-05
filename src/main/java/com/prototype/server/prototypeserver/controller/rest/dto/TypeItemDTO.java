package com.prototype.server.prototypeserver.controller.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.TypeItem;

import java.util.List;

public class TypeItemDTO {

    @JsonProperty("data")
    private List<TypeItem> data;


    public TypeItemDTO() {
    }

    public TypeItemDTO(List<TypeItem> data) {
        this.data = data;
    }

    public List<TypeItem> getData() {
        return data;
    }

    public void setData(List<TypeItem> data) {
        this.data = data;
    }
}

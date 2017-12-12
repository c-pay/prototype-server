package com.prototype.server.prototypeserver.controller.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototype.server.prototypeserver.entity.Item;

import java.util.List;

public class ItemDTO {

    @JsonProperty("data")
    private List<Item> data;

    public ItemDTO() {
    }

    public ItemDTO(List<Item> data) {
        this.data = data;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }
}

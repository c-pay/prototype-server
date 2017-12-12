package com.prototype.server.prototypeserver.controller.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototype.server.prototypeserver.entity.Transaction;

import java.util.List;

public class TransactionDTO {
    @JsonProperty("data")
    private List<Transaction> data;

    public TransactionDTO() {
    }

    public TransactionDTO(List<Transaction> data) {
        this.data = data;
    }

    public List<Transaction> getData() {
        return data;
    }

    public void setData(List<Transaction> data) {
        this.data = data;
    }
}

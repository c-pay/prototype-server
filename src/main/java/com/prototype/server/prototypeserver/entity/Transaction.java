package com.prototype.server.prototypeserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tx")
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    private String toAddress;
    private String fromAddress;
    private String value;
    private String hashTx;
    private String dateTx;
    private String rate;
}

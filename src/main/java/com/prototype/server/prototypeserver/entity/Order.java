package com.prototype.server.prototypeserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id",length = 19)
    private long id;

    @ManyToMany
    @JoinTable(name = "jnd_order", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> items = new HashSet<>();
    private Status status;
    private String hashTx;
    private String address;
    private Date date;
    private Double sum;
    private Double ethSum;

    public enum Status{
        PAID, WAIT
    }
}

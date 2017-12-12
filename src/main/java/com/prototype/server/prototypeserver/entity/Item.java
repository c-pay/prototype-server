package com.prototype.server.prototypeserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item")
@NoArgsConstructor
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id",length = 19)
    private long id;
    @Column(name="title", length = 150, nullable = false)
    private String title;
    private long price;
    @JsonIgnore
    @ManyToOne
    private Advert advert;


}
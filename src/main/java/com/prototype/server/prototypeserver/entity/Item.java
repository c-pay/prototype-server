package com.prototype.server.prototypeserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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
    private float price;
    @Column(name="price_currency")
    private float priceCurrency;
    @JsonIgnore
    @ManyToOne
    private Advert advert;
    @ManyToOne
    private Section section;
}
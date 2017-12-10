package com.prototype.server.prototypeserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "advert")
@NoArgsConstructor
@Getter @Setter
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id",length = 19)
    private long id;
    @Column(name="title", length = 150, nullable = false)
    private String title;
    @Column(name="description" , length = 500, nullable = true)
    private String description;
    @Lob
    private byte[] pic;

}

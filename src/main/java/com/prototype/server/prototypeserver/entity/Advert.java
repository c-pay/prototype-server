package com.prototype.server.prototypeserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

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
    @JsonIgnore
    @ManyToOne
    private User user;
    @ManyToOne
    private TypeItem typeItem;
    @JsonIgnore
    @OneToMany(mappedBy = "advert", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Item> items;

    @Column(name="wallet", length = 150, nullable = false)
    private String wallet;

    @Column(name="address" , length = 300, nullable = true)
    private String address;
    @Column(name="add_address" , length = 300, nullable = true)
    private String addAddress;
    @Column(name="latitude" , nullable = true)
    private double latitude;
    @Column(name="longitude" , nullable = true)
    private double longitude;

    @Column(name="tel" , length = 15, nullable = true)
    private String tel;
    @Column(name="site" , length = 150, nullable = true)
    private String site;
    @Column(name="email" , length = 150, nullable = true)
    private String email;
}

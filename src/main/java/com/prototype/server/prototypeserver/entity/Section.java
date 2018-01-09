package com.prototype.server.prototypeserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "section")
@NoArgsConstructor
@Getter
@Setter
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id",length = 12)
    private int id;
    @Column(name="title", length = 100, nullable = false)
    private String title;
    @Lob
    private byte[] pic;
}

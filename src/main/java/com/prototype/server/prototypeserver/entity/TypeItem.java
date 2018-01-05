package com.prototype.server.prototypeserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "type_item")
@NoArgsConstructor
@Getter
@Setter
public class TypeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id",length = 5)
    private long id;
    @Column(name="title", length = 40, nullable = false)
    private String title;
    @Lob
    private byte[] pic;

    public TypeItem(String title) {
        this.title = title;
    }
}

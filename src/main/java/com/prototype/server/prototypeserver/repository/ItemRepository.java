package com.prototype.server.prototypeserver.repository;

import com.prototype.server.prototypeserver.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("itemRepository")
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.advert.id = ?1 ")
    List<Item> findByAdvert(long id);
    @Query("select i.id, i.title, i.price from Item i where i.advert.id = ?1 ")
    List<Item> findByAdvertLight(long id);
}

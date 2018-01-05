package com.prototype.server.prototypeserver.repository;

import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.TypeItem;
import com.prototype.server.prototypeserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("typeItemRepository")
public interface TypeItemRepository extends JpaRepository<TypeItem, Long> {
    TypeItem findByTitle(String title);
    TypeItem findById(long id);
}

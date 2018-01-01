package com.prototype.server.prototypeserver.repository;

import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("advertRepository")
public interface AdvertRepository extends JpaRepository<Advert, Long> {
    List<Advert> findAllByUser(User user);
}

package com.prototype.server.prototypeserver.repository;

import com.prototype.server.prototypeserver.entity.Advert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("advertRepository")
public interface AdvertRepository extends JpaRepository<Advert, Long> {
}

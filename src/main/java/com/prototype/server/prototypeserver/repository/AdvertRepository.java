package com.prototype.server.prototypeserver.repository;

import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("advertRepository")
public interface AdvertRepository extends JpaRepository<Advert, Long> {
    List<Advert> findAllByUser(User user);
//    @Query("SELECT a.id as id, a.title as title, a.description as description, a.wallet, a.typeItem.id, a.typeItem.title, a.address, a.addAddress, a.latitude, a.longitude" +
//            ", a.tel, a.site, a.email from Advert a")
//    List<Advert> findAllLite();
    Advert findFirstByWallet(String wallet);

}

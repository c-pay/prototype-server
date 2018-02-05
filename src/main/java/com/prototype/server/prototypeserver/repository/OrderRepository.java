package com.prototype.server.prototypeserver.repository;

import com.prototype.server.prototypeserver.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {

        List<Order> findOrderByAddress(String address);
}

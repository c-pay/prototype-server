package com.prototype.server.prototypeserver.repository;

import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("transactionRepository")
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByToAddressOrFromAddress(String toAddress, String fromAddress);
}

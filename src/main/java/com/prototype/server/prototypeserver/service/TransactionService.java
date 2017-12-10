package com.prototype.server.prototypeserver.service;


import com.prototype.server.prototypeserver.entity.Transaction;
import com.prototype.server.prototypeserver.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionService")
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction){

        return transactionRepository.save(transaction);
    }
}

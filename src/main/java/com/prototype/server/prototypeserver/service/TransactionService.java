package com.prototype.server.prototypeserver.service;


import com.prototype.server.prototypeserver.entity.Order;
import com.prototype.server.prototypeserver.entity.Transaction;
import com.prototype.server.prototypeserver.repository.OrderRepository;
import com.prototype.server.prototypeserver.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("transactionService")
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Transaction saveTransaction(Transaction transaction){

        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAllTransactionByAddress(String address){
       return transactionRepository.findByToAddressOrFromAddress(address, address);
    }

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }

    public List<Order> findAllOrderByAddress(String address){
        return orderRepository.findOrderByAddress(address);
    }
}

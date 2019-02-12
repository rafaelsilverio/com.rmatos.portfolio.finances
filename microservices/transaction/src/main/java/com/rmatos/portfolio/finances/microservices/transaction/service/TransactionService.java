package com.rmatos.portfolio.finances.microservices.transaction.service;

import com.rmatos.portfolio.finances.microservices.transaction.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction insert(Transaction transaction);
    Transaction edit(String id, Transaction transaction);
    void delete(String id);
    List<Transaction> findAll(Long cnpj);
    Transaction findDetailed(String id);
    List<Transaction> transactionReport(Long cnpj, Integer month, Integer year);
}

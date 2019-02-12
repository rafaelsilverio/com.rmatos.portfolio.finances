package com.rmatos.portfolio.finances.microservices.transaction.repository;

import com.rmatos.portfolio.finances.microservices.transaction.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends CrudRepository<Transaction, Long>  {
    Transaction findById(UUID id);
    List<Transaction> findByCnpj(Long cnpj);
    List<Transaction> findByCnpjAndDateBetween(Long cnpj, Date initial, Date ending);
}

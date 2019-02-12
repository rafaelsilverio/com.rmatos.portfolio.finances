package com.rmatos.portfolio.finances.microservices.transaction.controller;

import com.rmatos.portfolio.finances.microservices.transaction.configuration.ResponseMessage;
import com.rmatos.portfolio.finances.microservices.transaction.entity.Transaction;
import com.rmatos.portfolio.finances.microservices.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ResponseMessage responseMessage;

    @PostMapping
    public ResponseEntity<Transaction> createFinancialTransaction(@RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionService.insert(transaction), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> editFinancialTransaction(@PathVariable String id,
                                                                    @RequestBody Transaction transaction){
        return ResponseEntity.ok(transactionService.edit(id, transaction));
    }

    @GetMapping("/all/{cnpj}")
    public ResponseEntity<List<Transaction>> findAllFinancialTransaction(@PathVariable Long cnpj) {
        return ResponseEntity.ok(transactionService.findAll(cnpj));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> detailedFinancialTransaction(@PathVariable String id) {
        return ResponseEntity.ok(transactionService.findDetailed(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFinancialTransaction(@PathVariable String id) {
        transactionService.delete(id);
        return ResponseEntity.ok().body(responseMessage.getMessageDeleteSuccess());
    }

    @GetMapping("/report/{cnpj}/{month}/{year}")
    public ResponseEntity<List<Transaction>> generateFinancialTransactionReport(@PathVariable Long cnpj,
                                                                      @PathVariable Integer month,
                                                                      @PathVariable Integer year) {
        return ResponseEntity.ok().body(transactionService.transactionReport(cnpj, month, year));
    }
}

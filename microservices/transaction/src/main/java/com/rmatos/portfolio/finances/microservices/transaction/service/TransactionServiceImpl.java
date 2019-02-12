package com.rmatos.portfolio.finances.microservices.transaction.service;

import com.rmatos.portfolio.finances.microservices.transaction.entity.Transaction;
import com.rmatos.portfolio.finances.microservices.transaction.exception.MicroserviceGenericException;
import com.rmatos.portfolio.finances.microservices.transaction.configuration.ResponseMessage;
import com.rmatos.portfolio.finances.microservices.transaction.repository.TransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository financialTransactionRepository;

    @Autowired
    private ResponseMessage responseMessage;

    @Override
    public Transaction insert(Transaction transaction) {
        validateFields(transaction);
        return financialTransactionRepository.save(transaction);
    }

    @Override
    public Transaction edit(String id, Transaction transaction) {
        fillFinancialTransaction(id);
        validateFields(transaction);
        transaction.setId(UUID.fromString(id));
        financialTransactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public void delete(String id) {
        Transaction persistedTransaction = fillFinancialTransaction(id);
        financialTransactionRepository.delete(persistedTransaction);
    }

    @Override
    public List<Transaction> findAll(Long cnpj) {
        List<Transaction> list = new ArrayList<>();
        Iterable<Transaction> result = financialTransactionRepository.findByCnpj(cnpj);
        result.iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Transaction findDetailed(String id) {
        return fillFinancialTransaction(id);
    }

    @Override
    public List<Transaction> transactionReport(Long cnpj, Integer month, Integer year) {
        try {
            LocalDate.of(year, month, 1);
        } catch (DateTimeException e) {
            throw new MicroserviceGenericException(responseMessage.getMessageInvalidDates(), HttpStatus.BAD_REQUEST);
        }

        Calendar calendarInitial = Calendar.getInstance();
        calendarInitial.set(Calendar.MONTH, month - 1);
        calendarInitial.set(Calendar.YEAR, year);
        calendarInitial.set(Calendar.DAY_OF_MONTH, 1);

        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.set(Calendar.MONTH, month - 1);
        calendarFinal.set(Calendar.YEAR, year);
        calendarFinal.set(Calendar.DAY_OF_MONTH, 1);
        calendarFinal.set(Calendar.DATE, calendarFinal.getActualMaximum(Calendar.DATE));

        return financialTransactionRepository.findByCnpjAndDateBetween(cnpj, calendarInitial.getTime(), calendarFinal.getTime());
    }

    private void validateFields(Transaction transaction) {
        List<String> invalidFields = new ArrayList<>();
        if (transaction.getCnpj() == null) {
            invalidFields.add("cnpj");
        }
        if (transaction.getDate() == null) {
            invalidFields.add("date");
        }
        if (transaction.getValue() == null || transaction.getValue() < 0) {
            invalidFields.add("total_in_cents");
        }
        if (StringUtils.isEmpty(transaction.getDescription())) {
            invalidFields.add("description");
        }
        if (!invalidFields.isEmpty()) {
            throw new MicroserviceGenericException(String.format(responseMessage.getMessageInvalidFields(), StringUtils.join(invalidFields, ",")),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private Transaction fillFinancialTransaction(String id) {
        UUID uuid;
        try{
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException exception){
            throw new MicroserviceGenericException(responseMessage.getMessageInvalidId(), HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = financialTransactionRepository.findById(uuid);
        if (transaction == null) {
            throw new MicroserviceGenericException(responseMessage.getMessageNotFound(), HttpStatus.NOT_FOUND);
        }
        return transaction;
    }
}

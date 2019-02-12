package com.rmatos.portfolio.finances.microservices.transaction.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponseMessage {

    @Value("${return.messages.success}")
    private String messageSuccess;

    @Value("${return.messages.save.success}")
    private String messageSaveSuccess;

    @Value("${return.messages.invalid.fields}")
    private String messageInvalidFields;

    @Value("${return.messages.invalid.id}")
    private String messageInvalidId;

    @Value("${return.messages.invalid.dates}")
    private String messageInvalidDates;

    @Value("${return.messages.not.found}")
    private String messageNotFound;

    @Value("${return.messages.delete.success}")
    private String messageDeleteSuccess;

    public ResponseMessage() {

    }

    public String getMessageNotFound() {
        return messageNotFound;
    }

    public void setMessageNotFound(String messageNotFound) {
        this.messageNotFound = messageNotFound;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public String getMessageSaveSuccess() {
        return messageSaveSuccess;
    }

    public void setMessageSaveSuccess(String messageSaveSuccess) {
        this.messageSaveSuccess = messageSaveSuccess;
    }

    public String getMessageInvalidFields() {
        return messageInvalidFields;
    }

    public void setMessageInvalidFields(String messageInvalidFields) {
        this.messageInvalidFields = messageInvalidFields;
    }

    public String getMessageInvalidId() {
        return messageInvalidId;
    }

    public void setMessageInvalidId(String messageInvalidId) {
        this.messageInvalidId = messageInvalidId;
    }

    public String getMessageInvalidDates() {
        return messageInvalidDates;
    }

    public void setMessageInvalidDates(String messageInvalidDates) {
        this.messageInvalidDates = messageInvalidDates;
    }

    public String getMessageDeleteSuccess() {
        return messageDeleteSuccess;
    }

    public void setMessageDeleteSuccess(String messageDeleteSuccess) {
        this.messageDeleteSuccess = messageDeleteSuccess;
    }
}

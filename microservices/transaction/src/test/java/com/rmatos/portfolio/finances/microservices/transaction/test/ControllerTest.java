package com.rmatos.portfolio.finances.microservices.transaction.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmatos.portfolio.finances.microservices.transaction.TransactionApplication;
import com.rmatos.portfolio.finances.microservices.transaction.configuration.ResponseMessage;
import com.rmatos.portfolio.finances.microservices.transaction.entity.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApplication.class)
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResponseMessage responseMessage;

    @Test
    public void saveFinancialTransactionValidationMessageEmptyEntity() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Transaction transaction = new Transaction();
        String jsonParam = mapper.writeValueAsString(transaction);
        MvcResult result = this.mockMvc.perform(post("/transaction/").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.assertEquals(content, String.format(responseMessage.getMessageInvalidFields(), "cnpj,date,total_in_cents,description"));
    }

    @Test
    public void deleteFinancialTransactionSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Long cnpj = 66640364000144L;
        Transaction transaction = new Transaction(66640364000144L, new Date(), 1000, "IR");
        String jsonParam = mapper.writeValueAsString(transaction);

        //Persist Transaction
        this.mockMvc.perform(post("/transaction/").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
                .andDo(print())
                .andExpect(status().isCreated());

        //Get the list of persisted FinancialTransactions
        ResultActions resultActions = this.mockMvc.perform(get("/transaction/all/" + cnpj))
                .andDo(print())
                .andExpect(status().isOk());
        List<Transaction> transactions = mapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<List<Transaction>>() {});

        //Delete the first one
        String firsFinancialTransactionId = transactions.get(0).getId().toString();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/transaction/" + firsFinancialTransactionId))
                .andDo(print())
                .andExpect(status().isOk());

        //Try to request it by id
        MvcResult result = this.mockMvc.perform(get("/transaction/" + firsFinancialTransactionId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.assertEquals(content, responseMessage.getMessageNotFound());
    }

    @Test
    public void createFinancialTransactionReportSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Long cnpj = 47166596000168L;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<Transaction> januaryList = new ArrayList<>();
        List<Transaction> mayList = new ArrayList<>();
        januaryList.add(new Transaction(cnpj, formatter.parse("2018-01-15"), 1000, "IR"));
        januaryList.add(new Transaction(cnpj, formatter.parse("2018-01-20"), 1000, "MEDIC"));

        mayList.add(new Transaction(cnpj, formatter.parse("2018-05-15"), 1000, "IR"));
        mayList.add(new Transaction(cnpj, formatter.parse("2018-05-20"), 1000, "MEDIC"));
        mayList.add(new Transaction(cnpj, formatter.parse("2018-05-21"), 1000, "ASSETS"));

        //Persists Lists
        String jsonParam;
        for (Transaction transaction : januaryList) {
            jsonParam = mapper.writeValueAsString(transaction);
            this.mockMvc.perform(post("/transaction/").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }
        for (Transaction transaction : mayList) {
            jsonParam = mapper.writeValueAsString(transaction);
            this.mockMvc.perform(post("/transaction/").contentType(MediaType.APPLICATION_JSON).content(jsonParam))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        //Get january report
        ResultActions resultActionsJanuary = this.mockMvc.perform(get("/transaction/report/" + cnpj + "/1/2018"))
                .andDo(print())
                .andExpect(status().isOk());
        List<Transaction> transactionsJanuary = mapper.readValue(resultActionsJanuary.andReturn().getResponse().getContentAsString(), new TypeReference<List<Transaction>>() {});

        //Get may report
        ResultActions resultActionsMay = this.mockMvc.perform(get("/transaction/report/" + cnpj + "/5/2018"))
                .andDo(print())
                .andExpect(status().isOk());
        List<Transaction> transactionsMay = mapper.readValue(resultActionsMay.andReturn().getResponse().getContentAsString(), new TypeReference<List<Transaction>>() {});

        //Check the report
        Assert.assertEquals("Report size for January", 2, transactionsJanuary.size());
        Assert.assertEquals("Report size for May", 3, transactionsMay.size());
    }
}

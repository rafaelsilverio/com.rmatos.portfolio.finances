package com.rmatos.portfolio.finances.aem.core.controller;

import com.adobe.cq.sightly.WCMUsePojo;
import com.rmatos.portfolio.finances.aem.core.config.EndpointConfig;

import java.io.IOException;

public class FinanceGenericController extends WCMUsePojo {
    private EndpointConfig endpointConfig;

    @Override
    public void activate() throws Exception {
        endpointConfig = getSlingScriptHelper().getService(EndpointConfig.class);
    }

    public String getFinanceEndpoint() throws IOException {
        return endpointConfig.getServiceURL();
    }
}

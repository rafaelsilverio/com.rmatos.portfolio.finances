package com.rmatos.portfolio.finances.aem.core.config;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;

@Component(service = EndpointConfig.class,immediate = true)
public class EndpointConfig {

    @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
    private ConfigurationAdmin configAdmin;

    public String getServiceURL() throws IOException {
        return (String) getConfiguration().getProperties().get("service.url");
    }

    private Configuration getConfiguration() throws IOException{
        return configAdmin.getConfiguration(EndpointConfig.class.getName());
    }
}

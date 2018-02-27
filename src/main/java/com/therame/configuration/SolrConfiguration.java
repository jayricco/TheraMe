package com.therame.configuration;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.HttpSolrClientFactoryBean;

@Configuration
public class SolrConfiguration {

    @Bean
    public HttpSolrClientFactoryBean solrServerFactoryBean() {
        HttpSolrClientFactoryBean factory = new HttpSolrClientFactoryBean();
        factory.setUrl("http://localhost:8983/solr/");
        return factory;
    }

    @Bean
    public SolrOperations solrTemplate() throws Exception {
        SolrTemplate solrTemplate = new SolrTemplate(solrClient());
        return solrTemplate;
    }

    @Bean
    public SolrClient solrClient() throws Exception {
         return solrServerFactoryBean().getObject();
    }
}

package com.example.esstudy.demo.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class EsConfig extends AbstractElasticsearchConfiguration {
    @Value("${my.elastacsearch.hostAddress}")
    private String esHostAddress;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {


//        如果以后要拓展为多个es，则用;分割
        String[] hostAddress = esHostAddress.split(";");
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo(hostAddress).build();
        return RestClients.create(clientConfiguration).rest();

    }
}

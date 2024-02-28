package com.example.demo.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// esta es una clase de configuracion
@Configuration
public class RestTemplateConfig {

    // lo inyectamos con el "bean"
    @Bean
    @LoadBalanced // creamos una instancia del rest template
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

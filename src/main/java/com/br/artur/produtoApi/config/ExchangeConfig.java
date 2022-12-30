package com.br.artur.produtoApi.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange("ProductExchange").durable(true).build();
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("toProductQueue");
    }
}

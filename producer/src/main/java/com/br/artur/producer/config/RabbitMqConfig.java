package com.br.artur.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String routingKey = "toProductQueue";
    public static final String exchangeName = "ProductExchange";
    public static final String queueName = "ProductQueue";

    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Queue productQueue(){
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(routingKey);
    }
}

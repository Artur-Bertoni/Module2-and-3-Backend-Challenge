package com.br.artur.producer.service;

import com.br.artur.producer.dto.ProductDto;
import com.br.artur.producer.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage (String exchange, String routingKey, ProductDto body, String header) throws JsonProcessingException {
        var jsonRequest = objectMapper.writeValueAsString(body);
        rabbitTemplate.convertAndSend(exchange,routingKey,jsonRequest, message -> {
            message.getMessageProperties().setHeader("EVENT",header);
            return message;});
    }

    public void sendMessageList (String exchange, String routingKey, List<Product> body, String header) throws JsonProcessingException {
        var jsonRequest = objectMapper.writeValueAsString(body);
        rabbitTemplate.convertAndSend(exchange,routingKey,jsonRequest, message -> {
            message.getMessageProperties().setHeader("EVENT",header);
            return message;});
    }
}

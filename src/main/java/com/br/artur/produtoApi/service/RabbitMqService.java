package com.br.artur.produtoApi.service;

import com.br.artur.produtoApi.dto.ProductDto;
import com.br.artur.produtoApi.dto.RequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage (String exchange, String routingKey, ProductDto productDto) throws JsonProcessingException {
        var jsonRequest = objectMapper.writeValueAsString(productDto);
        rabbitTemplate.convertAndSend(exchange,routingKey,jsonRequest, message -> {
            message.getMessageProperties().setHeader("EVENT","PRODUCT_CHANGE");
            return message;});
    }
}

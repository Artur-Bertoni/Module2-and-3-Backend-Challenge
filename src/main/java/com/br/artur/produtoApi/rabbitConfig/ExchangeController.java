package com.br.artur.produtoApi.rabbitConfig;

import com.br.artur.produtoApi.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.mapping.Any;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ExchangeController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/exchange/{exchange}/{routingKey}")
    public HttpEntity<Any> postProductOnExchange(@PathVariable String exchange,@PathVariable String routingKey,@RequestBody Product message) throws JsonProcessingException {
        var messageConvert = objectMapper.writeValueAsString(message);
        rabbitTemplate.convertAndSend(exchange, routingKey, messageConvert);
        return ResponseEntity.ok().build();
    }
}

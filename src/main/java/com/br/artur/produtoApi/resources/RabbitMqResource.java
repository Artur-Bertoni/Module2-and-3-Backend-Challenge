package com.br.artur.produtoApi.resources;

import com.br.artur.produtoApi.config.RabbitMqConfig;
import com.br.artur.produtoApi.dto.RequestDto;
import com.br.artur.produtoApi.service.RabbitMqService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/messages")
public class RabbitMqResource {

    @Autowired
    private RabbitMqService rabbitMqService;


    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity postByMessage(@RequestBody RequestDto request) throws JsonProcessingException {
        var jsonRequest = objectMapper.writeValueAsString(request);
        rabbitMqService.sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey, jsonRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}

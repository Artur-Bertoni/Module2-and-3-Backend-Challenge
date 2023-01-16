package com.br.artur.produtoApi.consumer;

import com.br.artur.produtoApi.config.RabbitMqConfig;
import com.br.artur.produtoApi.convert.ProductConvert;
import com.br.artur.produtoApi.dto.ProductDto;
import com.br.artur.produtoApi.entity.Product;
import com.br.artur.produtoApi.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class ProductConsumer {

    @Autowired
    ProductRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMqConfig.queueName)
    public void consumer(Message<String> message) throws IOException {

        Product product = objectMapper.readValue(message.getPayload(),Product.class);

        var messageHeader = message.getHeaders().get("EVENT");
        var productBody = message.getPayload();
        log.info("Receive message '"+messageHeader+"' from queue '"+RabbitMqConfig.queueName+"'");
        log.info("Message Body: "+productBody);

        switch (Objects.requireNonNull(messageHeader).toString()) {
            case "PRODUCT_CHANGE":
                ProductConvert.toDto(this.repository.save(product));
            case "GET_ALL":

        }
    }
}

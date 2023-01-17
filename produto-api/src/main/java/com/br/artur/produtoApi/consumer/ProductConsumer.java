package com.br.artur.produtoApi.consumer;

import com.br.artur.produtoApi.config.RabbitMqConfig;
import com.br.artur.produtoApi.convert.ProductConvert;
import com.br.artur.produtoApi.dto.ProductDto;
import com.br.artur.produtoApi.entity.Product;
import com.br.artur.produtoApi.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductConsumer {

    @Autowired
    ProductRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMqConfig.queueName)
    public void consumer(Message<String> message) throws IOException {

        var messageHeader = message.getHeaders().get("EVENT");
        var messageBody = message.getPayload();

        log.info("Received message '"+messageHeader+"' from queue '"+RabbitMqConfig.queueName+"'");
        log.info("Message Body: "+messageBody);

        switch (Objects.requireNonNull(messageHeader).toString()) {
            case "PRODUCT_CHANGE":
            case "PRODUCT_POST":
            case "PRODUCT_UPDATE":
                Product product = objectMapper.readValue(message.getPayload(),Product.class);

                this.repository.save(product);
                break;
            case "PRODUCT_POST_BY_CSV":
                List<Product> products = objectMapper.readValue(message.getPayload(), new TypeReference<>() {});

                repository.saveAll(products).stream().map(ProductConvert::toDto).collect(Collectors.toList());
                break;
            case "PRODUCT_DELETE":
                product = objectMapper.readValue(message.getPayload(),Product.class);

                this.repository.deleteById(product.getId());
                break;
        }
    }
}

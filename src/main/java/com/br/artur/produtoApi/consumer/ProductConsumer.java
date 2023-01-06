package com.br.artur.produtoApi.consumer;

import com.br.artur.produtoApi.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductConsumer {

    @RabbitListener(queues = RabbitMqConfig.queueName)
    public void consumer(Message<String> message) {
        log.info("Receive message from '"+RabbitMqConfig.queueName+"'");
        var product = message.getPayload();
        var productHeader = message.getHeaders().get("EVENT");
        log.info("Message Body: "+product);
        log.info("Message header: "+productHeader);
    }
}

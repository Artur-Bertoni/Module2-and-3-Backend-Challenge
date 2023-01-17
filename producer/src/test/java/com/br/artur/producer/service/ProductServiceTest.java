package com.br.artur.producer.service;

import com.br.artur.producer.config.RabbitMqConfig;
import com.br.artur.producer.config.RestTemplateConfig;
import com.br.artur.producer.convert.ProductConvert;
import com.br.artur.producer.creator.ProductCreator;
import com.br.artur.producer.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static com.br.artur.producer.service.ProductService.priceCalculator;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RabbitMqService rabbitMqService;

    @Mock
    private RestTemplateConfig config;

/*
    @Test
    void postTest() {
        var request = ProductCreator.fakerRequest();
        var productEntity = ProductConvert.toEntity(request);

        var savedProduct = productEntity;

        savedProduct.setQuantity(request.getQuantity() == null ? 0 : request.getQuantity());
        savedProduct.setBarCode(request.getBarCode().concat(String.valueOf(request.getQuantity())));

        savedProduct.setGrossAmount(request.getGrossAmount().setScale(2, RoundingMode.HALF_EVEN));
        savedProduct.setTaxes(request.getTaxes().setScale(2, RoundingMode.HALF_EVEN));
        savedProduct.setPrice(priceCalculator(request.getGrossAmount(),request.getTaxes()));

        Mockito.when(repository.save(productEntity)).thenReturn(savedProduct.withId(1L));
        var response = service.post(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void getAllTest() {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request);

        Mockito.when(repository.findAll()).thenReturn(List.of(productSave));
        var response = service.getAll();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.get(0).getCode(), request.getCode());
    }

    @Test
    void getByIdTest() {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(productSave));
        var response = service.getById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void getByCodeTest() {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(repository.findByCode("21h437s")).thenReturn(Optional.of(productSave));
        var response = service.getByCode("21h437s");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void deleteTest() {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(productSave));
        Mockito.doNothing().when(repository).deleteById(1L);
        var response = service.getById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void updateTest() {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(productSave));
        Mockito.when(repository.save(productSave)).thenReturn(productSave);

        var response = service.update(1L, request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
        Assertions.assertEquals(response.getName(), request.getName());
        Assertions.assertEquals(response.getDescription(), request.getDescription());
    }
*/
    @Test
    void patchQuantityTest() throws JsonProcessingException {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(restTemplate.getForObject(config.getUrl().concat("/code/{code}"), Product.class, productSave.getCode())).thenReturn(productSave);
        Mockito.doNothing().when(rabbitMqService).sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,ProductConvert.toDto(productSave),"PRODUCT_CHANGE");

        var stringResponse = service.patchQuantity(productSave.getCode(), 400);

        Assertions.assertNotNull(stringResponse);
        Assertions.assertEquals("Alteração no produto: \n'"+productSave+"'\n Enviada para a fila",stringResponse);
    }
}

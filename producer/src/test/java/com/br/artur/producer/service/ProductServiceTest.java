package com.br.artur.producer.service;

import com.br.artur.producer.config.RabbitMqConfig;
import com.br.artur.producer.config.RestTemplateConfig;
import com.br.artur.producer.convert.ProductConvert;
import com.br.artur.producer.creator.ProductCreator;
import com.br.artur.producer.dto.ProductDto;
import com.br.artur.producer.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.br.artur.producer.service.ProductService.priceCalculator;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RabbitMqService rabbitMqService;

    @Mock
    private RestTemplateConfig config;

    @Test
    void getAllTest() {
        var request = ProductConvert.toEntity(ProductCreator.fakerRequest());
        var productSavedList = Arrays.asList(request);
        var productListResponseEntity = ResponseEntity.of(Optional.of(productSavedList));

        Mockito.when(config.getUrl()).thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.exchange(config.getUrl(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {})).thenReturn(productListResponseEntity);

        var response = service.getAll();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.get(0).getCode(), request.getCode());
    }

    @Test
    void getByIdTest() {
        var request = ProductCreator.fakerRequest();
        var productSaved = ProductConvert.toDto(ProductConvert.toEntity(request).withId(1L));

        Mockito.when(config.getUrl())
                .thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.getForObject(config.getUrl().concat("/{id}"), ProductDto.class, 1L))
                .thenReturn(productSaved);

        var response = service.getById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void postTest() throws JsonProcessingException {
        var request = ProductCreator.fakerRequest();
        var productSaved = ProductConvert.toEntity(request);

        productSaved.setQuantity(request.getQuantity() == null ? 0 : request.getQuantity());
        productSaved.setBarCode(request.getBarCode().concat(String.valueOf(request.getQuantity())));

        productSaved.setGrossAmount(request.getGrossAmount().setScale(2, RoundingMode.HALF_EVEN));
        productSaved.setTaxes(request.getTaxes().setScale(2, RoundingMode.HALF_EVEN));
        productSaved.setPrice(priceCalculator(request.getGrossAmount(),request.getTaxes()));

        Mockito.doNothing().when(rabbitMqService).sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,ProductConvert.toDto(productSaved),"PRODUCT_POST");
        var response = service.post(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void deleteTest() throws JsonProcessingException {
        var request = ProductCreator.fakerRequest();
        var productSaved = ProductConvert.toDto(ProductConvert.toEntity(request).withId(1L));

        Mockito.when(config.getUrl())
                .thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.getForObject(config.getUrl().concat("/{id}"), ProductDto.class, 1L))
                .thenReturn(productSaved);
        Mockito.doNothing().when(rabbitMqService).sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,productSaved,"PRODUCT_DELETE");

        service.delete(1L);
        var response = service.getById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void updateTest() throws JsonProcessingException {
        var request = ProductCreator.fakerRequest();
        var requestToUpdate = ProductCreator.fakerRequest();
        var productSaved = ProductConvert.toDto(ProductConvert.toEntity(request).withId(1L));

        Mockito.when(config.getUrl())
                .thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.getForObject(config.getUrl().concat("/{id}"), ProductDto.class, 1L))
                .thenReturn(productSaved);
        Mockito.doNothing()
                .when(rabbitMqService).sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,ProductConvert.toDto(ProductConvert.toEntity(requestToUpdate)),"PRODUCT_UPDATE");

        var response = service.update(1L, requestToUpdate);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(requestToUpdate.getName(), response.getName());
    }

    @Test
    void patchQuantityTest() throws JsonProcessingException {
        var request = ProductCreator.fakerRequest();
        var productSaved = ProductConvert.toDto(ProductConvert.toEntity(request).withId(1L));

        Mockito.when(config.getUrl())
                .thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.getForObject(config.getUrl().concat("/code/{code}"), ProductDto.class, productSaved.getCode()))
                .thenReturn(productSaved);
        Mockito.doNothing()
                .when(rabbitMqService).sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,productSaved,"PRODUCT_CHANGE");

        var response = service.patchQuantity(productSaved.getCode(), 400);

        Assertions.assertNotNull(request);
        Assertions.assertEquals(request.getCode(), response.getCode());
    }
}

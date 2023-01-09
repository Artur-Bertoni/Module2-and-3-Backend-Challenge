package com.br.artur.produtoApi.service;

import com.br.artur.produtoApi.convert.ProductConvert;
import com.br.artur.produtoApi.creator.ProductCreator;
import com.br.artur.produtoApi.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static com.br.artur.produtoApi.service.ProductService.priceCalculator;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Test
    void insertTest() {
        var request = ProductCreator.fakerRequest();
        var productEntity = ProductConvert.toEntity(request);

        var produtoSalvo = productEntity;

        produtoSalvo.setQuantity(request.getQuantity() == null ? 0 : request.getQuantity());
        produtoSalvo.setBarCode(request.getBarCode().concat(String.valueOf(request.getQuantity())));

        produtoSalvo.setGrossAmount(request.getGrossAmount().setScale(2, RoundingMode.HALF_EVEN));
        produtoSalvo.setTaxes(request.getTaxes().setScale(2, RoundingMode.HALF_EVEN));
        produtoSalvo.setPrice(priceCalculator(request.getGrossAmount(),request.getTaxes()));

        Mockito.when(repository.save(productEntity)).thenReturn(produtoSalvo.withId(1L));
        var response = service.post(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void findAllTest() {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request);

        Mockito.when(repository.findAll()).thenReturn(List.of(productSave));
        var response = service.getAll();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.get(0).getCode(), request.getCode());
    }

    @Test
    void findByIdTest() {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(productSave));
        var response = service.getById(1L);

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

        var response = service.put(1L, request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
        Assertions.assertEquals(response.getName(), request.getName());
        Assertions.assertEquals(response.getDescription(), request.getDescription());
    }

    @Test
    void patchQuantityTest() {
        var request = ProductCreator.fakerRequest();
        var productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(productSave));
        Mockito.when(repository.save(productSave)).thenReturn(productSave);

        var stringResponse = service.patchQuantity(1L, 400);
        var response = repository.getById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
        Assertions.assertEquals(response.getQuantity(), request.getQuantity());
    }
}

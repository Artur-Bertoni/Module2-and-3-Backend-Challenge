package com.br.artur.desafio2.service;

import com.br.artur.desafio2.convert.ProductConvert;
import com.br.artur.desafio2.creator.ProductCreator;
import com.br.artur.desafio2.dto.RequestDTO;
import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Test
    void insertShouldReturnResponseEntityWhenSuccess() throws Exception{
        RequestDTO request = ProductCreator.createFakerRequest();
        Product productSave = ProductConvert.toEntity(request);

        Mockito.when(repository.save(productSave)).thenReturn(productSave);
        Product response = service.insert(ProductConvert.toEntity(request));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void findAllShouldReturnResponseEntityWhenSuccess() throws Exception{
        RequestDTO request = ProductCreator.createFakerRequest();
        Product productSave = ProductConvert.toEntity(request);

        Mockito.when(repository.findAll()).thenReturn(List.of(productSave));
        List<Product> response = service.findAll();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.get(0).getCode(), request.getCode());
    }

    @Test
    void findByIdShouldReturnResponseEntityWhenSuccess() throws Exception{
        RequestDTO request = ProductCreator.createFakerRequest();
        Product productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(productSave));
        Product response = service.findById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }

    @Test
    void deleteIdShouldReturnResponseEntityWhenSuccess() throws Exception{
        RequestDTO request = ProductCreator.createFakerRequest();
        Product productSave = ProductConvert.toEntity(request).withId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(productSave));
        Mockito.doNothing().when(repository).deleteById(1L);
        Product response = service.findById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCode(), request.getCode());
    }
}

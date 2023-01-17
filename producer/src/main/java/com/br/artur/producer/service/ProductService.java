package com.br.artur.producer.service;

import com.br.artur.producer.config.RabbitMqConfig;
import com.br.artur.producer.config.RestTemplateConfig;
import com.br.artur.producer.convert.ProductConvert;
import com.br.artur.producer.dto.ProductDto;
import com.br.artur.producer.dto.RequestDto;
import com.br.artur.producer.entity.Product;
import com.br.artur.producer.helper.CsvHelper;
import com.br.artur.producer.service.exceptions.ProductServiceException;
import com.br.artur.producer.service.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateConfig config;

    public List<ProductDto> getAll(){
        ResponseEntity<List<Product>> productList = restTemplate.exchange(config.getUrl(), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});

        return Objects.requireNonNull(productList.getBody()).stream().map(ProductConvert::toDto).collect(Collectors.toList());
    }

    public ProductDto getById(Long id){
        try{
            ResponseEntity<Product> product = restTemplate.exchange(config.getUrl().concat("/").concat(String.valueOf(id)), HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {});

            if (Objects.requireNonNull(product.getBody()).getId() == null){
                throw new ResourceNotFoundException("Produto não encontrado");
            }

            return ProductConvert.toDto(Objects.requireNonNull(product.getBody()));
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    public ProductDto post(RequestDto request){
        request.setCode(request.getCode() == null ? RandomStringUtils.randomAlphanumeric(8).toLowerCase() : request.getCode());

        request.setQuantity(request.getQuantity() == null ? 0 : request.getQuantity());
        request.setBarCode(request.getBarCode().concat(String.valueOf(request.getQuantity())));

        request.setGrossAmount(request.getGrossAmount().setScale(2, RoundingMode.HALF_EVEN));
        request.setTaxes(request.getTaxes().setScale(2, RoundingMode.HALF_EVEN));
        request.setPrice(priceCalculator(request.getGrossAmount(),request.getTaxes()));

        try{
            rabbitMqService.sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,ProductConvert.toDto(ProductConvert.toEntity(request)),"PRODUCT_POST");
        } catch (JsonProcessingException e) {
            throw new ProductServiceException(e.getMessage());
        }

        return ProductConvert.toDto(ProductConvert.toEntity(request));
    }

    public List<ProductDto> postByCsv(MultipartFile file) {
        try {
            List<Product> products = CsvHelper.toProductList(file.getInputStream());

            rabbitMqService.sendMessageList(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,products,"PRODUCT_POST_BY_CSV");

            return products.stream().map(ProductConvert::toDto).collect(Collectors.toList());
        } catch (IOException | NullPointerException e) {
            throw new ProductServiceException("Erro ao armazenar os dados do arquivo: "+e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            ProductDto productDto = getById(id);

            if (productDto == null){
                throw new ResourceNotFoundException("Produto não encontrado");
            }

            rabbitMqService.sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,productDto,"PRODUCT_DELETE");
        } catch (JsonProcessingException e) {
            throw new ProductServiceException(e.getMessage());
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    public ProductDto update(Long id, RequestDto request){
        try{
            ProductDto productDto = getById(id);

            if (productDto == null) {
                throw new ResourceNotFoundException("Produto não encontrado");
            }

            request.getGrossAmount().setScale(2, RoundingMode.HALF_EVEN);
            request.getTaxes().setScale(2, RoundingMode.HALF_EVEN);

            updateData(productDto, ProductConvert.toEntity(request));

            try{
                rabbitMqService.sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,productDto,"PRODUCT_UPDATE");
            } catch (JsonProcessingException e) {
                throw new ProductServiceException(e.getMessage());
            }

            return productDto;
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(ProductDto productDto, Product product) {
        productDto.setCategory(product.getCategory());
        productDto.setDescription(product.getDescription());
        productDto.setColor(product.getColor());
        productDto.setExpirationDate(product.getExpirationDate());
        productDto.setName(product.getName());
        productDto.setMaterial(product.getMaterial());
        productDto.setPrice(product.getPrice());
        productDto.setSeries(product.getSeries());
        productDto.setGrossAmount(product.getGrossAmount());
        productDto.setManufacturingDate(product.getManufacturingDate());
        productDto.setTaxes(product.getTaxes());
        productDto.setQuantity(product.getQuantity());

        productDto.setPrice(productDto.getPrice().setScale(2,RoundingMode.HALF_EVEN));
        productDto.setTaxes(productDto.getTaxes().setScale(2,RoundingMode.HALF_EVEN));
        productDto.setGrossAmount(productDto.getGrossAmount().setScale(2,RoundingMode.HALF_EVEN));
    }

    public ProductDto patchQuantity(String code, Integer quantity) {
        try{
            Product product = restTemplate.getForObject(config.getUrl().concat("/code/{code}"), Product.class, code);

            if (product == null) {
                throw new ResourceNotFoundException("Produto não encontrado");
            }

            product.setQuantity(quantity);

            rabbitMqService.sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,ProductConvert.toDto(product),"PRODUCT_CHANGE");

            return ProductConvert.toDto(product);
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(code);
        } catch (JsonProcessingException e) {
            throw new ProductServiceException(e.getMessage());
        }
    }

    public static BigDecimal priceCalculator(BigDecimal grossAmount, BigDecimal taxes){
        BigDecimal price = grossAmount.add((taxes.divide(BigDecimal.valueOf(100))).multiply(grossAmount));
        return price.add(price.multiply(BigDecimal.valueOf(0.45))).setScale(2, RoundingMode.HALF_UP);
    }
}
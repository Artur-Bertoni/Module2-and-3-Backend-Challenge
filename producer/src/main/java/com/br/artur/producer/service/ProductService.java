package com.br.artur.producer.service;

import com.br.artur.producer.config.RabbitMqConfig;
import com.br.artur.producer.config.RestTemplateConfig;
import com.br.artur.producer.convert.ProductConvert;
import com.br.artur.producer.dto.ProductDto;
import com.br.artur.producer.dto.RequestDto;
import com.br.artur.producer.entity.Product;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public String post(RequestDto request){
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

        return "POST do produto: \n'"+request+"'\n Enviada para a fila";
    }
/*
    public String postByCsv(MultipartFile file) {
        try {
            List<Product> products = CsvHelper.toProductList(file.getInputStream());
            return repository.saveAll(products).stream().map(ProductConvert::toDto).collect(Collectors.toList());
        } catch (IOException | NullPointerException e) {
            throw new ProductServiceException("Erro ao armazenar os dados do arquivo: "+e.getMessage());
        }
    }
*/
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
/*
    public String put(Long id, RequestDto request){
        try{
            Optional<Product> opt = repository.findById(id);

            if (opt.isEmpty()) {
                throw new ResourceNotFoundException("Produto não encontrado");
            }
            Product productEntity = opt.get();

            request.getGrossAmount().setScale(2, RoundingMode.HALF_EVEN);
            request.getTaxes().setScale(2, RoundingMode.HALF_EVEN);

            updateData(productEntity, ProductConvert.toEntity(request));
            return ProductConvert.toDto(this.repository.save(productEntity));
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Product productEntity, Product product) {
        productEntity.setCategory(product.getCategory());
        productEntity.setDescription(product.getDescription());
        productEntity.setColor(product.getColor());
        productEntity.setExpirationDate(product.getExpirationDate());
        productEntity.setName(product.getName());
        productEntity.setMaterial(product.getMaterial());
        productEntity.setPrice(product.getPrice());
        productEntity.setSeries(product.getSeries());
        productEntity.setGrossAmount(product.getGrossAmount());
        productEntity.setManufacturingDate(product.getManufacturingDate());
        productEntity.setTaxes(product.getTaxes());
        productEntity.setQuantity(product.getQuantity());
    }
*/
    public String patchQuantity(String code, Integer quantity) {
        try{
            Product product = restTemplate.getForObject(config.getUrl().concat("/code/{code}"), Product.class, code);

            if (product == null) {
                throw new ResourceNotFoundException("Produto não encontrado");
            }

            product.setQuantity(quantity);

            rabbitMqService.sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,ProductConvert.toDto(product),"PRODUCT_CHANGE");

            return "PATCH de quantidade no produto: \n'"+product+"'\n Enviada para a fila";
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
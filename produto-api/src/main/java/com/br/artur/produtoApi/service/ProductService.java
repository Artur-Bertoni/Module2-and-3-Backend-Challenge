package com.br.artur.produtoApi.service;

import com.br.artur.produtoApi.config.RabbitMqConfig;
import com.br.artur.produtoApi.convert.ProductConvert;
import com.br.artur.produtoApi.dto.ProductDto;
import com.br.artur.produtoApi.dto.RequestDto;
import com.br.artur.produtoApi.entity.Product;
import com.br.artur.produtoApi.helper.CsvHelper;
import com.br.artur.produtoApi.repository.ProductRepository;
import com.br.artur.produtoApi.service.exceptions.ProductServiceException;
import com.br.artur.produtoApi.service.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private RabbitMqService rabbitMqService;

    public List<ProductDto> getAll(){
        return repository.findAll().stream().map(ProductConvert::toDto).collect(Collectors.toList());
    }

    public ProductDto getById(Long id){
        return repository.findById(id).map(ProductConvert::toDto).orElse(ProductDto.builder().build());
    }

    public ProductDto getByCode(String code) {
        return repository.findByCode(code).map(ProductConvert::toDto).orElse(ProductDto.builder().build());
    }

    public ProductDto post(RequestDto request){
        request.setQuantity(request.getQuantity() == null ? 0 : request.getQuantity());
        request.setBarCode(request.getBarCode().concat(String.valueOf(request.getQuantity())));

        request.setGrossAmount(request.getGrossAmount().setScale(2, RoundingMode.HALF_EVEN));
        request.setTaxes(request.getTaxes().setScale(2, RoundingMode.HALF_EVEN));
        request.setPrice(priceCalculator(request.getGrossAmount(),request.getTaxes()));

        return ProductConvert.toDto(this.repository.save(ProductConvert.toEntity(request)));
    }

    public List<ProductDto> postByCsv(MultipartFile file) {
        try {
            List<Product> products = CsvHelper.toProductList(file.getInputStream());
            return repository.saveAll(products).stream().map(ProductConvert::toDto).collect(Collectors.toList());
        } catch (IOException | NullPointerException e) {
            throw new ProductServiceException("Erro ao armazenar os dados do arquivo: "+e.getMessage());
        }
    }

    public void delete(Long id) {
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(id);
        }
    }

    public ProductDto put(Long id, RequestDto request){
        try{
            Optional<Product> opt = repository.findById(id);

            if (opt.isEmpty()) {
                throw new EntityNotFoundException("Produto não encontrado");
            }
            Product productEntity = opt.get();

            request.getGrossAmount().setScale(2, RoundingMode.HALF_EVEN);
            request.getTaxes().setScale(2, RoundingMode.HALF_EVEN);

            updateData(productEntity, ProductConvert.toEntity(request));
            return ProductConvert.toDto(this.repository.save(productEntity));
        } catch (EntityNotFoundException e){
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

    public String patchQuantity(String code, Integer quantity) {
        try{
            Optional<Product> opt = repository.findByCode(code);

            if (opt.isEmpty()) {
                throw new EntityNotFoundException("Produto não encontrado");
            }
            Product productEntity = opt.get();
            productEntity.setQuantity(quantity);

            rabbitMqService.sendMessage(RabbitMqConfig.exchangeName,RabbitMqConfig.routingKey,ProductConvert.toDto(productEntity),"PRODUCT_CHANGE");

            return "Alteração no produto: \n'"+productEntity+"'\n Enviada para a fila";
        } catch (EntityNotFoundException e){
            e.printStackTrace();
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
package com.br.artur.desafio2.service;

import com.br.artur.desafio2.convert.ProductConvert;
import com.br.artur.desafio2.dto.ProductDto;
import com.br.artur.desafio2.dto.RequestDto;
import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.helper.CsvHelper;
import com.br.artur.desafio2.repository.ProductRepository;
import com.br.artur.desafio2.service.exceptions.ProductServiceException;
import com.br.artur.desafio2.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<ProductDto> findAll(){
        return repository.findAll().stream().map(ProductConvert::toDto).collect(Collectors.toList());
    }

    public ProductDto findById(Long id){
        return repository.findById(id).map(ProductConvert::toDto).orElse(ProductDto.builder().build());
    }

    public ProductDto insert(RequestDto obj){
        return ProductConvert.toDto(repository.save(ProductConvert.toEntity(obj)));
    }

    public List<ProductDto> insertByCsv(MultipartFile file) {
        try {
            List<Product> products = CsvHelper.toProductList(file.getInputStream());
            return ProductConvert.toDtoList(repository.saveAll(products));
        } catch (IOException e) {
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

    public ProductDto update(Long id, RequestDto obj){
        try{
            Product productEntity = repository.getById(id);
            updateData(productEntity, ProductConvert.toEntity(obj));
            return ProductConvert.toDto(repository.save(productEntity.withId(id)));
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Product productEntity, Product obj) {
        productEntity.setCategory(obj.getCategory());
        productEntity.setDescription(obj.getDescription());
        productEntity.setColor(obj.getColor());
        productEntity.setExpirationDate(obj.getExpirationDate());
        productEntity.setName(obj.getName());
        productEntity.setMaterial(obj.getMaterial());
        productEntity.setPrice(obj.getPrice());
        productEntity.setSeries(obj.getSeries());
        productEntity.setGrossAmount(obj.getGrossAmount());
        productEntity.setManufacturingDate(obj.getManufacturingDate());
        productEntity.setTaxes(obj.getTaxes());
        productEntity.setQuantity(obj.getQuantity());
    }
}
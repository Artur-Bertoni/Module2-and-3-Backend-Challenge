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
import java.util.Optional;
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

    public ProductDto insert(RequestDto request){
        return ProductConvert.toDto(repository.save(ProductConvert.toEntity(request)));
    }

    public List<ProductDto> insertByCsv(MultipartFile file) {
        try {
            List<Product> products = CsvHelper.toProductList(file.getInputStream());
            return ProductConvert.toDtoList(repository.saveAll(products));
        } catch (IOException e) {
            throw new ProductServiceException("Erro ao armazenar os dados do arquivo: "+e.getMessage());
        } catch (NullPointerException e){
            throw new ProductServiceException("Null pointer");
        }
    }

    public void delete(Long id) {
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(id);
        }
    }

    public ProductDto update(Long id, RequestDto request){
        try{
            Optional<Product> opt = repository.findById(id);

            if (!opt.isPresent()) {
                throw new EntityNotFoundException("Produto não encontrado");
            }
            Product productEntity = opt.get();

            updateData(productEntity, ProductConvert.toEntity(request));
            return ProductConvert.toDto(repository.save(productEntity));
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
}
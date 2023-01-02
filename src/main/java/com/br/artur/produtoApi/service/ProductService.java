package com.br.artur.produtoApi.service;

import com.br.artur.produtoApi.convert.ProductConvert;
import com.br.artur.produtoApi.dto.ProductDto;
import com.br.artur.produtoApi.dto.RequestDto;
import com.br.artur.produtoApi.entity.Product;
import com.br.artur.produtoApi.helper.CsvHelper;
import com.br.artur.produtoApi.repository.ProductRepository;
import com.br.artur.produtoApi.service.exceptions.ProductServiceException;
import com.br.artur.produtoApi.service.exceptions.ResourceNotFoundException;
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

    public List<ProductDto> getAll(){
        return repository.findAll().stream().map(ProductConvert::toDto).collect(Collectors.toList());
    }

    public ProductDto getById(Long id){
        return repository.findById(id).map(ProductConvert::toDto).orElse(ProductDto.builder().build());
    }

    public ProductDto post(RequestDto request){
        return ProductConvert.toDto(repository.save(ProductConvert.toEntity(request)));
    }

    public List<ProductDto> postByCsv(MultipartFile file) {
        try {
            List<Product> products = CsvHelper.toProductList(file.getInputStream());
            return repository.saveAll(products).stream().map(entity -> ProductConvert.toDto(entity)).collect(Collectors.toList());
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

    public ProductDto put(Long id, RequestDto request){
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
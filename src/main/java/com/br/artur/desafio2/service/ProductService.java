package com.br.artur.desafio2.service;

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

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> findAll(){
        return repository.findAll();
    }

    public Product findById(Long id){
        Optional<Product> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Product insert(Product obj){
        return repository.save(obj);
    }

    public List<Product> insertByCsv(MultipartFile file) {
        try {
            List<Product> products = CsvHelper.toProductList(file.getInputStream());
            return repository.saveAll(products);
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

    public Product update(Long id, Product obj){
        try{
            Product productEntity = repository.getById(id);
            updateData(productEntity, obj);
            return repository.save(productEntity);
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
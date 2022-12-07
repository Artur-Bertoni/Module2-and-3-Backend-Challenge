package com.br.artur.desafio2.resources;

import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.service.exceptions.ProductServiceException;
import com.br.artur.desafio2.service.ProductService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> findAll(){
        List<Product> productList = service.findAll();
        return ResponseEntity.ok().body(productList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id){
        Product p = service.findById(id);
        return ResponseEntity.ok().body(p);
    }

    @PostMapping
    public ResponseEntity<Product> insert(@RequestBody Product p) {
        p = service.insert(p);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(p.getId()).toUri();
        return ResponseEntity.created(uri).body(p);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product p){
        p = service.update(id, p);
        return ResponseEntity.ok().body(p);
    }

    @PostMapping
    public List<ResponseEntity<Product>> insertByCsv(String path) {
       List<Product> productList = service.insertByCsv(path);

       List<ResponseEntity<Product>> responseEntityList = null;
       for (Product p : productList){
           URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                   .buildAndExpand(p.getId()).toUri();

           responseEntityList.add(ResponseEntity.created(uri).body(p));
       }

       return responseEntityList;
    }
}

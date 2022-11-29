package com.br.artur.desafio2.resources;

import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

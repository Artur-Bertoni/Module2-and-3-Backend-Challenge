package com.br.artur.desafio2.resources;

import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.helper.CsvHelper;
import com.br.artur.desafio2.service.ProductService;
import com.br.artur.desafio2.service.exceptions.ProductServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<Product> insert(@RequestBody Product p) {
        p = service.insert(p);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(p.getId()).toUri();
        return ResponseEntity.created(uri).body(p);
    }

    @PostMapping("/upload")
    public ResponseEntity<List<Product>> insertByCsv(@RequestParam("file") MultipartFile file) {
        if (CsvHelper.isCsv(file)) {
            try {
                List<Product> pl = service.insertByCsv(file);

                return ResponseEntity.status(HttpStatus.CREATED).body(pl);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ProductServiceException("Não foi possível importar o arquivo: "+e.getMessage());
            }
        }
        throw new ProductServiceException("Por favor, insira um arquivo válido");
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
}

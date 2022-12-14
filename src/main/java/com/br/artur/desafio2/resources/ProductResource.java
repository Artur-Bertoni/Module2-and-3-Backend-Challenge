package com.br.artur.desafio2.resources;

import com.br.artur.desafio2.dto.ProductDto;
import com.br.artur.desafio2.dto.RequestDto;
import com.br.artur.desafio2.helper.CsvHelper;
import com.br.artur.desafio2.service.ProductService;
import com.br.artur.desafio2.service.exceptions.ProductServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping
    @ResponseBody
    public List<ProductDto> findAll(){
        return service.findAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ProductDto findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PostMapping
    @ResponseBody
    public ProductDto insert(@RequestBody RequestDto request) {
        return service.insert(request);
    }

    @PostMapping("/upload")
    @ResponseBody
    public List<ProductDto> insertByCsv(@RequestParam("file") MultipartFile file) {
        if (CsvHelper.isCsv(file)) {
            try {
                return service.insertByCsv(file);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ProductServiceException("Não foi possível importar o arquivo: "+e.getMessage());
            }
        }
        throw new ProductServiceException("Por favor, insira um arquivo válido");
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    public ProductDto update(@PathVariable Long id, @RequestBody RequestDto request){
        return service.update(id, request);
    }
}

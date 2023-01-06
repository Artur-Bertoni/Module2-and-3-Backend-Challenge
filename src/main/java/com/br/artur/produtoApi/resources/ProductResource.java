package com.br.artur.produtoApi.resources;

import com.br.artur.produtoApi.dto.ProductDto;
import com.br.artur.produtoApi.dto.RequestDto;
import com.br.artur.produtoApi.entity.Product;
import com.br.artur.produtoApi.helper.CsvHelper;
import com.br.artur.produtoApi.service.ProductService;
import com.br.artur.produtoApi.service.exceptions.ProductServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping
    @ResponseBody
    public List<ProductDto> getAll(){
        return this.service.getAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ProductDto getById(@PathVariable Long id){
        return this.service.getById(id);
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProductDto post(@RequestBody RequestDto request) {
        return this.service.post(request);
    }

    @PostMapping("/upload")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<ProductDto> postByCsv(@RequestParam("file") MultipartFile file) {
        if (CsvHelper.isCsv(file)) {
            try {
                return this.service.postByCsv(file);
            } catch (Exception e) {
                throw new ProductServiceException("Não foi possível importar o arquivo: "+e.getMessage());
            }
        }
        throw new ProductServiceException("Por favor, insira um arquivo válido");
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public ProductDto put(@PathVariable Long id, @RequestBody RequestDto request){
        return this.service.put(id, request);
    }

    @PatchMapping(value = "/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public String patchQuantity(@PathVariable Long id, @RequestParam("quantity") Integer quantity){
        return this.service.patchQuantity(id, quantity);
    }
}

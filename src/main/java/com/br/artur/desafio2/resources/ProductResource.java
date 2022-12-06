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
    public ResponseEntity<Product> insert(@RequestBody Product pdct) {
        pdct = service.insert(pdct);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pdct.getId()).toUri();
        return ResponseEntity.created(uri).body(pdct);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    public List<ResponseEntity<Product>> insertByCsv(String path) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            CSVReader csvReader = new CSVReader(new FileReader(path));

            List<ResponseEntity<Product>> insertsList = new ArrayList<>();
            List<Map<String, String>> lines = new ArrayList<>();

            String[] headers = csvReader.readNext(),
                    columns;

            for (int i = 0; i < headers.length; i++) {
                headers[i] = headers[i].toLowerCase();
            }

            while (true) {
                try {
                    if ((columns = csvReader.readNext()) == null) break;
                } catch (IOException | CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                Map<String, String> campos = new HashMap<>();

                for (int i = 0; i < columns.length; i++) {
                    campos.put(headers[i], columns[i]);
                }

                lines.add(campos);
            }

            lines.forEach(cols -> {
                String code = cols.get("código");
                Long barCode = cols.get("codigo de barras").equals("null") ? null : Long.parseLong(cols.get("codigo de barras"));
                String series = cols.get("série");
                String name = cols.get("nome");
                String description = cols.get("descrição");
                String category = cols.get("categoria");
                BigDecimal taxes = cols.get("impostos (%)").equals("null") ? null : new BigDecimal(cols.get("impostos (%)").replace(',', '.'));
                BigDecimal grossAmount = cols.get("valor bruto").equals("null") ? null : new BigDecimal(cols.get("valor bruto").replace(',', '.'));
                BigDecimal price = null;
                if (taxes != null && grossAmount != null) {
                    price = grossAmount.add((taxes.divide(BigDecimal.valueOf(100))).multiply(grossAmount));
                    price = price.add(price.multiply(BigDecimal.valueOf(0.45)));
                }
                Date manufacturingDate = null, expirationDate = null;
                Integer quantity = (cols.get("quantidade") == null || cols.get("quantidade").equals("null")) ? null : Integer.parseInt(cols.get("quantidade"));

                try {
                    if (!cols.get("data de fabricação").equals("n/a") && !cols.get("data de fabricação").equals("") && !cols.get("data de fabricação").equals("null")) {
                        manufacturingDate = sdf.parse(cols.get("data de fabricação"));
                    }
                    if (!cols.get("data de validade").equals("n/a") && !cols.get("data de validade").equals("") && !cols.get("data de validade").equals("null")) {
                        expirationDate = sdf.parse(cols.get("data de validade"));
                    }
                } catch (ParseException e) {
                    throw new ProductServiceException(e.getMessage());
                }

                String color = cols.get("cor");
                String material = cols.get("material");

                Product product = new Product();

                product.setCode(code);
                product.setBarCode(barCode);
                product.setSeries(series);
                product.setName(name);
                product.setDescription(description);
                product.setCategory(category);
                product.setGrossAmount(grossAmount);
                product.setTaxes(taxes);
                product.setPrice(price);
                assert manufacturingDate != null;
                product.setManufacturingDate(manufacturingDate.toInstant());
                assert expirationDate != null;
                product.setExpirationDate(expirationDate.toInstant());
                product.setColor(color);
                product.setMaterial(material);
                product.setQuantity(quantity);

                insertsList.add(insert(product));
            });

            return insertsList;
        } catch (Exception e) {
            throw new ProductServiceException(e.getMessage());
        }
    }
}

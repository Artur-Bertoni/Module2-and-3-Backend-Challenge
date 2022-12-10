package com.br.artur.desafio2.service;

import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.repository.ProductRepository;
import com.br.artur.desafio2.service.exceptions.ProductServiceException;
import com.br.artur.desafio2.service.exceptions.ResourceNotFoundException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public List<Product> insertByCsv(String path) {
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new ProductServiceException(e.getMessage());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        List<Product> insertsList = new ArrayList<>();
        List<Map<String, String>> lines = new ArrayList<>();

        String[] headers,
                 columns;
        try {
            headers = csvReader.readNext();
        } catch (IOException | CsvValidationException e) {
            throw new ProductServiceException(e.getMessage());
        }

        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].toLowerCase();
        }

        while (true) {
            try {
                if ((columns = csvReader.readNext()) == null) break;
            } catch (IOException | CsvValidationException e) {
                throw new RuntimeException(e);
            }
            Map<String, String> fields = new HashMap<>();

            for (int i = 0; i < columns.length; i++) {
                fields.put(headers[i], columns[i]);
            }

            lines.add(fields);
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

            insertsList.add(product);

        });

        return repository.saveAll(insertsList);
    }
/*
    public String insertByCsv(MultipartFile file, RedirectAttributes redirectAttributes, String folderPath) {

        if (file.isEmpty()){
            redirectAttributes.addFlashAttribute("mensagem","Por favor, selecione um arquivo para inserir");
            return "redirect:uploadStatus";
        }

        try{
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderPath + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("mensagem", "Arquivo '"+file.getOriginalFilename()+"' importado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
        /*
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new ProductServiceException(e.getMessage());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        List<Product> insertsList = new ArrayList<>();
        List<Map<String, String>> lines = new ArrayList<>();

        String[] headers,
                 columns;
        try {
            headers = csvReader.readNext();
        } catch (IOException | CsvValidationException e) {
            throw new ProductServiceException(e.getMessage());
        }

        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].toLowerCase();
        }

        while (true) {
            try {
                if ((columns = csvReader.readNext()) == null) break;
            } catch (IOException | CsvValidationException e) {
                throw new RuntimeException(e);
            }
            Map<String, String> fields = new HashMap<>();

            for (int i = 0; i < columns.length; i++) {
                fields.put(headers[i], columns[i]);
            }

            lines.add(fields);
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

            insertsList.add(product);

        });

        return repository.saveAll(insertsList);
    }*/

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
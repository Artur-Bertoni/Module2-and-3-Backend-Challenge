package com.br.artur.desafio2.helper;

import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.service.exceptions.ProductServiceException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsvHelper {

    public static String TYPE = "text/csv";

    public static boolean isCsv(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Product> toProductList (InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Product> products = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {

                String code = csvRecord.get("código");
                String series = csvRecord.get("série");
                String name = csvRecord.get("nome");
                String description = csvRecord.get("descrição");
                String category = csvRecord.get("categoria");
                String color = csvRecord.get("cor");
                String material = csvRecord.get("material");
                Long barCode = csvRecord.get("codigo de barras").equals("null") ? null : Long.parseLong(csvRecord.get("codigo de barras"));
                BigDecimal taxes = csvRecord.get("impostos (%)").equals("null") ? null : new BigDecimal(csvRecord.get("impostos (%)").replace(',', '.'));
                BigDecimal grossAmount = csvRecord.get("valor bruto").equals("null") ? null : new BigDecimal(csvRecord.get("valor bruto").replace(',', '.'));
                Integer quantity = 0;

                BigDecimal price = null;
                if (taxes != null && grossAmount != null) {
                    price = grossAmount.add((taxes.divide(BigDecimal.valueOf(100))).multiply(grossAmount));
                    price = price.add(price.multiply(BigDecimal.valueOf(0.45)));
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date manufacturingDate = null, expirationDate = null;
                if (!csvRecord.get("data de fabricação").equals("n/a") && !csvRecord.get("data de fabricação").equals("") && !csvRecord.get("data de fabricação").equals("null")) {
                    manufacturingDate = sdf.parse(csvRecord.get("data de fabricação"));
                }
                if (!csvRecord.get("data de validade").equals("n/a") && !csvRecord.get("data de validade").equals("") && !csvRecord.get("data de validade").equals("null")) {
                    expirationDate = sdf.parse(csvRecord.get("data de validade"));
                }

                if (expirationDate != null && manufacturingDate != null) {
                    Product product = new Product(null,code,category,series,description,color,material,name,grossAmount,taxes,price,quantity,barCode,manufacturingDate.toInstant(),expirationDate.toInstant());

                    products.add(product);
                } else{
                    Product product = new Product(null,code,category,series,description,color,material,name,grossAmount,taxes,price,quantity,barCode,null,null);

                    products.add(product);
                }
            }

            return products;
        } catch (IOException e) {
            throw new ProductServiceException("Erro ao ler o arquivo CSV: "+e.getMessage());
        } catch (ParseException e) {
            throw new ProductServiceException(e.getMessage());
        } catch (NullPointerException e){
            e.printStackTrace();
            throw new ProductServiceException(e.getMessage());
        }
    }
}

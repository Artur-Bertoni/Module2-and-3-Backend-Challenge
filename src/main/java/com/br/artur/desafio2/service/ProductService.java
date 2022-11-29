package com.br.artur.desafio2.service;

import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return obj.get();
    }
    /*
    public void addProductByImport(String path) {
        Product p = new Product();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            CSVReader csvReader = new CSVReader(new FileReader(path));

            List<Map<String,String>> lines = new ArrayList<>();

            String[] headers = csvReader.readNext(),
                    columns;

            for (int i=0; i < headers.length; i++){
                headers[i] = headers[i].toLowerCase();
            }

            while (true){
                try {
                    if ((columns = csvReader.readNext()) == null) break;
                } catch (IOException | CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                Map<String,String> campos = new HashMap<>();

                for (int i=0; i< columns.length; i++){
                    campos.put(headers[i], columns[i]);
                }

                lines.add(campos);
            }

            final int[] cont = {0};

            lines.forEach(cols -> {
                String code = cols.get("código");
                Long barCode = cols.get("codigo de barras").equals("null") ? null : Long.parseLong(cols.get("codigo de barras"));
                String series = cols.get("série");
                String name = cols.get("nome");
                String description = cols.get("descrição");
                String category = cols.get("categoria");
                BigDecimal taxes = cols.get("impostos (%)").equals("null") ? null : new BigDecimal(cols.get("impostos (%)").replace(',','.'));
                BigDecimal grossAmount = cols.get("valor bruto").equals("null") ? null : new BigDecimal(cols.get("valor bruto").replace(',','.'));
                BigDecimal price = null;
                if (taxes != null && grossAmount != null){
                    price = grossAmount.add((taxes.divide(BigDecimal.valueOf(100))).multiply(grossAmount));
                    price = price.add(price.multiply(BigDecimal.valueOf(0.45)));
                }
                Date manufacturingDate = null, expirationDate = null;
                Integer quantity = (cols.get("quantidade") == null || cols.get("quantidade").equals("null")) ? null : Integer.parseInt(cols.get("quantidade"));

                try {
                    if (!cols.get("data de fabricação").equals("n/a") && !cols.get("data de fabricação").equals("") && !cols.get("data de fabricação").equals("null")){
                        manufacturingDate = sdf.parse(cols.get("data de fabricação"));
                    }
                    if (!cols.get("data de validade").equals("n/a") && !cols.get("data de validade").equals("") && !cols.get("data de validade").equals("null")){
                        expirationDate = sdf.parse(cols.get("data de validade"));
                    }
                } catch (ParseException e) {
                    throw new ProductServiceException(e.getMessage());
                }

                String color = cols.get("cor");
                String material = cols.get("material");

                p.setCode(code);
                p.setBarCode(barCode);
                p.setSeries(series);
                p.setName(name);
                p.setDescription(description);
                p.setCategory(category);
                p.setGrossAmount(grossAmount);
                p.setTaxes(taxes);
                p.setPrice(price);
                p.setManufacturingDate(manufacturingDate);
                p.setExpirationDate(expirationDate);
                p.setColor(color);
                p.setMaterial(material);
                p.setQuantity(quantity);

                p.products.add(p);
                cont[0]++;
            });
        } catch(Exception e){
            throw new ProductServiceException(e.getMessage());
        }
    }
 */
}
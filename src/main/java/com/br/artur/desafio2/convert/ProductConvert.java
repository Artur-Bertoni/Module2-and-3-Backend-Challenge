package com.br.artur.desafio2.convert;

import com.br.artur.desafio2.dto.ProductDTO;
import com.br.artur.desafio2.entity.Product;

public class ProductConvert {

    public ProductDTO toBuildDTO(Product product){
        return ProductDTO.builder()
                .id(Integer.valueOf(product.getCode()))
                .build();
    }
}

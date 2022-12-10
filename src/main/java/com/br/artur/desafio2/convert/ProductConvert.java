package com.br.artur.desafio2.convert;

import com.br.artur.desafio2.dto.ProductDTO;
import com.br.artur.desafio2.dto.RequestDTO;
import com.br.artur.desafio2.entity.Product;

public class ProductConvert {

    public static Product toEntity(RequestDTO productRequest){
        return Product.builder()
                .barCode(productRequest.getBarCode())
                .color(productRequest.getColor())
                .code(productRequest.getCode())
                .description(productRequest.getDescription())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .expirationDate(productRequest.getExpirationDate())
                .grossAmount(productRequest.getGrossAmount())
                .taxes(productRequest.getTaxes())
                .series(productRequest.getSeries())
                .material(productRequest.getMaterial())
                .quantity(productRequest.getQuantity())
                .manufacturingDate(productRequest.getManufacturingDate())
                .category(productRequest.getCategory())
                .build();
    }

    public static ProductDTO toDTO(Product p){
        return ProductDTO.builder()
                .barCode(p.getBarCode())
                .color(p.getColor())
                .code(p.getCode())
                .description(p.getDescription())
                .name(p.getName())
                .price(p.getPrice())
                .expirationDate(p.getExpirationDate())
                .grossAmount(p.getGrossAmount())
                .taxes(p.getTaxes())
                .series(p.getSeries())
                .material(p.getMaterial())
                .quantity(p.getQuantity())
                .manufacturingDate(p.getManufacturingDate())
                .category(p.getCategory())
                .build();
    }
}

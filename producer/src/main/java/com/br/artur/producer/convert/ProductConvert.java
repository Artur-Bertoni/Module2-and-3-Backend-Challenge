package com.br.artur.producer.convert;

import com.br.artur.producer.dto.ProductDto;
import com.br.artur.producer.dto.RequestDto;
import com.br.artur.producer.entity.Product;

public class ProductConvert {

    public static Product toEntity(RequestDto productRequest){
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

    public static ProductDto toDto(Product p){
        return ProductDto.builder()
                .id(p.getId())
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

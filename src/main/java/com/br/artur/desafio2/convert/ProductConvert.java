package com.br.artur.desafio2.convert;

import com.br.artur.desafio2.dto.ProductDto;
import com.br.artur.desafio2.dto.RequestDto;
import com.br.artur.desafio2.entity.Product;

import java.util.ArrayList;
import java.util.List;

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

    public static List<ProductDto> toDtoList(List<Product> pl){
        List<ProductDto> dtoList = new ArrayList<>();

        for (Product p : pl){
            dtoList.add(ProductDto.builder()
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
                    .build());
        }
        return dtoList;
    }
}

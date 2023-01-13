package com.br.artur.produtoApi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@With
public class ProductDto {

    private Long id;
    private String code, category, series, description, color, material, name, barCode;
    private BigDecimal grossAmount, taxes, price;
    private Integer quantity;
    private Instant manufacturingDate, expirationDate;
}

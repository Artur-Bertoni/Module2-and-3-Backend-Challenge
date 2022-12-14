package com.br.artur.desafio2.dto;

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
    private Long barCode;
    private String code, category, series, description, color, material, name;
    private BigDecimal grossAmount, taxes, price;
    private Integer quantity;
    private Instant manufacturingDate, expirationDate;

}

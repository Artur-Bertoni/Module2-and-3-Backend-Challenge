package com.br.artur.producer.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String code, category, series, description, color, material, name, barCode;
    private BigDecimal grossAmount, taxes, price;
    private Integer quantity;
    private Instant manufacturingDate, expirationDate;
}

package com.br.artur.produtoApi.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code, category, series, description, color, material, name;
    private BigDecimal grossAmount, taxes, price;
    private Integer quantity;
    private Long barCode;
    private Instant manufacturingDate, expirationDate;

    public Product(String code, String category, String series, String description, String color, String material, String name, BigDecimal grossAmount, BigDecimal taxes, BigDecimal price, Integer quantity, Long barCode, Instant manufacturingDate, Instant expirationDate) {
        this.code = code;
        this.category = category;
        this.series = series;
        this.description = description;
        this.color = color;
        this.material = material;
        this.name = name;
        this.grossAmount = grossAmount;
        this.taxes = taxes;
        this.price = price;
        this.quantity = quantity;
        this.barCode = barCode;
        this.manufacturingDate = manufacturingDate;
        this.expirationDate = expirationDate;
    }
}

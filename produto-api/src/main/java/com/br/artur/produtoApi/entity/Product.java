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
    private String code, category, series, description, color, material, name, barCode;
    private BigDecimal grossAmount, taxes, price;
    private Integer quantity;
    private Instant manufacturingDate, expirationDate;
}

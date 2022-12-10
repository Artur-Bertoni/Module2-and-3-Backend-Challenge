package com.br.artur.desafio2.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Data
@Builder
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
}

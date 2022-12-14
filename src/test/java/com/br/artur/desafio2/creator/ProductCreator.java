package com.br.artur.desafio2.creator;

import com.br.artur.desafio2.dto.RequestDto;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductCreator {

    public static RequestDto createRequest(){
        return RequestDto.builder()
                .barCode(123456789L)
                .color("Vermelha")
                .code(RandomStringUtils.randomAlphanumeric(8).toLowerCase())
                .description("Fruta")
                .name("Maçã")
                .price(new BigDecimal(12))
                .expirationDate(Instant.now())
                .grossAmount(new BigDecimal(11))
                .taxes(new BigDecimal(10))
                .series("1/2022")
                .material("n/a")
                .quantity(150)
                .manufacturingDate(Instant.now())
                .category("Comida")
                .build();
    }

    public static RequestDto createFakerRequest() {
        Faker faker = new Faker();

        return RequestDto.builder()
                .barCode(faker.number().randomNumber())
                .color(faker.commerce().color())
                .code(RandomStringUtils.randomAlphanumeric(8).toLowerCase())
                .description(faker.food().ingredient())
                .name(faker.commerce().productName())
                .price(new BigDecimal(faker.commerce().price().replace(",", ".")))
                .expirationDate(Instant.now())
                .grossAmount(new BigDecimal(faker.commerce().price().replace(",", ".")))
                .taxes(new BigDecimal(faker.number().randomDigitNotZero()))
                .series(faker.number().randomDigitNotZero()+"/2022")
                .material(faker.commerce().material())
                .quantity(faker.number().randomDigitNotZero())
                .manufacturingDate(Instant.now())
                .category(faker.commerce().department())
                .build();
    }

    public static RequestDto updateRequest(){
        return RequestDto.builder()
                .color("Azul")
                .description("Mesa grande")
                .name("Mesa")
                .price(new BigDecimal(1))
                .expirationDate(Instant.now())
                .grossAmount(new BigDecimal(2))
                .taxes(new BigDecimal(3))
                .series("2/2022")
                .material("madeira")
                .quantity(20)
                .manufacturingDate(Instant.now())
                .category("Móveis")
                .build();
    }
}

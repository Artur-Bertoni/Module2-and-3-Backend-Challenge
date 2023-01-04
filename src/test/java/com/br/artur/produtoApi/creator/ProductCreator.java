package com.br.artur.produtoApi.creator;

import com.br.artur.produtoApi.dto.RequestDto;
import com.br.artur.produtoApi.entity.Product;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductCreator {

    public static RequestDto createRequest(){
        return RequestDto.builder()
                .barCode("123456789")
                .color("Vermelha")
                .code(RandomStringUtils.randomAlphanumeric(8).toLowerCase())
                .description("Fruta")
                .name("Maçã")
                .expirationDate(Instant.now())
                .grossAmount(new BigDecimal(11))
                .taxes(new BigDecimal(10))
                .series("1/2022")
                .material("n/a")
                .manufacturingDate(Instant.now())
                .category("Comida")
                .build();
    }

    public static RequestDto fakerRequest() {
        Faker faker = new Faker();

        BigDecimal grossAmount = new BigDecimal(faker.commerce().price().replace(",", ".")),
                   taxes = new BigDecimal(faker.number().randomDigitNotZero()),
                   price = new BigDecimal(0);

        return RequestDto.builder()
                .barCode(String.valueOf(faker.number().randomNumber()))
                .color(faker.commerce().color())
                .code(RandomStringUtils.randomAlphanumeric(8).toLowerCase())
                .description(faker.food().ingredient())
                .name(faker.commerce().productName())
                .price(Product.priceCalculator(grossAmount,taxes))
                .expirationDate(Instant.now())
                .grossAmount(grossAmount)
                .taxes(taxes)
                .series(faker.number().randomDigitNotZero()+"/2022")
                .material(faker.commerce().material())
                .quantity(faker.number().randomDigitNotZero())
                .manufacturingDate(Instant.now())
                .category(faker.commerce().department())
                .build();
    }
}

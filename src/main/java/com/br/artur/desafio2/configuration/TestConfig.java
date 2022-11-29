package com.br.artur.desafio2.configuration;

import com.br.artur.desafio2.entity.Product;
import com.br.artur.desafio2.repository.ProductRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Product p1 = new Product(null, null,"FOOD",
                "2022/1","Fruta","Vermelha","n/a","Maçã", null,
                null,null,null,null, Instant.parse("2019-06-20T19:53:07Z"),
                Instant.parse("2019-06-20T19:53:07Z"));
        Product p2 = new Product(null, null,"FOOD",
                "2022/1","Fruta","Amarela","n/a","Banana", null,
                null, null,null,null, Instant.parse("2019-06-20T19:53:07Z"),
                Instant.parse("2019-06-20T19:53:07Z"));

        productRepository.saveAll(Arrays.asList(p1, p2));
    }
}

package com.br.artur.desafio2.repository;

import com.br.artur.desafio2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
package com.br.artur.desafio2.exception;

public class ProductServiceException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ProductServiceException(String message) {
        super(message);
    }
}
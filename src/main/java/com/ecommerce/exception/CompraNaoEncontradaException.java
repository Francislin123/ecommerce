package com.ecommerce.exception;

public class CompraNaoEncontradaException extends RuntimeException {
    public CompraNaoEncontradaException(int ano) {
        super("Nenhuma compra encontrada para o ano " + ano);
    }
}

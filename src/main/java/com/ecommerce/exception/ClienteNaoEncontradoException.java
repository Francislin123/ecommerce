package com.ecommerce.exception;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(String cpf) {
        super("Cliente não encontrado para o CPF: " + cpf);
    }
}

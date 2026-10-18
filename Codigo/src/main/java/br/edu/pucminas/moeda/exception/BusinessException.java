package br.edu.pucminas.moeda.exception;

/** Lancada quando uma regra de negocio impede a operacao (ex.: CNPJ duplicado). */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}

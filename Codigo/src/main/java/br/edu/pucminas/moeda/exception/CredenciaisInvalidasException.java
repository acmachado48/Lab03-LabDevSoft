package br.edu.pucminas.moeda.exception;

/** Login ou senha incorretos. */
public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException(String message) {
        super(message);
    }
}

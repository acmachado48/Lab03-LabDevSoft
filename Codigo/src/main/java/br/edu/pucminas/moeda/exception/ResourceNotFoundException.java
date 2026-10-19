package br.edu.pucminas.moeda.exception;

/** Lancada quando um recurso solicitado (ex.: empresa por id) nao existe. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

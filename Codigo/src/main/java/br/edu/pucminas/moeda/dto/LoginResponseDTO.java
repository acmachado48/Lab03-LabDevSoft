package br.edu.pucminas.moeda.dto;

/** Usuario autenticado: o front-end usa o tipo para decidir qual painel abrir. */
public record LoginResponseDTO(Long id, String nome, String tipo) {
}

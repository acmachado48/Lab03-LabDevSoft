package br.edu.pucminas.moeda.dto;

/**
 * Usuario autenticado: o front-end usa o tipo para decidir qual painel abrir
 * e envia o token no cabecalho "Authorization: Bearer <token>" das proximas requisicoes.
 */
public record LoginResponseDTO(Long id, String nome, String tipo, String token) {
}

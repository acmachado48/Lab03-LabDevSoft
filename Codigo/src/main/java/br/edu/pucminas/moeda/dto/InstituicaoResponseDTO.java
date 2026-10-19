package br.edu.pucminas.moeda.dto;

import br.edu.pucminas.moeda.model.Instituicao;

public record InstituicaoResponseDTO(Long id, String nome) {
    public static InstituicaoResponseDTO fromEntity(Instituicao instituicao) {
        return new InstituicaoResponseDTO(instituicao.getId(), instituicao.getNome());
    }
}
package br.edu.pucminas.moeda.dto;

import br.edu.pucminas.moeda.model.Aluno;

public record AlunoResponseDTO(Long id, String nome, String curso, Integer saldoMoedas) {
    public static AlunoResponseDTO fromEntity(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getCurso(),
                aluno.getSaldoMoedas()
        );
    }
}
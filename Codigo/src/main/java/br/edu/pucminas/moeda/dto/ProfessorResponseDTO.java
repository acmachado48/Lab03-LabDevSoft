package br.edu.pucminas.moeda.dto;

import br.edu.pucminas.moeda.model.Professor;

public record ProfessorResponseDTO(Long id, String nome, String departamento, Integer saldoMoedas) {
    public static ProfessorResponseDTO fromEntity(Professor professor) {
        return new ProfessorResponseDTO(
                professor.getId(),
                professor.getNome(),
                professor.getDepartamento(),
                professor.getSaldoMoedas()
        );
    }
}
package br.edu.pucminas.moeda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransacaoRequestDTO(
        @NotNull(message = "O ID do professor é obrigatório") Long professorId,
        @NotNull(message = "O ID do aluno é obrigatório") Long alunoId,
        @NotNull(message = "O valor é obrigatório") @Positive(message = "O valor deve ser positivo") Integer valor,
        @NotBlank(message = "O motivo é obrigatório") String motivo
) {}
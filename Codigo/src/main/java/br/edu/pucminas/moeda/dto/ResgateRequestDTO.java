package br.edu.pucminas.moeda.dto;

import jakarta.validation.constraints.NotNull;

public record ResgateRequestDTO(
        @NotNull(message = "O ID do aluno é obrigatório")
        Long alunoId,

        @NotNull(message = "O ID da vantagem é obrigatório")
        Long vantagemId
) {}
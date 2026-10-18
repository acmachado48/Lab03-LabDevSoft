package br.edu.pucminas.moeda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VantagemRequestDTO(
        @NotBlank(message = "A descrição é obrigatória")
        String descricao,
        
        @NotNull(message = "O custo em moedas é obrigatório")
        @Positive(message = "O custo em moedas deve ser maior que zero")
        Integer custoEmMoedas,
        
        @NotBlank(message = "A foto é obrigatória")
        String foto,
        
        @NotNull(message = "O ID da empresa parceira é obrigatório")
        Long empresaId
) {
}
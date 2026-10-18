package br.edu.pucminas.moeda.dto;

import br.edu.pucminas.moeda.model.Vantagem;

public record VantagemResponseDTO(
        Long id,
        String descricao,
        Integer custoEmMoedas,
        String foto,
        Long empresaId,
        String nomeEmpresa
) {
    public static VantagemResponseDTO fromEntity(Vantagem vantagem) {
        return new VantagemResponseDTO(
                vantagem.getId(),
                vantagem.getDescricao(),
                vantagem.getCustoEmMoedas(),
                vantagem.getFoto(),
                vantagem.getEmpresa().getId(),
                vantagem.getEmpresa().getNome()
        );
    }
}
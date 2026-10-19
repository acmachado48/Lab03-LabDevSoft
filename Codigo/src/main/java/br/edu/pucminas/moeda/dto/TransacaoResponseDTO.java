package br.edu.pucminas.moeda.dto;

import br.edu.pucminas.moeda.model.Transacao;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(
        Long id, 
        LocalDateTime dataTransacao, 
        String descricao, 
        String tipo, // "ENTRADA" ou "SAIDA"
        Integer valor
) {
    public static TransacaoResponseDTO fromEntity(Transacao transacao) {
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getDataTransacao(),
                transacao.getMotivo(),
                "ENTRADA",
                transacao.getValor()
        );
    }
}
package br.edu.pucminas.moeda.dto;

import br.edu.pucminas.moeda.model.Transacao;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(Long id, LocalDateTime dataTransacao, String nomeAluno, String nomeProfessor,
                                   String motivo, Integer valor) {
    public static TransacaoResponseDTO fromEntity(Transacao transacao) {
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getDataTransacao(),
                transacao.getAluno().getNome(),
                transacao.getProfessor().getNome(),
                transacao.getMotivo(),
                transacao.getValor()
        );
    }
}

package br.edu.pucminas.moeda.dto;

import br.edu.pucminas.moeda.model.Resgate;
import java.time.LocalDateTime;

public record ResgateResponseDTO(Long id, LocalDateTime dataResgate, String codigoCupom,
                                 String descricaoVantagem, String nomeEmpresa, Integer valor) {
    public static ResgateResponseDTO fromEntity(Resgate resgate) {
        return new ResgateResponseDTO(
                resgate.getId(),
                resgate.getDataResgate(),
                resgate.getCodigoCupom(),
                resgate.getVantagem().getDescricao(),
                resgate.getVantagem().getEmpresa().getNome(),
                resgate.getValor()
        );
    }
}

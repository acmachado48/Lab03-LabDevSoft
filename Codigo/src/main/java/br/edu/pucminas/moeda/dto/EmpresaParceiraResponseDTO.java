package br.edu.pucminas.moeda.dto;

import br.edu.pucminas.moeda.model.EmpresaParceira;

/**
 * Dados devolvidos ao cliente. Nunca inclui a senha.
 */
public record EmpresaParceiraResponseDTO(
        Long id,
        String nome,
        String cnpj,
        String telefone,
        String email,
        String login
) {
    public static EmpresaParceiraResponseDTO fromEntity(EmpresaParceira empresa) {
        return new EmpresaParceiraResponseDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getTelefone(),
                empresa.getEmail(),
                empresa.getLogin()
        );
    }
}

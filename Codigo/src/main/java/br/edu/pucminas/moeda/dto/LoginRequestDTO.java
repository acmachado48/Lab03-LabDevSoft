package br.edu.pucminas.moeda.dto;

import jakarta.validation.constraints.NotBlank;

/** Credenciais informadas na tela inicial. O campo login aceita o login ou o e-mail do usuario. */
public record LoginRequestDTO(

        @NotBlank(message = "Informe o login ou e-mail")
        String login,

        @NotBlank(message = "Informe a senha")
        String senha
) {
}

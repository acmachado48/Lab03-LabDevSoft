package br.edu.pucminas.moeda.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Dados para atualizar uma empresa parceira existente. A senha e opcional:
 * se vier em branco ou nula, a senha atual e mantida.
 */
public record EmpresaParceiraUpdateDTO(

        @NotBlank(message = "A razao social e obrigatoria")
        String nome,

        @NotBlank(message = "O CNPJ e obrigatorio")
        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter 14 digitos numericos, sem pontuacao")
        String cnpj,

        String telefone,

        @NotBlank(message = "O e-mail e obrigatorio")
        @Email(message = "Informe um e-mail valido")
        String email,

        @NotBlank(message = "O login e obrigatorio")
        @Size(min = 3, max = 50, message = "O login deve ter entre 3 e 50 caracteres")
        String login,

        @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres")
        String senha
) {
}

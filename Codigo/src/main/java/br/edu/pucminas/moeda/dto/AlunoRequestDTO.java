package br.edu.pucminas.moeda.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Dados recebidos para o auto-cadastro de um aluno. */
public record AlunoRequestDTO(

        @NotBlank(message = "O nome e obrigatorio")
        String nome,

        @NotBlank(message = "O CPF e obrigatorio")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 digitos numericos, sem pontuacao")
        String cpf,

        @NotBlank(message = "O RG e obrigatorio")
        String rg,

        @NotBlank(message = "O endereco e obrigatorio")
        String endereco,

        @NotNull(message = "Selecione a instituicao de ensino")
        Long instituicaoId,

        @NotBlank(message = "O curso e obrigatorio")
        String curso,

        @NotBlank(message = "O e-mail e obrigatorio")
        @Email(message = "Informe um e-mail valido")
        String email,

        @NotBlank(message = "O login e obrigatorio")
        @Size(min = 3, max = 50, message = "O login deve ter entre 3 e 50 caracteres")
        String login,

        @NotBlank(message = "A senha e obrigatoria")
        @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres")
        String senha
) {
}

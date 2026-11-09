package br.edu.pucminas.moeda.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Regras usadas nas anotacoes @PreAuthorize, ex.: @PreAuthorize("@acesso.ehOProprio(#id, authentication)").
 * Impede que um usuario logado acesse dados de outro trocando o id na URL ou no corpo.
 */
@Component("acesso")
public class AcessoUsuario {

    /** O "sub" do token e o id do usuario, exposto como authentication.getName(). */
    public boolean ehOProprio(Long usuarioId, Authentication authentication) {
        return usuarioId != null && authentication != null
                && String.valueOf(usuarioId).equals(authentication.getName());
    }
}

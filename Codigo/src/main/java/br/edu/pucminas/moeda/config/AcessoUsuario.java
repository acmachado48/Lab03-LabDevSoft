package br.edu.pucminas.moeda.config;

import br.edu.pucminas.moeda.repository.VantagemRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Regras usadas nas anotacoes @PreAuthorize, ex.: @PreAuthorize("@acesso.ehOProprio(#id, authentication)").
 * Impede que um usuario logado acesse dados de outro trocando o id na URL ou no corpo.
 */
@Component("acesso")
public class AcessoUsuario {

    private final VantagemRepository vantagemRepository;

    public AcessoUsuario(VantagemRepository vantagemRepository) {
        this.vantagemRepository = vantagemRepository;
    }

    /** O "sub" do token e o id do usuario, exposto como authentication.getName(). */
    public boolean ehOProprio(Long usuarioId, Authentication authentication) {
        return usuarioId != null && authentication != null
                && String.valueOf(usuarioId).equals(authentication.getName());
    }

    /** O administrador gerencia tudo; a empresa, apenas os proprios dados. */
    public boolean ehAdminOuOProprio(Long usuarioId, Authentication authentication) {
        return ehAdmin(authentication) || ehOProprio(usuarioId, authentication);
    }

    /** Vantagem inexistente passa adiante para o service responder 404. */
    public boolean podeAlterarVantagem(Long vantagemId, Authentication authentication) {
        if (ehAdmin(authentication)) {
            return true;
        }
        return vantagemRepository.findById(vantagemId)
                .map(v -> ehOProprio(v.getEmpresa().getId(), authentication))
                .orElse(true);
    }

    private boolean ehAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}

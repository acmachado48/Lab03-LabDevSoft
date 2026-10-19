package br.edu.pucminas.moeda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.pucminas.moeda.model.Usuario;

import java.util.Optional;

/**
 * Consultas comuns a todos os tipos de usuario (email e login sao unicos no sistema todo).
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByLoginAndIdNot(String login, Long id);

    Optional<Usuario> findByLoginOrEmail(String login, String email);
}

package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.LoginRequestDTO;
import br.edu.pucminas.moeda.dto.LoginResponseDTO;
import br.edu.pucminas.moeda.exception.CredenciaisInvalidasException;
import br.edu.pucminas.moeda.model.Aluno;
import br.edu.pucminas.moeda.model.EmpresaParceira;
import br.edu.pucminas.moeda.model.Professor;
import br.edu.pucminas.moeda.model.Usuario;
import br.edu.pucminas.moeda.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLoginOrEmail(dto.login(), dto.login())
                .filter(u -> passwordEncoder.matches(dto.senha(), u.getSenha()))
                .orElseThrow(() -> new CredenciaisInvalidasException("Login ou senha incorretos"));

        return switch (usuario) {
            case Aluno a -> new LoginResponseDTO(a.getId(), a.getNome(), "ALUNO");
            case Professor p -> new LoginResponseDTO(p.getId(), p.getNome(), "PROFESSOR");
            case EmpresaParceira e -> new LoginResponseDTO(e.getId(), e.getNome(), "EMPRESA");
            default -> new LoginResponseDTO(usuario.getId(), usuario.getLogin(), "USUARIO");
        };
    }
}

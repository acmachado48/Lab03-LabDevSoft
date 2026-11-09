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
    private final TokenService tokenService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLoginOrEmail(dto.login(), dto.login())
                .filter(u -> passwordEncoder.matches(dto.senha(), u.getSenha()))
                .orElseThrow(() -> new CredenciaisInvalidasException("Login ou senha incorretos"));

        String nome;
        String tipo;
        switch (usuario) {
            case Aluno a -> { nome = a.getNome(); tipo = "ALUNO"; }
            case Professor p -> { nome = p.getNome(); tipo = "PROFESSOR"; }
            // A empresa de login "admin" administra o sistema (empresas e todas as vantagens)
            case EmpresaParceira e -> { nome = e.getNome(); tipo = "admin".equals(e.getLogin()) ? "ADMIN" : "EMPRESA"; }
            default -> { nome = usuario.getLogin(); tipo = "USUARIO"; }
        }

        String token = tokenService.gerarToken(usuario.getId(), nome, tipo);
        return new LoginResponseDTO(usuario.getId(), nome, tipo, token);
    }
}

package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.AlunoRequestDTO;
import br.edu.pucminas.moeda.dto.AlunoResponseDTO;
import br.edu.pucminas.moeda.exception.BusinessException;
import br.edu.pucminas.moeda.exception.ResourceNotFoundException;
import br.edu.pucminas.moeda.model.Aluno;
import br.edu.pucminas.moeda.model.Instituicao;
import br.edu.pucminas.moeda.repository.AlunoRepository;
import br.edu.pucminas.moeda.repository.InstituicaoRepository;
import br.edu.pucminas.moeda.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AlunoService {
    private final AlunoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository repository,
                        UsuarioRepository usuarioRepository,
                        InstituicaoRepository instituicaoRepository,
                        PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AlunoResponseDTO> listarTodos() {
        return repository.findAllByOrderByNomeAsc().stream()
                .map(AlunoResponseDTO::fromEntity)
                .toList();
    }

    public AlunoResponseDTO buscarPorId(Long id) {
        return repository.findComInstituicaoById(id)
                .map(AlunoResponseDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno nao encontrado (id " + id + ")"));
    }

    @Transactional
    public AlunoResponseDTO cadastrar(AlunoRequestDTO dto) {
        validarUnicidade(dto.cpf(), dto.email(), dto.login());

        Instituicao instituicao = instituicaoRepository.findById(dto.instituicaoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Instituicao nao encontrada (id " + dto.instituicaoId() + ")"));

        Aluno aluno = new Aluno();
        aluno.setNome(dto.nome());
        aluno.setCpf(dto.cpf());
        aluno.setRg(dto.rg());
        aluno.setEndereco(dto.endereco());
        aluno.setCurso(dto.curso());
        aluno.setInstituicao(instituicao);
        aluno.setEmail(dto.email());
        aluno.setLogin(dto.login());
        aluno.setSenha(passwordEncoder.encode(dto.senha()));

        return AlunoResponseDTO.fromEntity(repository.save(aluno));
    }

    private void validarUnicidade(String cpf, String email, String login) {
        if (repository.existsByCpf(cpf)) {
            throw new BusinessException("Ja existe um aluno cadastrado com este CPF");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Ja existe um usuario cadastrado com este e-mail");
        }
        if (usuarioRepository.existsByLogin(login)) {
            throw new BusinessException("Ja existe um usuario cadastrado com este login");
        }
    }
}

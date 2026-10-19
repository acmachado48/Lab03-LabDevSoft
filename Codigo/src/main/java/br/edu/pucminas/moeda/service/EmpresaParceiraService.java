package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.EmpresaParceiraRequestDTO;
import br.edu.pucminas.moeda.dto.EmpresaParceiraResponseDTO;
import br.edu.pucminas.moeda.dto.EmpresaParceiraUpdateDTO;
import br.edu.pucminas.moeda.exception.BusinessException;
import br.edu.pucminas.moeda.exception.ResourceNotFoundException;
import br.edu.pucminas.moeda.model.EmpresaParceira;
import br.edu.pucminas.moeda.repository.EmpresaParceiraRepository;
import br.edu.pucminas.moeda.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmpresaParceiraService {

    private final EmpresaParceiraRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpresaParceiraService(EmpresaParceiraRepository empresaRepository,
                                   UsuarioRepository usuarioRepository,
                                   PasswordEncoder passwordEncoder) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<EmpresaParceiraResponseDTO> listar() {
        return empresaRepository.findAllByOrderByNomeAsc().stream()
                .map(EmpresaParceiraResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpresaParceiraResponseDTO buscarPorId(Long id) {
        return EmpresaParceiraResponseDTO.fromEntity(buscarEntidadePorId(id));
    }

    public EmpresaParceiraResponseDTO criar(EmpresaParceiraRequestDTO dto) {
        String cnpjNormalizado = normalizarCnpj(dto.cnpj());

        validarUnicidadeParaCriacao(cnpjNormalizado, dto.email(), dto.login());

        EmpresaParceira empresa = new EmpresaParceira();
        empresa.setNome(dto.nome());
        empresa.setCnpj(cnpjNormalizado);
        empresa.setTelefone(dto.telefone());
        empresa.setEmail(dto.email());
        empresa.setLogin(dto.login());
        empresa.setSenha(passwordEncoder.encode(dto.senha()));

        return EmpresaParceiraResponseDTO.fromEntity(empresaRepository.save(empresa));
    }

    public EmpresaParceiraResponseDTO atualizar(Long id, EmpresaParceiraUpdateDTO dto) {
        EmpresaParceira empresa = buscarEntidadePorId(id);
        String cnpjNormalizado = normalizarCnpj(dto.cnpj());

        validarUnicidadeParaAtualizacao(id, cnpjNormalizado, dto.email(), dto.login());

        empresa.setNome(dto.nome());
        empresa.setCnpj(cnpjNormalizado);
        empresa.setTelefone(dto.telefone());
        empresa.setEmail(dto.email());
        empresa.setLogin(dto.login());
        if (dto.senha() != null && !dto.senha().isBlank()) {
            empresa.setSenha(passwordEncoder.encode(dto.senha()));
        }

        return EmpresaParceiraResponseDTO.fromEntity(empresaRepository.save(empresa));
    }

    public void excluir(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa parceira nao encontrada (id " + id + ")");
        }
        empresaRepository.deleteById(id);
    }

    private EmpresaParceira buscarEntidadePorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa parceira nao encontrada (id " + id + ")"));
    }

    private void validarUnicidadeParaCriacao(String cnpj, String email, String login) {
        if (empresaRepository.existsByCnpj(cnpj)) {
            throw new BusinessException("Ja existe uma empresa cadastrada com este CNPJ");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Ja existe um usuario cadastrado com este e-mail");
        }
        if (usuarioRepository.existsByLogin(login)) {
            throw new BusinessException("Ja existe um usuario cadastrado com este login");
        }
    }

    private void validarUnicidadeParaAtualizacao(Long id, String cnpj, String email, String login) {
        if (empresaRepository.existsByCnpjAndIdNot(cnpj, id)) {
            throw new BusinessException("Ja existe uma empresa cadastrada com este CNPJ");
        }
        if (usuarioRepository.existsByEmailAndIdNot(email, id)) {
            throw new BusinessException("Ja existe um usuario cadastrado com este e-mail");
        }
        if (usuarioRepository.existsByLoginAndIdNot(login, id)) {
            throw new BusinessException("Ja existe um usuario cadastrado com este login");
        }
    }

    private String normalizarCnpj(String cnpj) {
        return cnpj == null ? null : cnpj.replaceAll("\\D", "");
    }
}

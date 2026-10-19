package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.VantagemRequestDTO;
import br.edu.pucminas.moeda.dto.VantagemResponseDTO;
import br.edu.pucminas.moeda.dto.VantagemUpdateDTO;
import br.edu.pucminas.moeda.exception.BusinessException;
import br.edu.pucminas.moeda.exception.ResourceNotFoundException;
import br.edu.pucminas.moeda.model.EmpresaParceira;
import br.edu.pucminas.moeda.model.Vantagem;
import br.edu.pucminas.moeda.repository.EmpresaParceiraRepository;
import br.edu.pucminas.moeda.repository.ResgateRepository;
import br.edu.pucminas.moeda.repository.VantagemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VantagemService {

    private final VantagemRepository vantagemRepository;
    private final EmpresaParceiraRepository empresaRepository;
    private final ResgateRepository resgateRepository;

    public VantagemService(VantagemRepository vantagemRepository, EmpresaParceiraRepository empresaRepository,
                           ResgateRepository resgateRepository) {
        this.vantagemRepository = vantagemRepository;
        this.empresaRepository = empresaRepository;
        this.resgateRepository = resgateRepository;
    }

    public VantagemResponseDTO criar(VantagemRequestDTO dto) {
        EmpresaParceira empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa parceira não encontrada (id " + dto.empresaId() + ")"));

        Vantagem vantagem = new Vantagem();
        vantagem.setDescricao(dto.descricao());
        vantagem.setCustoEmMoedas(dto.custoEmMoedas());
        vantagem.setFoto(dto.foto());
        vantagem.setEmpresa(empresa);

        vantagem = vantagemRepository.save(vantagem);
        return VantagemResponseDTO.fromEntity(vantagem);
    }

    @Transactional(readOnly = true)
    public List<VantagemResponseDTO> listarTodas() {
        return vantagemRepository.findAll().stream()
                .map(VantagemResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VantagemResponseDTO> listarPorEmpresa(Long empresaId) {
        if (!empresaRepository.existsById(empresaId)) {
            throw new ResourceNotFoundException("Empresa parceira não encontrada (id " + empresaId + ")");
        }
        return vantagemRepository.findByEmpresaId(empresaId).stream()
                .map(VantagemResponseDTO::fromEntity)
                .toList();
    }

    public void excluir(Long id) {
        if (!vantagemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vantagem não encontrada (id " + id + ")");
        }
        // Resgates ja feitos precisam continuar no extrato dos alunos
        if (resgateRepository.existsByVantagemId(id)) {
            throw new BusinessException("Esta vantagem já foi resgatada por alunos e não pode ser excluída");
        }
        vantagemRepository.deleteById(id);
    }

    public VantagemResponseDTO atualizar(Long id, VantagemUpdateDTO dto) {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vantagem não encontrada (id " + id + ")"));

        vantagem.setDescricao(dto.descricao());
        vantagem.setCustoEmMoedas(dto.custoEmMoedas());
        vantagem.setFoto(dto.foto());

        vantagem = vantagemRepository.save(vantagem);
        return VantagemResponseDTO.fromEntity(vantagem);
    }
}


package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.VantagemRequestDTO;
import br.edu.pucminas.moeda.dto.VantagemResponseDTO;
import br.edu.pucminas.moeda.exception.ResourceNotFoundException;
import br.edu.pucminas.moeda.model.EmpresaParceira;
import br.edu.pucminas.moeda.model.Vantagem;
import br.edu.pucminas.moeda.repository.EmpresaParceiraRepository;
import br.edu.pucminas.moeda.repository.VantagemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VantagemService {

    private final VantagemRepository vantagemRepository;
    private final EmpresaParceiraRepository empresaRepository;

    public VantagemService(VantagemRepository vantagemRepository, EmpresaParceiraRepository empresaRepository) {
        this.vantagemRepository = vantagemRepository;
        this.empresaRepository = empresaRepository;
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
        vantagemRepository.deleteById(id);
    }
}
package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.InstituicaoResponseDTO;
import br.edu.pucminas.moeda.repository.InstituicaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InstituicaoService {

    private final InstituicaoRepository repository;

    public InstituicaoService(InstituicaoRepository repository) {
        this.repository = repository;
    }

    public List<InstituicaoResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(InstituicaoResponseDTO::fromEntity)
                .toList();
    }
}
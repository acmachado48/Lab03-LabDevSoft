package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.AlunoResponseDTO;
import br.edu.pucminas.moeda.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AlunoService {
    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public List<AlunoResponseDTO> listarTodos() {
        return repository.findAllByOrderByNomeAsc().stream()
                .map(AlunoResponseDTO::fromEntity)
                .toList();
    }
}
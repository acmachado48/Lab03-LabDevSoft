package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.ProfessorResponseDTO;
import br.edu.pucminas.moeda.exception.ResourceNotFoundException;
import br.edu.pucminas.moeda.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfessorService {
    private final ProfessorRepository repository;

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public ProfessorResponseDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(ProfessorResponseDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
    }
}
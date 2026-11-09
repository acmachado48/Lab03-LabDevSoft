package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.ProfessorResponseDTO;
import br.edu.pucminas.moeda.service.ProfessorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/professores")
public class ProfessorController {
    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@acesso.ehOProprio(#id, authentication)")
    public ProfessorResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
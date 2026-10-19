package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.AlunoResponseDTO;
import br.edu.pucminas.moeda.service.AlunoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlunoResponseDTO> listar() {
        return service.listarTodos();
    }
}
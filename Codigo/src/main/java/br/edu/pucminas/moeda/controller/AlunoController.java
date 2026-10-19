package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.AlunoRequestDTO;
import br.edu.pucminas.moeda.dto.AlunoResponseDTO;
import br.edu.pucminas.moeda.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
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

    @GetMapping("/{id}")
    public AlunoResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrar(@RequestBody @Valid AlunoRequestDTO dto) {
        AlunoResponseDTO criado = service.cadastrar(dto);
        return ResponseEntity.created(URI.create("/api/alunos/" + criado.id())).body(criado);
    }
}

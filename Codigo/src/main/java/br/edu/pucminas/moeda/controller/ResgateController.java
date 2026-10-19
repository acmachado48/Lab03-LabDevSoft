package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.ResgateRequestDTO;
import br.edu.pucminas.moeda.dto.ResgateResponseDTO;
import br.edu.pucminas.moeda.service.ResgateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/resgates")
public class ResgateController {
    private final ResgateService service;

    public ResgateController(ResgateService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResgateResponseDTO resgatar(@RequestBody @Valid ResgateRequestDTO dto) {
        return service.resgatar(dto);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<ResgateResponseDTO> listarPorAluno(@PathVariable Long alunoId) {
        return service.listarPorAluno(alunoId);
    }
}

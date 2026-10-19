package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.TransacaoRequestDTO;
import br.edu.pucminas.moeda.dto.TransacaoResponseDTO;
import br.edu.pucminas.moeda.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {
    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void criarTransacao(@RequestBody @Valid TransacaoRequestDTO dto) {
        service.enviarMoedas(dto);
    }

    @GetMapping("/professor/{professorId}")
    public List<TransacaoResponseDTO> listarPorProfessor(@PathVariable Long professorId) {
        return service.listarPorProfessor(professorId);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<TransacaoResponseDTO> listarPorAluno(@PathVariable Long alunoId) {
        return service.listarPorAluno(alunoId);
    }
}
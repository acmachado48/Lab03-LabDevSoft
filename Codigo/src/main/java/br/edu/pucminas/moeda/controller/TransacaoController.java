package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.TransacaoRequestDTO;
import br.edu.pucminas.moeda.dto.TransacaoResponseDTO;
import br.edu.pucminas.moeda.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@acesso.ehOProprio(#dto.professorId(), authentication)")
    public void criarTransacao(@RequestBody @Valid TransacaoRequestDTO dto) {
        service.enviarMoedas(dto);
    }

    @GetMapping("/professor/{professorId}")
    @PreAuthorize("hasRole('PROFESSOR') and @acesso.ehOProprio(#professorId, authentication)")
    public List<TransacaoResponseDTO> listarPorProfessor(@PathVariable Long professorId) {
        return service.listarPorProfessor(professorId);
    }

    @GetMapping("/aluno/{alunoId}")
    @PreAuthorize("hasRole('ALUNO') and @acesso.ehOProprio(#alunoId, authentication)")
    public List<TransacaoResponseDTO> listarPorAluno(@PathVariable Long alunoId) {
        return service.listarPorAluno(alunoId);
    }
}
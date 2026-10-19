package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.InstituicaoResponseDTO;
import br.edu.pucminas.moeda.service.InstituicaoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {

    private final InstituicaoService service;

    public InstituicaoController(InstituicaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<InstituicaoResponseDTO> listar() {
        return service.listarTodas();
    }
}
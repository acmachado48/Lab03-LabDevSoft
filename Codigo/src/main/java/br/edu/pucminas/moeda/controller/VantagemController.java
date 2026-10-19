package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.VantagemRequestDTO;
import br.edu.pucminas.moeda.dto.VantagemResponseDTO;
import br.edu.pucminas.moeda.dto.VantagemUpdateDTO;
import br.edu.pucminas.moeda.service.VantagemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vantagens")
public class VantagemController {

    private final VantagemService service;

    public VantagemController(VantagemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VantagemResponseDTO> criar(@RequestBody @Valid VantagemRequestDTO dto) {
        VantagemResponseDTO criada = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/vantagens/" + criada.id())).body(criada);
    }

    @GetMapping
    public List<VantagemResponseDTO> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("/empresa/{empresaId}")
    public List<VantagemResponseDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        return service.listarPorEmpresa(empresaId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @PutMapping("/{id}")
    public VantagemResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid VantagemUpdateDTO dto) {
        return service.atualizar(id, dto);
    }
}
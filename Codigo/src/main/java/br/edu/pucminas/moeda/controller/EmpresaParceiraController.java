package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.EmpresaParceiraRequestDTO;
import br.edu.pucminas.moeda.dto.EmpresaParceiraResponseDTO;
import br.edu.pucminas.moeda.dto.EmpresaParceiraUpdateDTO;
import br.edu.pucminas.moeda.service.EmpresaParceiraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/empresas")
public class EmpresaParceiraController {

    private final EmpresaParceiraService service;

    public EmpresaParceiraController(EmpresaParceiraService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmpresaParceiraResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public EmpresaParceiraResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<EmpresaParceiraResponseDTO> criar(@RequestBody @Valid EmpresaParceiraRequestDTO dto) {
        EmpresaParceiraResponseDTO criada = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/empresas/" + criada.id())).body(criada);
    }

    @PutMapping("/{id}")
    public EmpresaParceiraResponseDTO atualizar(@PathVariable Long id,
                                                 @RequestBody @Valid EmpresaParceiraUpdateDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}

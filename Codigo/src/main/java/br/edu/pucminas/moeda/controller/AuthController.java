package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.dto.LoginRequestDTO;
import br.edu.pucminas.moeda.dto.LoginResponseDTO;
import br.edu.pucminas.moeda.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        return service.autenticar(dto);
    }
}

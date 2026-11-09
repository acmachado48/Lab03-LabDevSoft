package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/** Gera o JWT entregue no login. O "sub" e o id do usuario e a claim "papel" define as permissoes. */
@Service
public class TokenService {

    private static final String EMISSOR = "moeda-estudantil";

    private final JwtEncoder jwtEncoder;
    private final long expiracaoMinutos;

    public TokenService(JwtEncoder jwtEncoder, @Value("${app.jwt.expiracao-minutos}") long expiracaoMinutos) {
        this.jwtEncoder = jwtEncoder;
        this.expiracaoMinutos = expiracaoMinutos;
    }

    public String gerarToken(Long usuarioId, String nome, String papel) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(EMISSOR)
                .subject(String.valueOf(usuarioId))
                .issuedAt(agora)
                .expiresAt(agora.plus(expiracaoMinutos, ChronoUnit.MINUTES))
                .claim("nome", nome)
                .claim(SecurityConfig.CLAIM_PAPEL, papel)
                .build();
        JwsHeader cabecalho = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(cabecalho, claims)).getTokenValue();
    }
}

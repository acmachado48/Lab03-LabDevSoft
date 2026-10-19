package br.edu.pucminas.moeda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/** Troca de moedas de um aluno por uma vantagem. O codigo do cupom confere a troca presencial. */
@Entity
@Table(name = "resgate")
public class Resgate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String codigoCupom = gerarCodigo();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vantagem_id", nullable = false)
    private Vantagem vantagem;

    // Custo pago no momento do resgate (a vantagem pode mudar de preco depois)
    @Column(nullable = false)
    private Integer valor;

    @Column(nullable = false)
    private LocalDateTime dataResgate = LocalDateTime.now();

    public static String gerarCodigo() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getCodigoCupom() { return codigoCupom; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Vantagem getVantagem() { return vantagem; }
    public void setVantagem(Vantagem vantagem) { this.vantagem = vantagem; }
    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }
    public LocalDateTime getDataResgate() { return dataResgate; }
}

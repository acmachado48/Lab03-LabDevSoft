package br.edu.pucminas.moeda.repository;

import br.edu.pucminas.moeda.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByProfessorIdOrderByDataTransacaoDesc(Long professorId);
}
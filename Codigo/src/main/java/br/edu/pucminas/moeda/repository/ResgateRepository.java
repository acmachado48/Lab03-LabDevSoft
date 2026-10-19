package br.edu.pucminas.moeda.repository;

import br.edu.pucminas.moeda.model.Resgate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResgateRepository extends JpaRepository<Resgate, Long> {
    // Carrega vantagem e empresa na mesma consulta para montar o extrato
    @EntityGraph(attributePaths = {"vantagem", "vantagem.empresa"})
    List<Resgate> findByAlunoIdOrderByDataResgateDesc(Long alunoId);

    boolean existsByVantagemId(Long vantagemId);
}

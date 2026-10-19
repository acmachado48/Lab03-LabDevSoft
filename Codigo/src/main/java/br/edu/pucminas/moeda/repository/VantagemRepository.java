package br.edu.pucminas.moeda.repository;

import br.edu.pucminas.moeda.model.Vantagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VantagemRepository extends JpaRepository<Vantagem, Long> {
    List<Vantagem> findByEmpresaId(Long empresaId);
}
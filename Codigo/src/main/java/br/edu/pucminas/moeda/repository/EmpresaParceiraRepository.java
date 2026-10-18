package br.edu.pucminas.moeda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.pucminas.moeda.model.EmpresaParceira;

import java.util.List;

public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdNot(String cnpj, Long id);

    List<EmpresaParceira> findAllByOrderByNomeAsc();
}

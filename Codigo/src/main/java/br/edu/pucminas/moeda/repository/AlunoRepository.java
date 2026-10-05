package br.edu.pucminas.moeda.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.pucminas.moeda.model.Aluno;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByCpf(String cpf);

    // Carrega a instituicao na mesma consulta para evitar N+1 selects na listagem
    @EntityGraph(attributePaths = "instituicao")
    List<Aluno> findAllByOrderByNomeAsc();

    @EntityGraph(attributePaths = "instituicao")
    Optional<Aluno> findComInstituicaoById(Long id);
}

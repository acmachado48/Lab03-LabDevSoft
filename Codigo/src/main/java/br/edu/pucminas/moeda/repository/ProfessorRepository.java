package br.edu.pucminas.moeda.repository;

import br.edu.pucminas.moeda.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {}
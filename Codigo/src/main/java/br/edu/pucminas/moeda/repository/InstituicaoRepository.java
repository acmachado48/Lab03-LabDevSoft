package br.edu.pucminas.moeda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.pucminas.moeda.model.Instituicao;

public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {
}

package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.TransacaoRequestDTO;
import br.edu.pucminas.moeda.dto.TransacaoResponseDTO;
import br.edu.pucminas.moeda.exception.BusinessException;
import br.edu.pucminas.moeda.exception.ResourceNotFoundException;
import br.edu.pucminas.moeda.model.Aluno;
import br.edu.pucminas.moeda.model.Professor;
import br.edu.pucminas.moeda.model.Transacao;
import br.edu.pucminas.moeda.repository.AlunoRepository;
import br.edu.pucminas.moeda.repository.ProfessorRepository;
import br.edu.pucminas.moeda.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TransacaoService {
    private final TransacaoRepository transacaoRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, ProfessorRepository professorRepository, AlunoRepository alunoRepository) {
        this.transacaoRepository = transacaoRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
    }

    public void enviarMoedas(TransacaoRequestDTO dto) {
        Professor professor = professorRepository.findById(dto.professorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
        
        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        if (professor.getSaldoMoedas() < dto.valor()) {
            throw new BusinessException("Saldo insuficiente. O seu saldo atual é de " + professor.getSaldoMoedas() + " moedas.");
        }

        professor.setSaldoMoedas(professor.getSaldoMoedas() - dto.valor());
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() + dto.valor());

        Transacao transacao = new Transacao();
        transacao.setProfessor(professor);
        transacao.setAluno(aluno);
        transacao.setValor(dto.valor());
        transacao.setMotivo(dto.motivo());
        
        transacaoRepository.save(transacao);
        
        // TODO: Enviar email ao aluno
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarPorProfessor(Long professorId) {
        return transacaoRepository.findByProfessorIdOrderByDataTransacaoDesc(professorId).stream()
                .map(TransacaoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarPorAluno(Long alunoId) {
        return transacaoRepository.findByAlunoIdOrderByDataTransacaoDesc(alunoId).stream()
                .map(TransacaoResponseDTO::fromEntity)
                .toList();
    }
}
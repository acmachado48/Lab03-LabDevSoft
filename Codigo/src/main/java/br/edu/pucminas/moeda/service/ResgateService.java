package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.dto.ResgateRequestDTO;
import br.edu.pucminas.moeda.dto.ResgateResponseDTO;
import br.edu.pucminas.moeda.exception.BusinessException;
import br.edu.pucminas.moeda.exception.ResourceNotFoundException;
import br.edu.pucminas.moeda.model.Aluno;
import br.edu.pucminas.moeda.model.Resgate;
import br.edu.pucminas.moeda.model.Vantagem;
import br.edu.pucminas.moeda.repository.AlunoRepository;
import br.edu.pucminas.moeda.repository.ResgateRepository;
import br.edu.pucminas.moeda.repository.VantagemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ResgateService {
    private final ResgateRepository resgateRepository;
    private final AlunoRepository alunoRepository;
    private final VantagemRepository vantagemRepository;

    public ResgateService(ResgateRepository resgateRepository, AlunoRepository alunoRepository,
                          VantagemRepository vantagemRepository) {
        this.resgateRepository = resgateRepository;
        this.alunoRepository = alunoRepository;
        this.vantagemRepository = vantagemRepository;
    }

    public ResgateResponseDTO resgatar(ResgateRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        Vantagem vantagem = vantagemRepository.findById(dto.vantagemId())
                .orElseThrow(() -> new ResourceNotFoundException("Vantagem não encontrada"));

        Integer custo = vantagem.getCustoEmMoedas();
        if (aluno.getSaldoMoedas() < custo) {
            throw new BusinessException("Saldo insuficiente. O seu saldo atual é de " + aluno.getSaldoMoedas() + " moedas.");
        }

        aluno.setSaldoMoedas(aluno.getSaldoMoedas() - custo);

        Resgate resgate = new Resgate();
        resgate.setAluno(aluno);
        resgate.setVantagem(vantagem);
        resgate.setValor(custo);

        // TODO: Enviar email com o cupom ao aluno e à empresa parceira
        return ResgateResponseDTO.fromEntity(resgateRepository.save(resgate));
    }

    @Transactional(readOnly = true)
    public List<ResgateResponseDTO> listarPorAluno(Long alunoId) {
        return resgateRepository.findByAlunoIdOrderByDataResgateDesc(alunoId).stream()
                .map(ResgateResponseDTO::fromEntity)
                .toList();
    }
}

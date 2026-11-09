package br.edu.pucminas.moeda.service;

import br.edu.pucminas.moeda.model.Aluno;
import br.edu.pucminas.moeda.model.Professor;
import br.edu.pucminas.moeda.model.Transacao;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class EmailTransacaoService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String remetente;

    public EmailTransacaoService(JavaMailSender mailSender, TemplateEngine templateEngine,
                                 @Value("${app.mail.from}") String remetente) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.remetente = remetente;
    }

    public void enviarConfirmacaoAoProfessor(Professor professor, Aluno aluno, Transacao transacao) {
        Context context = criarContexto(transacao);
        context.setVariable("nomeDestinatario", professor.getNome());
        context.setVariable("nomeAluno", aluno.getNome());

        enviar(professor.getEmail(), "Confirmação de envio de moedas",
                "email/confirmacao-envio-professor", context);
    }

    public void notificarAluno(Professor professor, Aluno aluno, Transacao transacao) {
        Context context = criarContexto(transacao);
        context.setVariable("nomeDestinatario", aluno.getNome());
        context.setVariable("nomeProfessor", professor.getNome());

        enviar(aluno.getEmail(), "Você recebeu moedas de reconhecimento",
                "email/notificacao-recebimento-aluno", context);
    }

    private Context criarContexto(Transacao transacao) {
        Context context = new Context(Locale.forLanguageTag("pt-BR"));
        context.setVariable("quantidade", transacao.getValor());
        context.setVariable("motivo", transacao.getMotivo());
        return context;
    }

    private void enviar(String destinatario, String assunto, String template, Context context) {
        MimeMessage mensagem = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    mensagem, StandardCharsets.UTF_8.name());
            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(templateEngine.process(template, context), true);
        } catch (MessagingException exception) {
            throw new MailPreparationException(
                    "Não foi possível preparar o email de transação para " + destinatario, exception);
        }
        mailSender.send(mensagem);
    }
}

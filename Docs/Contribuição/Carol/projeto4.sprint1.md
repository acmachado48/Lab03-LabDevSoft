## Atividades realizadas

- Centralização do fluxo de login, garantindo que todos os perfis (Aluno, Professor e Administrador/Empresa) utilizem a mesma interface de entrada e sejam redirecionados corretamente.
- Implementação de travas de segurança (validação de sessão via JavaScript) para proteger o painel administrativo contra acessos diretos não autenticados.
- Criação de um script de inserção de dados iniciais (`data.sql`) para gerar um usuário Administrador padrão, resolvendo o bloqueio de acesso inicial ao sistema.
- Diagnóstico e resolução de um problema de *rollback* nas transações do banco de dados que estava sendo causado por falhas de autenticação no serviço SMTP de e-mail.

## Nota de Transparência sobre o Uso de IA

Utilizei a ferramenta de Inteligência Artificial **Gemini (Google)** como apoio às seguintes etapas:

- Diagnóstico de erros de compilação do Maven para identificar dependências e classes ausentes.
- Auxílio na identificação de gargalos e exceções silenciosas relacionadas à integração com o `JavaMailSender`.

Todo o conteúdo e código gerado ou sugerido foi revisado, testado em ambiente local e validado por mim antes de ser incorporado à entrega final.


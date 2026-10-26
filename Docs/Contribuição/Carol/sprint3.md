## Novas Atividades Desenvolvidas

Nesta etapa, focamos na consolidação da infraestrutura de persistência, modelagem relacional avançada e na implementação completa dos fluxos de interação para estudantes e docentes, superando o escopo inicial:

- **Correção de Concorrência e Conexões do H2:** Resolução de travamentos de arquivos e bloqueios de instâncias concorrentes do banco de dados H2 através da configuração correta de exclusividade de processos e ajuste de contadores de auto-incremento de chaves primárias (`RESTART WITH`).
- **Implementação do Painel do Aluno (Front-end e Back-end):** 
  - Criação da tela de dashboard do estudante (`aluno.html` / `aluno-dashboard.html`) com exibição dinâmica de saldo em moedas e dados cadastrais.
  - Desenvolvimento do catálogo interativo de vantagens e benefícios disponibilizados por empresas parceiras, com validação de saldo e pop-ups de confirmação de resgate.
  - Implementação do fluxo de listagem e histórico de extrato de moedas recebidas via doações acadêmicas.
- **Estruturação do Sistema de Transações (`TransacaoService` e Controladores):** 
  - Criação da lógica de negócio para transferência de moedas entre professores e alunos, garantindo validação prévia de saldo disponível.
  - Mapeamento de rotas dedicadas para consulta de extrato por aluno (`/api/transacoes/aluno/{alunoId}`) e por professor, bem como a estruturação inicial para processamento de resgates.
- **Ajustes de CORS e Segurança:** Configuração explícita de políticas de acesso via `@CrossOrigin` e ajuste nos filtros de segurança (`SecurityConfig`) para viabilizar a comunicação assíncrona (`fetch`) entre o front-end estático e a API REST em Spring Boot.

## Nota de Transparência sobre o Uso de IA

Utilizei a ferramenta de Inteligência Artificial **Gemini (Google)** como apoio nas seguintes etapas:

- Auxílio na estruturação das views e scripts em JavaScript assíncrono (`fetch`) para consumo das APIs de saldo, vantagens e extrato do aluno.
- Suporte na modelagem de DTOs, métodos de repositório Spring Data JPA e tratamento de exceções para o motor de transações.
- Diagnóstico e correção de conflitos de mapeamento ORM (herança `JOINED` com `Usuario`, `Aluno` e `Professor`) e solução de erros de bloqueio de arquivos do H2.

Todo o conteúdo gerado foi revisado, testado, validado e ajustado por mim antes de ser incorporado à entrega.
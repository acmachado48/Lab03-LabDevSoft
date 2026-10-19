## Atividades realizadas

Nesta sprint eu elaborei o Modelo Entidade-Relacionamento (ER), desenvolvi o CRUD completo (Back-end e Front-end) de Empresas Parceiras e Vantagens, e implementei o fluxo de doação de moedas e extrato pelo Painel do Professor.

- **Modelagem do Banco de Dados (ER):** Elaboração do diagrama Entidade-Relacionamento (ER) com mapeamento de entidades, atributos, cardinalidades e restrições de integridade.
- **Gestão de Empresas Parceiras e Vantagens (CRUD Completo):** Desenvolvimento das APIs REST em Spring Boot contemplando todas as operações (criar, listar, editar e excluir), com validações de unicidade (CNPJ, e-mail e login) e DTOs específicos.
- **Painel do Professor e Motor de Transações:** Implementação da lógica de envio de moedas de reconhecimento aos alunos, validação de saldo disponível, registro de extrato no banco de dados (`TransacaoService`) e visualização no front-end.
- **Interfaces Front-end (HTML, CSS e JavaScript):** Criação do Dashboard administrativo responsivo (para empresas e vantagens), painel do professor e telas de visualização e cadastro para alunos (integração via `fetch`).
- **Infraestrutura e Carga Inicial de Dados (`data.sql`):** Configuração de carga inicial de instituições e professores pré-cadastrados, correção de auto-incremento de chaves primárias no H2 (`RESTART WITH`), tratamento de exceções globais e configuração de CORS.

## Nota de Transparência sobre o Uso de IA

Utilizei a ferramenta de Inteligência Artificial **Gemini (Google)** como apoio às seguintes etapas:

- Estruturação e estilização das interfaces visuais em HTML/CSS/JS para os painéis de administração, doação do professor e navegação do aluno.
- Auxílio na criação de DTOs, controllers e regras de negócio para atualização (PUT) de vantagens e registro de transações.
- Diagnóstico e correção de erros de infraestrutura, incluindo colisão de IDs no H2, erros de repositório JPA (`UsuarioRepository`) e bloqueios de conexões do banco de dados.

Todo o conteúdo gerado foi revisado, testado, validado e ajustado por mim antes de ser incorporado à entrega.
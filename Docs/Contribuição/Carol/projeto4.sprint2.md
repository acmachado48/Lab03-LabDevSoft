## Atividades realizadas

Nesta sprint eu elaborei os diagramas de sequência do sistema, cobrindo todas as histórias de usuário (HU01 a HU17).

- Modelagem de 8 diagramas de sequência em PlantUML, um por fluxo principal, cada um em um arquivo `.puml` próprio:
  - Login e logout (HU01, HU02)
  - Cadastro de aluno, com seleção da instituição pré-cadastrada (HU03)
  - Cadastro de empresa parceira e de vantagens (HU04, HU12, HU13)
  - Pré-cadastro de instituições e professores pelo administrador (HU05, HU06)
  - Crédito semestral de 1.000 moedas, com saldo acumulável (HU07)
  - Envio de moedas pelo professor, com validação de saldo e notificação por e-mail ao aluno (HU08, HU09)
  - Consulta de extrato de professor e de aluno (HU10, HU11)
  - Resgate de vantagem, com geração do código do cupom e e-mails ao aluno e à empresa parceira com o mesmo código (HU14 a HU17)
- Representação dos caminhos alternativos de cada fluxo (dados inválidos, saldo insuficiente, cadastro duplicado, credenciais inválidas), com os respectivos retornos ao usuário.

## Nota de Transparência sobre o Uso de IA

Utilizei a ferramenta de Inteligência Artificial **Claude (Anthropic)** como apoio às seguintes etapas:

- Elaboração inicial do código PlantUML dos diagramas de sequência a partir das histórias de usuário e da descrição do sistema fornecidas por mim.
- Diagnóstico e correção do erro de sintaxe apontado pelo preview do PlantUML no diagrama de resgate de vantagem.

Todo o conteúdo gerado foi revisado, renderizado no ambiente local e validado por mim antes de ser incorporado à entrega.
# Histórias de Usuário: Sistema de Mérito Estudantil
 
## Autenticação e Acesso
 
**HU01: Login no sistema**
Como **aluno, professor ou empresa parceira**, quero fazer login com meu usuário e senha, para acessar as funcionalidades do sistema com segurança.
 
Critérios de aceitação:
- O sistema exige autenticação antes de qualquer operação.
- Credenciais inválidas exibem mensagem de erro sem indicar qual campo está incorreto.
- Cada perfil (aluno, professor, empresa) acessa apenas as funcionalidades do seu papel.

**HU02: Logout**
Como **usuário autenticado**, quero encerrar minha sessão, para proteger minha conta em dispositivos compartilhados.
 
---
 
## Cadastros
 
**HU03: Cadastro de aluno**
Como **aluno**, quero me cadastrar informando nome, email, CPF, RG, endereço, instituição de ensino, curso, login e senha, para participar do sistema de mérito.
 
Critérios de aceitação:
- Todos os campos são obrigatórios.
- A instituição é escolhida a partir de uma lista de instituições pré-cadastradas (não é digitada livremente).
- CPF e email não podem estar duplicados no sistema.
- O saldo inicial do aluno é de 0 moedas.

**HU04: Cadastro de empresa parceira**
Como **empresa parceira**, quero me cadastrar no sistema com meus dados, login e senha, para oferecer vantagens aos alunos.
 
Critérios de aceitação:
- O cadastro exige os dados da empresa, incluindo o email para recebimento das notificações de troca.
- Após o cadastro, a empresa pode cadastrar vantagens.

**HU05: Pré-cadastro de instituições**
Como **administrador do sistema**, quero manter as instituições participantes pré-cadastradas, para que os alunos possam selecioná-las no cadastro.
 
**HU06: Pré-cadastro de professores**
Como **administrador do sistema**, quero carregar a lista de professores enviada pela instituição parceira (nome, CPF, departamento e instituição vinculada), para que eles já possam acessar o sistema.
 
Critérios de aceitação:
- Cada professor fica vinculado a uma única instituição participante.
- O professor recebe credenciais de acesso (login e senha) a partir do pré-cadastro.
---

## Saldo e Distribuição de Moedas (Professor)
 
**HU07: Crédito semestral de moedas**
Como **professor**, quero receber 1.000 moedas a cada semestre, para reconhecer meus alunos.
 
Critérios de aceitação:
- A cada novo semestre, o sistema credita 1.000 moedas ao saldo de cada professor.
- O saldo é acumulável: moedas não distribuídas permanecem e somam-se às novas 1.000.
- Exemplo: se sobraram 300 moedas, o saldo no novo semestre é 1.300.

**HU08: Enviar moedas a um aluno**
Como **professor**, quero enviar moedas a um aluno, informando o motivo do reconhecimento, para valorizar seu bom comportamento e participação.
 
Critérios de aceitação:
- O professor seleciona o aluno destinatário e informa a quantidade de moedas.
- O motivo é uma mensagem aberta e de preenchimento obrigatório.
- O envio só é permitido se o saldo for suficiente; caso contrário, o sistema exibe erro e não realiza a operação.
- O valor enviado é debitado do saldo do professor e creditado ao aluno.
- A transação fica registrada no extrato de ambos.

**HU09: Notificação de recebimento de moedas**
Como **aluno**, quero ser notificado por email ao receber moedas, para saber quando fui reconhecido.
 
Critérios de aceitação:
- O email é enviado ao aluno logo após a conclusão do envio.
- O email informa a quantidade de moedas, o professor remetente e o motivo.
---
 
## Extrato
 
**HU10: Consultar extrato (professor)**
Como **professor**, quero consultar meu extrato, para acompanhar meu saldo e os envios que realizei.
 
Critérios de aceitação:
- Exibe o saldo atual de moedas.
- Lista as transações de envio, com aluno, quantidade, motivo e data.

**HU11: Consultar extrato (aluno)**
Como **aluno**, quero consultar meu extrato, para acompanhar meu saldo e meu histórico de moedas.
 
Critérios de aceitação:
- Exibe o saldo atual de moedas.
- Lista os recebimentos (professor, quantidade, motivo, data) e as trocas realizadas (vantagem, custo, data).
---
 
## Vantagens (Empresa Parceira)
 
**HU12: Cadastrar vantagem**
Como **empresa parceira**, quero cadastrar vantagens com descrição, foto e custo em moedas, para que os alunos possam trocá-las.
 
Critérios de aceitação:
- Nome, descrição, foto do produto e custo em moedas são obrigatórios.
- A vantagem fica disponível para consulta pelos alunos após o cadastro.
- Exemplos: desconto em restaurantes da universidade, desconto de mensalidade, compra de materiais específicos.

**HU13: Gerenciar vantagens cadastradas** *(complementar)*
Como **empresa parceira**, quero editar ou remover minhas vantagens, para manter a oferta atualizada.
 
---
 
## Troca de Moedas (Aluno)
 
**HU14: Visualizar vantagens disponíveis**
Como **aluno**, quero ver a lista de vantagens com descrição, foto e custo, para escolher a que desejo resgatar.
 
**HU15: Resgatar vantagem**
Como **aluno**, quero trocar minhas moedas por uma vantagem, para usufruir do benefício.
 
Critérios de aceitação:
- A troca só é permitida se o saldo do aluno for suficiente.
- O custo da vantagem é descontado do saldo do aluno.
- O sistema gera um código único para a troca.
- A transação aparece no extrato do aluno.

**HU16: Receber cupom por email (aluno)**
Como **aluno**, quero receber um email com o cupom da vantagem resgatada, para apresentá-lo na troca presencial.
 
Critérios de aceitação:
- O email contém o código gerado pelo sistema, a vantagem e a empresa parceira.

**HU17: Receber email de conferência (parceiro)**
Como **empresa parceira**, quero receber um email quando um aluno resgatar minha vantagem, para conferir a troca presencial.
 
Critérios de aceitação:
- O email contém o mesmo código enviado ao aluno, além da vantagem resgatada.
- O código idêntico nos dois emails permite a conferência da troca.
---
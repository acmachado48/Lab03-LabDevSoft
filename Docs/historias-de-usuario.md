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
 
 Cadastros
 
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
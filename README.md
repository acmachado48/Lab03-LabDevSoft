# Lab03-LabDevSoft

Sistema de Moeda Estudantil.

## Como executar

Pré-requisito: JDK 21 ou superior.

```bash
cd Codigo
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. O console do banco fica em `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:file:./data/moeda`, usuário `sa`, sem senha).

## Configuração de email

O envio de emails de transações usa SMTP. Configure `SMTP_HOST`, `SMTP_PORT`,
`SMTP_USERNAME`, `SMTP_PASSWORD` e, quando necessário, `SMTP_AUTH` e
`SMTP_STARTTLS_ENABLE` e `SMTP_STARTTLS_REQUIRED` no ambiente antes de iniciar
a aplicação. Para Gmail, os padrões são `smtp.gmail.com`, porta `587` e
autenticação com STARTTLS. Use uma senha de app em `SMTP_PASSWORD`, não a senha
normal da conta. O remetente pode ser definido com `SMTP_FROM` e, por padrão,
usa o endereço configurado em `SMTP_USERNAME`. Não armazene a senha SMTP nos
arquivos do projeto.

## Estrutura do código (MVC em camadas)

```
Codigo/src/main/java/br/edu/pucminas/coin/
├── model/        Entidades JPA (mapeadas para as tabelas)
├── repository/   Interfaces Spring Data JPA (acesso a dados)
├── service/      Regras de negócio
├── controller/   Endpoints REST
├── dto/          Objetos de entrada e saída da API
└── config/       Configurações
Codigo/src/main/resources/static/   Front-end (HTML + JavaScript)
```

## Estratégia de acesso ao banco de dados

Usamos **ORM com Spring Data JPA** (Hibernate como implementação), com o acesso isolado em uma **camada de repositório**.

- **Entidades** (`model/`): cada classe persistente do diagrama de classes é uma `@Entity`. Os relacionamentos viram chaves estrangeiras via `@ManyToOne` / `@JoinColumn`.
- **Repositórios** (`repository/`): interfaces que estendem `JpaRepository`. O Spring gera a implementação em tempo de execução, com os métodos de CRUD (`save`, `findById`, `findAll`, `deleteById`) e consultas derivadas do nome do método (ex.: `existsByCpf`).
- **Serviços** (`service/`): são os únicos que usam os repositórios. Os controllers nunca acessam o banco diretamente.

**Herança de usuários:** `Usuario` é uma classe abstrata mapeada com `@Inheritance(strategy = JOINED)`. Os dados comuns (email, login, senha) ficam na tabela `usuario`, e cada subtipo (`aluno`, `empresa_parceira`, ...) tem sua própria tabela, com a mesma chave primária apontando para `usuario`. Escolhemos `JOINED` porque mantém as tabelas normalizadas, sem colunas nulas de outros subtipos, e garante email/login únicos no sistema todo.

**Por que JPA:** elimina o SQL repetitivo de CRUD, gera o esquema a partir das entidades (`ddl-auto=update`) e deixa o banco trocável. Para migrar de H2 para PostgreSQL basta trocar a dependência e a URL em `application.properties`.

**Banco:** H2 em modo arquivo (`Codigo/data/`), para que nenhum integrante precise instalar um SGBD.

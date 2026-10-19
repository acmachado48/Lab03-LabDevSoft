-- Dados iniciais. Cada INSERT so roda se o registro ainda nao existir, assim
-- os cadastros feitos pela aplicacao (alunos, transacoes...) sobrevivem a reinicios.
-- Os ids sao gerados pelo banco; os vinculos sao resolvidos por login/nome.

-- Pré-cadastro de Instituições de Ensino
INSERT INTO instituicao (nome) SELECT 'PUC Minas - Coração Eucarístico'
    WHERE NOT EXISTS (SELECT 1 FROM instituicao WHERE nome = 'PUC Minas - Coração Eucarístico');
INSERT INTO instituicao (nome) SELECT 'PUC Minas - Praça da Liberdade'
    WHERE NOT EXISTS (SELECT 1 FROM instituicao WHERE nome = 'PUC Minas - Praça da Liberdade');
INSERT INTO instituicao (nome) SELECT 'UFMG - Campus Pampulha'
    WHERE NOT EXISTS (SELECT 1 FROM instituicao WHERE nome = 'UFMG - Campus Pampulha');

-- Nota: a senha de todos os usuarios abaixo é '123456' (criptografada em BCrypt)

-- Corrige o hash invalido gravado por versoes anteriores deste script
UPDATE usuario SET senha = '$2a$10$y.6gTzC.D/ExCSPaTcco/.6FjSU1EsEXO46wYGYPmnrcTCXGowr56'
    WHERE senha = '$2a$10$E2UPv7arXym3L0.R76eSde.51261gHh/96gV.iT0.xH4x5eO7nOAG';

-- Professor 1 (PUC Minas - Coração Eucarístico)
INSERT INTO usuario (email, login, senha)
    SELECT 'carlos.silva@pucminas.br', 'carlos.silva', '$2a$10$y.6gTzC.D/ExCSPaTcco/.6FjSU1EsEXO46wYGYPmnrcTCXGowr56'
    WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE login = 'carlos.silva');
INSERT INTO professor (id, nome, cpf, departamento, saldo_moedas, instituicao_id)
    SELECT u.id, 'Carlos Silva', '11122233344', 'Engenharia de Software', 1000, i.id
    FROM usuario u, instituicao i
    WHERE u.login = 'carlos.silva' AND i.nome = 'PUC Minas - Coração Eucarístico'
      AND NOT EXISTS (SELECT 1 FROM professor p WHERE p.id = u.id);

-- Professor 2 (PUC Minas - Praça da Liberdade)
INSERT INTO usuario (email, login, senha)
    SELECT 'ana.mendonca@pucminas.br', 'ana.mendonca', '$2a$10$y.6gTzC.D/ExCSPaTcco/.6FjSU1EsEXO46wYGYPmnrcTCXGowr56'
    WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE login = 'ana.mendonca');
INSERT INTO professor (id, nome, cpf, departamento, saldo_moedas, instituicao_id)
    SELECT u.id, 'Ana Mendonça', '55566677788', 'Ciência da Computação', 1000, i.id
    FROM usuario u, instituicao i
    WHERE u.login = 'ana.mendonca' AND i.nome = 'PUC Minas - Praça da Liberdade'
      AND NOT EXISTS (SELECT 1 FROM professor p WHERE p.id = u.id);

-- Aluno de Teste
INSERT INTO usuario (email, login, senha)
    SELECT 'joao.aluno@pucminas.br', 'joao.aluno', '$2a$10$y.6gTzC.D/ExCSPaTcco/.6FjSU1EsEXO46wYGYPmnrcTCXGowr56'
    WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE login = 'joao.aluno');
INSERT INTO aluno (id, nome, cpf, rg, endereco, curso, saldo_moedas, instituicao_id)
    SELECT u.id, 'João Silva (Aluno Teste)', '12345678901', 'MG12345678', 'Rua dos Testes, 100', 'Engenharia de Software', 0, i.id
    FROM usuario u, instituicao i
    WHERE u.login = 'joao.aluno' AND i.nome = 'PUC Minas - Coração Eucarístico'
      AND NOT EXISTS (SELECT 1 FROM aluno a WHERE a.id = u.id);

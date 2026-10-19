-- Pré-cadastro de Instituições de Ensino
INSERT INTO instituicao (id, nome) VALUES (1, 'PUC Minas - Coração Eucarístico');
INSERT INTO instituicao (id, nome) VALUES (2, 'PUC Minas - Praça da Liberdade');
INSERT INTO instituicao (id, nome) VALUES (3, 'UFMG - Campus Pampulha');

-- Pré-cadastro de Professores
-- Nota: A senha de todos os professores abaixo é '123456' (criptografada em BCrypt)

-- Professor 1 (PUC Minas - Coração Eucarístico)
INSERT INTO usuario (id, email, login, senha) VALUES (1, 'carlos.silva@pucminas.br', 'carlos.silva', '$2a$10$E2UPv7arXym3L0.R76eSde.51261gHh/96gV.iT0.xH4x5eO7nOAG');
INSERT INTO professor (id, nome, cpf, departamento, saldo_moedas, instituicao_id) VALUES (1, 'Carlos Silva', '11122233344', 'Engenharia de Software', 1000, 1);

-- Professor 2 (PUC Minas - Praça da Liberdade)
INSERT INTO usuario (id, email, login, senha) VALUES (2, 'ana.mendonca@pucminas.br', 'ana.mendonca', '$2a$10$E2UPv7arXym3L0.R76eSde.51261gHh/96gV.iT0.xH4x5eO7nOAG');
INSERT INTO professor (id, nome, cpf, departamento, saldo_moedas, instituicao_id) VALUES (2, 'Ana Mendonça', '55566677788', 'Ciência da Computação', 1000, 2);

-- Atualiza os contadores de auto-incremento para evitar colisões com os inserts manuais
ALTER TABLE usuario ALTER COLUMN id RESTART WITH 10;
ALTER TABLE instituicao ALTER COLUMN id RESTART WITH 10;
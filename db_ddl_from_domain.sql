-- DDL gerado a partir das classes do pacote aulas.umc.oo.model
-- Instruções:
-- 1) Conectar ao servidor PostgreSQL (por exemplo, database postgres) com superuser/"raiz" e executar este arquivo.
-- 2) O CREATE DATABASE só funcionará quando conectado ao database "postgres" ou outro DB existente.

-- 1) criar o banco
CREATE DATABASE oo_db;

-- A partir daqui reconectar em oo_db e executar o restante (ou executar com psql -d oo_db)

-- Habilita função de gerar UUIDs
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Tabela: pessoa  (classe: aulas.umc.oo.model.Pessoa)
DROP TABLE IF EXISTS documento CASCADE;
DROP TABLE IF EXISTS endereco CASCADE;
DROP TABLE IF EXISTS pessoa CASCADE;

CREATE TABLE pessoa (
  id UUID PRIMARY KEY,
  nome VARCHAR(200) NOT NULL,                -- NomePessoa.valor
  idade INTEGER NOT NULL CHECK (idade BETWEEN 18 AND 99), -- IdadePessoa.valor
  email VARCHAR(255),                         -- Email.valor (opcional em uma das construtoras)
  tipo_sanguineo VARCHAR(10),                -- atributo tipoSanguineo
  status SMALLINT NOT NULL DEFAULT 1 CHECK (status IN (1,2,3)), -- 1=ativo,2=inativo,3=excluido
  criado_em TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Tabela: endereco (classe: aulas.umc.oo.model.Endereco)
CREATE TABLE endereco (
  id UUID PRIMARY KEY,
  pessoa_id UUID NOT NULL REFERENCES pessoa(id) ON DELETE CASCADE,
  rua VARCHAR(200),
  numero VARCHAR(50),
  cidade VARCHAR(100),
  status SMALLINT NOT NULL DEFAULT 1 CHECK (status IN (1,2,3))
);

-- Tabela: documento (classe: aulas.umc.oo.model.Documento)
CREATE TABLE documento (
  id UUID PRIMARY KEY,
  pessoa_id UUID NOT NULL REFERENCES pessoa(id) ON DELETE CASCADE,
  tipo VARCHAR(50),          -- Tipo.valor
  valor VARCHAR(200),        -- ValorDoc.valor
  demais_dados TEXT,         -- DemaisDados.valor
  status SMALLINT NOT NULL DEFAULT 1 CHECK (status IN (1,2,3))
);

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_pessoa_email ON pessoa(email);
CREATE INDEX IF NOT EXISTS idx_endereco_pessoa ON endereco(pessoa_id);
CREATE INDEX IF NOT EXISTS idx_documento_pessoa ON documento(pessoa_id);

-- Observações:
-- - As constraints refletem validações das value objects (ex.: idade entre 18 e 99).
-- - UUIDs são gerados pela aplicação; passe o id explicitamente nas inserções.
-- - Ajustar tamanhos de VARCHAR conforme necessidade do domínio real.

-- Fim do script

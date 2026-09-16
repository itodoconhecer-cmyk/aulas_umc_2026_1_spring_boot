-- DDL gerado a partir das classes do pacote aulas.umc.oo.model
-- Essa versão é idempotente: se o banco e as tabelas já existirem, nada será alterado.

-- Habilita função de gerar UUIDs
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Tabela: pessoa  (classe: aulas.umc.oo.model.Pessoa)
CREATE TABLE IF NOT EXISTS pessoa (
  id UUID PRIMARY KEY,
  nome VARCHAR(200) NOT NULL,
  idade INTEGER NOT NULL CHECK (idade BETWEEN 18 AND 99),
  email VARCHAR(255),
  tipo_sanguineo VARCHAR(10),
  status SMALLINT NOT NULL DEFAULT 1 CHECK (status IN (1,2,3)),
  criado_em TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Tabela: endereco (classe: aulas.umc.oo.model.Endereco)
CREATE TABLE IF NOT EXISTS endereco (
  id UUID PRIMARY KEY,
  pessoa_id UUID NOT NULL REFERENCES pessoa(id) ON DELETE CASCADE,
  rua VARCHAR(200),
  numero VARCHAR(50),
  cidade VARCHAR(100),
  status SMALLINT NOT NULL DEFAULT 1 CHECK (status IN (1,2,3))
);

-- Tabela: documento (classe: aulas.umc.oo.model.Documento)
CREATE TABLE IF NOT EXISTS documento (
  id UUID PRIMARY KEY,
  pessoa_id UUID NOT NULL REFERENCES pessoa(id) ON DELETE CASCADE,
  tipo VARCHAR(50),
  valor VARCHAR(200),
  demais_dados TEXT,
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

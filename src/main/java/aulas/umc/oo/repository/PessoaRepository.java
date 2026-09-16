package aulas.umc.oo.repository;

import aulas.umc.oo.model.Documento;
import aulas.umc.oo.model.Endereco;
import aulas.umc.oo.model.Pessoa;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaRepository {
    void insert(Pessoa pessoa) throws IOException;
    void insertWithRelations(Pessoa pessoa, List<Endereco> enderecos, List<Documento> documentos) throws IOException;
    List<Pessoa> findAll() throws IOException;
    Optional<Pessoa> findById(UUID id) throws IOException;
    void update(Pessoa pessoa) throws IOException;
    void deleteLogical(UUID id) throws IOException;
}

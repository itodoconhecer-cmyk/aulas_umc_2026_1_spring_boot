package aulas.umc.oo.repository;

import aulas.umc.oo.model.Documento;
import aulas.umc.oo.model.Endereco;
import aulas.umc.oo.model.Pessoa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaRepository {
    void insert(Pessoa pessoa);
    void insertWithRelations(Pessoa pessoa, List<Endereco> enderecos, List<Documento> documentos);
    List<Pessoa> findAll();
    Optional<Pessoa> findById(UUID id);
    void update(Pessoa pessoa);
    void deleteLogical(UUID id);
}

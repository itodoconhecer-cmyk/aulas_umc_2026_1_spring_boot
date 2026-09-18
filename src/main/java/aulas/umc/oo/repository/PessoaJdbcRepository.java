package aulas.umc.oo.repository;

import aulas.umc.oo.model.Pessoa;
import aulas.umc.oo.utilities.ConexaoPostGreSQL;
import model.valueObjects.Email;
import model.valueObjects.IdadePessoa;
import model.valueObjects.NomePessoa;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PessoaJdbcRepository implements PessoaRepository {

    private final DataSource dataSource;

    private static final String INSERT = "INSERT INTO pessoa (id, nome, idade, email, tipo_sanguineo, status, criado_em) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT id, nome, idade, email, tipo_sanguineo, status, criado_em FROM pessoa WHERE status != 3";
    private static final String SELECT_BY_ID = "SELECT id, nome, idade, email, tipo_sanguineo, status, criado_em FROM pessoa WHERE id = ? AND status != 3";
    private static final String UPDATE = "UPDATE pessoa SET nome = ?, idade = ?, email = ?, tipo_sanguineo = ?, status = ? WHERE id = ? AND status != 3";
    private static final String DELETE_LOGICAL = "UPDATE pessoa SET status = 3 WHERE id = ?";
    private static final String INSERT_ENDERECO = "INSERT INTO endereco (id, pessoa_id, rua, numero, cidade, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_DOCUMENTO = "INSERT INTO documento (id, pessoa_id, tipo, valor, demais_dados, status) VALUES (?, ?, ?, ?, ?, ?)";

    public PessoaJdbcRepository() {
        this.dataSource = null;
    }

    public PessoaJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Pessoa pessoa) throws IOException {

        DataSource ds = getDataSource();

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT)) {

            ps.setObject(1, pessoa.id);
            ps.setString(2, pessoa.nome.getValor());
            ps.setInt(3, pessoa.idade.getValor());
            ps.setString(4, pessoa.email != null ? pessoa.email.getValor() : null);
            ps.setString(5, pessoa.tipoSanguineo);
            ps.setShort(6, (short)1);
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro inserindo pessoa", e);
        }
    }

    @Override
    public List<Pessoa> findAll() throws IOException {
        DataSource ds = getDataSource();

        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT_ALL); ResultSet rs = ps.executeQuery()) {
            List<Pessoa> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException("Erro consultando pessoas", e);
        }
    }

    @Override
    public Optional<Pessoa> findById(UUID id) throws IOException {
        DataSource ds = getDataSource();
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro consultando pessoa por id", e);
        }
    }

    @Override
    public void update(Pessoa pessoa) throws IOException {
        DataSource ds = getDataSource();

        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE)) {
            ps.setString(1, pessoa.nome.getValor());
            ps.setInt(2, pessoa.idade.getValor());
            ps.setString(3, pessoa.email != null ? pessoa.email.getValor() : null);
            ps.setString(4, pessoa.tipoSanguineo);
            ps.setShort(5, (short)1); // keep active unless changed by caller via direct SQL
            ps.setObject(6, pessoa.id);
            int changed = ps.executeUpdate();
            if (changed == 0) {
                throw new RepositoryException("Nenhuma pessoa atualizada (talvez inexistente ou já excluída)");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro atualizando pessoa", e);
        }
    }

    @Override
    public void deleteLogical(UUID id) throws IOException {
        DataSource ds = getDataSource();
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(DELETE_LOGICAL)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro excluindo logicamente pessoa", e);
        }
    }

    @Override
    public void insertWithRelations(Pessoa pessoa, java.util.List<aulas.umc.oo.model.Endereco> enderecos, java.util.List<aulas.umc.oo.model.Documento> documentos) throws IOException {
        DataSource ds = getDataSource();
        try (Connection c = ds.getConnection()) {
            try {
                c.setAutoCommit(false);

                // insert pessoa
                try (PreparedStatement ps = c.prepareStatement(INSERT)) {
                    ps.setObject(1, pessoa.id);
                    ps.setString(2, pessoa.nome.getValor());
                    ps.setInt(3, pessoa.idade.getValor());
                    ps.setString(4, pessoa.email != null ? pessoa.email.getValor() : null);
                    ps.setString(5, pessoa.tipoSanguineo);
                    ps.setShort(6, (short)1);
                    ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                    ps.executeUpdate();
                }

                // insert enderecos
                try (PreparedStatement psEnd = c.prepareStatement(INSERT_ENDERECO)) {
                    for (aulas.umc.oo.model.Endereco e : enderecos) {
                        psEnd.setObject(1, e.id);
                        psEnd.setObject(2, pessoa.id);
                        psEnd.setString(3, e.getRua());
                        psEnd.setString(4, e.getNumero());
                        psEnd.setString(5, e.getCidade());
                        psEnd.setShort(6, (short)1);
                        psEnd.executeUpdate();
                    }
                }

                // insert documentos
                try (PreparedStatement psDoc = c.prepareStatement(INSERT_DOCUMENTO)) {
                    for (aulas.umc.oo.model.Documento d : documentos) {
                        psDoc.setObject(1, d.id);
                        psDoc.setObject(2, pessoa.id);
                        psDoc.setString(3, d.getTipo() != null ? d.getTipo().getValor() : null);
                        psDoc.setString(4, d.getValor() != null ? d.getValor().getValor() : null);
                        psDoc.setString(5, d.getDemaisDados() != null ? d.getDemaisDados().getValor() : null);
                        psDoc.setShort(6, (short)1);
                        psDoc.executeUpdate();
                    }
                }

                c.commit();
            } catch (SQLException e) {
                try { c.rollback(); } catch (SQLException ex) { /* ignore */ }
                throw new RepositoryException("Erro inserindo pessoa com relacionamentos", e);
            } finally {
                try { c.setAutoCommit(true); } catch (SQLException ignored) {}
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro obtendo conexão para transação", e);
        }
    }

    private Pessoa mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String nome = rs.getString("nome");
        int idade = rs.getInt("idade");
        String email = rs.getString("email");
        String tipoSanguineo = rs.getString("tipo_sanguineo");

        // use the lightweight constructor and set fields
        Pessoa p = new Pessoa(new NomePessoa(nome), email != null ? new Email(email) : null);
        p.id = id;
        p.idade = new IdadePessoa(idade);
        p.tipoSanguineo = tipoSanguineo;
        return p;
    }

    private DataSource getDataSource() throws IOException {
        if (dataSource != null) {
            return dataSource;
        }

        return ConexaoPostGreSQL.createFromApplicationProperties();
    }
}

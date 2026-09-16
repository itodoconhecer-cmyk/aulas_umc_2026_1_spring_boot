package aulas.umc.oo.repository;

import aulas.umc.oo.model.Endereco;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EnderecoJdbcRepository {
    private final DataSource ds;

    private static final String INSERT = "INSERT INTO endereco (id, pessoa_id, rua, numero, cidade, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_BY_PESSOA = "SELECT id, pessoa_id, rua, numero, cidade, status FROM endereco WHERE pessoa_id = ? AND status != 3";
    private static final String SELECT_BY_ID = "SELECT id, pessoa_id, rua, numero, cidade, status FROM endereco WHERE id = ? AND status != 3";
    private static final String UPDATE = "UPDATE endereco SET rua = ?, numero = ?, cidade = ?, status = ? WHERE id = ? AND status != 3";
    private static final String DELETE_LOGICAL = "UPDATE endereco SET status = 3 WHERE id = ?";

    public EnderecoJdbcRepository(DataSource ds) {
        this.ds = ds;
    }

    public void insert(Endereco endereco, UUID pessoaId) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setObject(1, endereco.id);
            ps.setObject(2, pessoaId);
            ps.setString(3, endereco.getRua());
            ps.setString(4, endereco.getNumero());
            ps.setString(5, endereco.getCidade());
            ps.setShort(6, (short)1);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro inserindo endereco", e);
        }
    }

    public List<Endereco> findAllByPessoaId(UUID pessoaId) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT_ALL_BY_PESSOA)) {
            ps.setObject(1, pessoaId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Endereco> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro consultando enderecos", e);
        }
    }

    public Optional<Endereco> findById(UUID id) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro consultando endereco por id", e);
        }
    }

    public void update(Endereco endereco) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE)) {
            ps.setString(1, endereco.getRua());
            ps.setString(2, endereco.getNumero());
            ps.setString(3, endereco.getCidade());
            ps.setShort(4, (short)1);
            ps.setObject(5, endereco.id);
            int changed = ps.executeUpdate();
            if (changed == 0) throw new RepositoryException("Nenhum endereco atualizado (talvez inexistente ou excluído)");
        } catch (SQLException e) {
            throw new RepositoryException("Erro atualizando endereco", e);
        }
    }

    public void deleteLogical(UUID id) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(DELETE_LOGICAL)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro excluindo logicamente endereco", e);
        }
    }

    private Endereco mapRow(ResultSet rs) throws SQLException {
        Endereco e = new Endereco();
        e.id = (UUID) rs.getObject("id");
        e.setRua(rs.getString("rua"));
        e.setNumero(rs.getString("numero"));
        e.setCidade(rs.getString("cidade"));
        return e;
    }
}

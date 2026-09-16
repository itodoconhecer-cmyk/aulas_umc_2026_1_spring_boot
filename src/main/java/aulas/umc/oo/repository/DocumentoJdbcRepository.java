package aulas.umc.oo.repository;

import aulas.umc.oo.model.Documento;
import aulas.umc.oo.model.valueObjects.ValorDoc;
import model.valueObjects.DemaisDados;
import model.valueObjects.Tipo;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DocumentoJdbcRepository {
    private final DataSource ds;

    private static final String INSERT = "INSERT INTO documento (id, pessoa_id, tipo, valor, demais_dados, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_BY_PESSOA = "SELECT id, pessoa_id, tipo, valor, demais_dados, status FROM documento WHERE pessoa_id = ? AND status != 3";
    private static final String SELECT_BY_ID = "SELECT id, pessoa_id, tipo, valor, demais_dados, status FROM documento WHERE id = ? AND status != 3";
    private static final String UPDATE = "UPDATE documento SET tipo = ?, valor = ?, demais_dados = ?, status = ? WHERE id = ? AND status != 3";
    private static final String DELETE_LOGICAL = "UPDATE documento SET status = 3 WHERE id = ?";

    public DocumentoJdbcRepository(DataSource ds) {
        this.ds = ds;
    }

    public void insert(Documento doc, UUID pessoaId) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setObject(1, doc.id);
            ps.setObject(2, pessoaId);
            ps.setString(3, doc.getTipo() != null ? doc.getTipo().getValor() : null);
            ps.setString(4, doc.getValor() != null ? doc.getValor().getValor() : null);
            ps.setString(5, doc.getDemaisDados() != null ? doc.getDemaisDados().getValor() : null);
            ps.setShort(6, (short)1);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro inserindo documento", e);
        }
    }

    public List<Documento> findAllByPessoaId(UUID pessoaId) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT_ALL_BY_PESSOA)) {
            ps.setObject(1, pessoaId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Documento> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro consultando documentos", e);
        }
    }

    public Optional<Documento> findById(UUID id) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro consultando documento por id", e);
        }
    }

    public void update(Documento doc) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(UPDATE)) {
            ps.setString(1, doc.getTipo() != null ? doc.getTipo().getValor() : null);
            ps.setString(2, doc.getValor() != null ? doc.getValor().getValor() : null);
            ps.setString(3, doc.getDemaisDados() != null ? doc.getDemaisDados().getValor() : null);
            ps.setShort(4, (short)1);
            ps.setObject(5, doc.id);
            int changed = ps.executeUpdate();
            if (changed == 0) throw new RepositoryException("Nenhum documento atualizado (talvez inexistente ou excluído)");
        } catch (SQLException e) {
            throw new RepositoryException("Erro atualizando documento", e);
        }
    }

    public void deleteLogical(UUID id) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(DELETE_LOGICAL)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro excluindo logicamente documento", e);
        }
    }

    private Documento mapRow(ResultSet rs) throws SQLException {
        Documento d = new Documento();
        d.id = (UUID) rs.getObject("id");
        String tipo = rs.getString("tipo");
        String valor = rs.getString("valor");
        String demais = rs.getString("demais_dados");
        d.setTipo(tipo != null ? new Tipo(tipo) : null);
        d.setValor(valor != null ? new ValorDoc(valor) : null);
        d.setDemaisDados(demais != null ? new DemaisDados(demais) : null);
        return d;
    }
}

package aulas.umc.oo.repository;

import aulas.umc.oo.model.Pessoa;
import model.valueObjects.Email;
import model.valueObjects.IdadePessoa;
import model.valueObjects.NomePessoa;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PessoaJdbcRepositoryTest {

    private PessoaJdbcRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        DataSource dataSource = createDataSource();
        createSchema(dataSource);
        repository = new PessoaJdbcRepository(dataSource);
    }

    @Test
    void deveExecutarCrudDePessoa() throws Exception {
        Pessoa pessoa = criarPessoa("Ana Silva", 30, "ana@email.com", "O+");

        repository.insert(pessoa);

        Optional<Pessoa> pessoaInserida = repository.findById(pessoa.id);
        assertTrue(pessoaInserida.isPresent());
        assertEquals("Ana Silva", pessoaInserida.get().nome.getValor());
        assertEquals(30, pessoaInserida.get().idade.getValor());
        assertEquals("ana@email.com", pessoaInserida.get().email.getValor());
        assertEquals("O+", pessoaInserida.get().tipoSanguineo);

        List<Pessoa> pessoas = repository.findAll();
        assertEquals(1, pessoas.size());

        pessoa.nome = new NomePessoa("Ana Souza");
        pessoa.idade = new IdadePessoa(31);
        pessoa.email = new Email("ana.souza@email.com");
        pessoa.tipoSanguineo = "A+";
        repository.update(pessoa);

        Pessoa pessoaAtualizada = repository.findById(pessoa.id).orElseThrow();
        assertEquals("Ana Souza", pessoaAtualizada.nome.getValor());
        assertEquals(31, pessoaAtualizada.idade.getValor());
        assertEquals("ana.souza@email.com", pessoaAtualizada.email.getValor());
        assertEquals("A+", pessoaAtualizada.tipoSanguineo);

        repository.deleteLogical(pessoa.id);

        assertFalse(repository.findById(pessoa.id).isPresent());
        assertTrue(repository.findAll().isEmpty());
    }

    private DataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    private void createSchema(DataSource dataSource) throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE pessoa (
                      id UUID PRIMARY KEY,
                      nome VARCHAR(200) NOT NULL,
                      idade INTEGER NOT NULL CHECK (idade BETWEEN 18 AND 99),
                      email VARCHAR(255),
                      tipo_sanguineo VARCHAR(10),
                      status SMALLINT NOT NULL DEFAULT 1 CHECK (status IN (1,2,3)),
                      criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
        }
    }

    private Pessoa criarPessoa(String nome, int idade, String email, String tipoSanguineo) {
        Pessoa pessoa = new Pessoa(new NomePessoa(nome), new Email(email));
        pessoa.idade = new IdadePessoa(idade);
        pessoa.tipoSanguineo = tipoSanguineo;
        return pessoa;
    }
}

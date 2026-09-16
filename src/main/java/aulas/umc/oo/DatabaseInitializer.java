package aulas.umc.oo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class DatabaseInitializer {

    public static void main(String[] args) throws Exception {
        run();
    }

    public static void run() throws Exception {
        Properties props = loadProperties();

        String host = props.getProperty("db.host", "localhost");
        int port = Integer.parseInt(props.getProperty("db.port", "5432"));
        String database = props.getProperty("db.name", "bdoo");
        String user = props.getProperty("db.user", "postgres");
        String password = props.getProperty("db.password", "");

        System.out.println("[DatabaseInitializer] Verificando banco PostgreSQL: " + database + " em " + host + ":" + port);
        ensureDatabaseExists(host, port, database, user, password);
        executeSchemaScript(host, port, database, user, password);
        System.out.println("[DatabaseInitializer] Banco e tabelas prontos.");
    }

    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("application.properties não encontrado no classpath.");
            }
            props.load(input);
        }
        return props;
    }

    private static void ensureDatabaseExists(String host, int port, String database, String user, String password) throws SQLException {
        String adminUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";

        try (Connection connection = DriverManager.getConnection(adminUrl, user, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM pg_database WHERE datname = ?")) {
            statement.setString(1, database);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return;
                }
            }
        }

        try (Connection connection = DriverManager.getConnection(adminUrl, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE " + database);
        }
    }

    private static void executeSchemaScript(String host, int port, String database, String user, String password)
            throws IOException, SQLException, URISyntaxException {
        String scriptContent = Files.readString(resolveScriptPath(), StandardCharsets.UTF_8);

        List<String> statements = new ArrayList<>();
        for (String part : scriptContent.split("(?s);") ) {
            String normalized = part.trim();
            if (normalized.isEmpty() || normalized.startsWith("--")) {
                continue;
            }

            String upper = normalized.toUpperCase(Locale.ROOT);
            if (upper.contains("CREATE DATABASE")) {
                continue;
            }

            statements.add(normalized + ";");
        }

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
        }
    }

    private static Path resolveScriptPath() throws URISyntaxException {
        Path rootPath = Paths.get("db_ddl_from_domain.sql");
        if (Files.exists(rootPath)) {
            return rootPath;
        }

        URL resource = Thread.currentThread().getContextClassLoader().getResource("db_ddl_from_domain.sql");
        if (resource != null) {
            return Paths.get(resource.toURI());
        }

        throw new IllegalStateException("Arquivo db_ddl_from_domain.sql não encontrado no diretório raiz nem no classpath.");
    }
}

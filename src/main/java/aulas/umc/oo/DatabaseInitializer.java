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

        String adminHost = props.getProperty("dbadm.host", props.getProperty("db.host", "localhost"));
        int adminPort = Integer.parseInt(props.getProperty("dbadm.port", props.getProperty("db.port", "5432")));
        String adminUser = props.getProperty("dbadm.user", props.getProperty("db.user", "postgres"));
        String adminPassword = props.getProperty("dbadm.password", props.getProperty("db.password", ""));
        String adminDatabase = props.getProperty("dbadm.name", "postgres");

        String targetHost = props.getProperty("db.host", adminHost);
        int targetPort = Integer.parseInt(props.getProperty("db.port", String.valueOf(adminPort)));
        String targetDatabase = props.getProperty("db.name", "bdoo");
        String targetUser = props.getProperty("db.user", adminUser);
        String targetPassword = props.getProperty("db.password", adminPassword);

        System.out.println("[DatabaseInitializer] Verificando banco PostgreSQL: " + targetDatabase + " em " + targetHost + ":" + targetPort);
        ensureDatabaseExists(adminHost, adminPort, adminDatabase, targetDatabase, adminUser, adminPassword);
        executeSchemaScript(targetHost, targetPort, targetDatabase, targetUser, targetPassword);
        validateRequiredTables(targetHost, targetPort, targetDatabase, targetUser, targetPassword);
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

    private static void ensureDatabaseExists(String host, int port, String adminDatabase, String targetDatabase, String user, String password) throws SQLException {
        String adminUrl = "jdbc:postgresql://" + host + ":" + port + "/" + adminDatabase;

        try (Connection connection = DriverManager.getConnection(adminUrl, user, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM pg_database WHERE datname = ?")) {
            statement.setString(1, targetDatabase);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return;
                }
            }
        }

        try (Connection connection = DriverManager.getConnection(adminUrl, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE " + quoteIdentifier(targetDatabase));
        }
    }

    private static void executeSchemaScript(String host, int port, String database, String user, String password)
            throws IOException, SQLException, URISyntaxException {
        String scriptContent = Files.readString(resolveScriptPath(), StandardCharsets.UTF_8);
        List<String> statements = parseSqlStatements(scriptContent);

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
        }
    }

    private static List<String> parseSqlStatements(String scriptContent) {
        String withoutComments = scriptContent
                .replaceAll("(?m)--.*$", "")
                .replaceAll("(?s)/\\*.*?\\*/", " ");

        List<String> statements = new ArrayList<>();
        for (String part : withoutComments.split(";")) {
            String normalized = part.trim();
            if (normalized.isEmpty()) {
                continue;
            }

            String upper = normalized.toUpperCase(Locale.ROOT);
            if (upper.contains("CREATE DATABASE")) {
                continue;
            }

            statements.add(normalized + ";");
        }
        return statements;
    }

    private static void validateRequiredTables(String host, int port, String database, String user, String password) throws SQLException {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT to_regclass('public.pessoa'), to_regclass('public.endereco'), to_regclass('public.documento')")) {
            if (rs.next()) {
                if (rs.getObject(1) == null || rs.getObject(2) == null || rs.getObject(3) == null) {
                    throw new IllegalStateException("Schema do banco incompleto. Tabelas pessoa/endereco/documento não foram criadas no banco " + database);
                }
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

    private static String quoteIdentifier(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }
}

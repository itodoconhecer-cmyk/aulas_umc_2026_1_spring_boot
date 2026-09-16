package aulas.umc.oo.utilities;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility to create a DataSource for PostgreSQL.
 * Requires the PostgreSQL JDBC driver (org.postgresql:postgresql) on the classpath.
 */
public class ConexaoPostGreSQL {

    public static DataSource create(String host, int port, String database, String user, String password) {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerNames(new String[]{host});
        ds.setPortNumbers(new int[]{port});
        ds.setDatabaseName(database);
        ds.setUser(user);
        ds.setPassword(password);
        return ds;
    }

    public static DataSource createFromProperties(Properties props) {
        String host = props.getProperty("db.host", "");
        int port = Integer.parseInt(props.getProperty("db.port", ""));
        String database = props.getProperty("db.name", "");
        String user = props.getProperty("db.user", "");
        String password = props.getProperty("db.password", "");
        return create(host, port, database, user, password);
    }

    public static DataSource createFromClasspathResource(String resourceName) throws IOException {
        Properties props = new Properties();
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IOException("Arquivo de propriedades não encontrado: " + resourceName);
            }
            props.load(in);
        }
        return createFromProperties(props);
    }

    public static DataSource createFromApplicationProperties() throws IOException {
        return createFromClasspathResource("application.properties");
    }
}

package aulas.umc.oo.utilities;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

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
}

package pl.moja.biblioteczka.database.dbuitls;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class DbManager {

    private static final String JDBC_PREFIX = "jdbc:mysql://";
    private static final String DATABASE_HOSTNAME = "127.0.0.1";
    private static final String DATABASE_NAME = "materials";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "admin";

    Connection connection = getConnection();

    public static void printSQLException(SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
        System.out.println(Arrays.toString(e.getStackTrace()));
    }

    public Connection getConnection() {
        Optional<Connection> optionalConnection = getOptionalConnection();
        return optionalConnection.orElse(null);
    }

    private Optional<Connection> getOptionalConnection() {
        if (connection == null) {
            try {
                String connectionString = JDBC_PREFIX + DATABASE_HOSTNAME + "/" + DATABASE_NAME +
                        "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=" + DATABASE_USERNAME + "&" + "password=" + DATABASE_PASSWORD;
                connection = DriverManager.getConnection(connectionString);
                return Optional.ofNullable(connection);
            } catch (SQLException sqlException) {
                printSQLException(sqlException);
                return Optional.ofNullable(connection);
            }

        } else {
            return Optional.of(connection);
        }

    }

}
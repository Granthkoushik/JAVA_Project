import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String USER = System.getProperty("db.user", "root");
    private static final String PASSWORD = System.getProperty("db.password", "");
    private static final String OPTIONS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/" + OPTIONS;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db" + OPTIONS;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Connector/J jar is missing from the classpath.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void setup() throws SQLException {
        try (Connection con = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS library_db");
        }

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS books (id INT PRIMARY KEY AUTO_INCREMENT, title VARCHAR(100) NOT NULL, author VARCHAR(100) NOT NULL, quantity INT NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS members (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, phone VARCHAR(20) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS issues (id INT PRIMARY KEY AUTO_INCREMENT, book_id INT NOT NULL, member_id INT NOT NULL, returned BOOLEAN DEFAULT FALSE, FOREIGN KEY (book_id) REFERENCES books(id), FOREIGN KEY (member_id) REFERENCES members(id))");
        }
    }
}

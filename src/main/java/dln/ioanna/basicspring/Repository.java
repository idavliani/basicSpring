package dln.ioanna.basicspring;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.sqlite.JDBC;

import java.sql.*;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class contains the logic need to connect to the SQLite Database.
 * <p>
 * Method verifyBlockChain() retrieves the entire BlockChains for Database
 * and ChainValidator.isChainValid() method to validate the BlockChain
 */

@Component
public class Repository {

    public void createNewDatabase() {
        String url = "jdbc:sqlite:src/main/resources/testDB.db";
        try {
            Class.forName("org.sqlite.JDBC");

            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection connect() {
        // db parameters
        String url = "jdbc:sqlite:src/main/resources/testDB.db";
        // create a connection to the database
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                //System.out.println("Connection to SQLite has been closed.");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void createNewTable() {
        // db parameters
        String url = "jdbc:sqlite:src/main/resources/testDB.db";
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS testDB (\n" +
                "id integer PRIMARY KEY AUTOINCREMENT, \n" +
                "firstname text, \n" +
                "lastName text, \n" +
                "country text, \n" +
                "address text, \n" +
                "phone text, \n" +
                "age integer) ;";

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insert(Connection conn,
                       String firstname,
                       String lastName,
                       String country,
                       String address,
                       String phone,
                       int age) {
        String sql = "INSERT INTO testDB (firstName, lastName,  country," +
                "address, phone, age) VALUES(?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, country);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, phone);
            preparedStatement.setInt(6, age);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadData(Connection connection) {
        Faker faker = new Faker();
        for (int i = 0; i < 30; i++) {
            insert(connection, faker.name().firstName(),
                    faker.name().lastName(),
                    faker.address().country(),
                    faker.address().streetAddress(),
                    faker.phoneNumber().phoneNumber(),
                    faker.number().numberBetween(1, 99));
        }
    }

    public List<User> getAllUsers(Connection conn) {

        String sql = "SELECT * FROM testDB ORDER BY id DESC";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(7)));
            }
            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
    @PostConstruct
    public void loadDB() {
        Connection connection = connect();
        createNewTable();
        loadData(connection);
        close(connection);
    }
}

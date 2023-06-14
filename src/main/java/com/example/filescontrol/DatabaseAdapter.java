package com.example.filescontrol;

import com.example.filescontrol.FilesControl.User;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Класс для работы с базой данных пользователей.
 */
public class DatabaseAdapter {
    /**
     * URL для подключения к базе данных.
     */
    public static final String DB_URL = "jdbc:h2:/db/projects";
    /**
     * Драйвер для работы с базой данных.
     */
    public static final String DB_Driver = "org.h2.Driver";
    /**
     * Название таблицы пользователей.
     */
    public static final String TableUser="User";

    /**
     * Метод для получения соединения с базой данных и создания таблицы пользователей, если ее нет.
     */
    public static void getDBConnection() {
        try {
            Class.forName(DB_Driver);
            Connection connection = DriverManager.getConnection(DB_URL);
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, TableUser, null);
            //deleteTable(TableUser);
            if (!rs.next()) {
                createTableUser();
            }
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL!");
        }
    }

    /**
     * Метод для удаления таблицы в базе данных.
     * @param Table - название таблицы
     */
    private static void deleteTable(String Table)
    {
        String deleteTableSQL = "DROP TABLE "+Table;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(deleteTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для создания таблицы пользователей в базе данных.
     */
    private static void createTableUser() {
        String createTableSQL = "CREATE TABLE "+ TableUser+ " ("
                + "ID INT NOT NULL auto_increment, "
                + "Name varchar(20) NOT NULL UNIQUE, "
                + "Password varchar(20) NOT NULL, "
                + "PRIMARY KEY (ID) "
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Метод для получения идентификатора пользователя по его имени и паролю.
     * @param Name - имя пользователя
     * @param Password - пароль пользователя
     * @return идентификатор пользователя, если он найден, иначе -1
     */
    public static int getUser(String Name,String Password) {
        String selection = "select * from "+ TableUser+" where Name='"+Name+"' AND Password='"+Password+"'";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * Метод для получения объекта пользователя по его идентификатору.
     * @param id - идентификатор пользователя
     * @return объект пользователя, если он найден, иначе null
     */
    public static User getUser(int id) {
        String selection = "select * from "+ TableUser+" where ID="+id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return new User(rs.getString("Name"),
                            rs.getString("Password"),rs.getInt("ID"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Метод для добавления нового пользователя в базу данных.
     * @param Name - имя пользователя
     * @param Password - пароль пользователя
     * @return идентификатор нового пользователя, если он успешно добавлен, иначе -1
     */
    public static int addUser(User user) {
        String insertTableSQL = "INSERT INTO "+ TableUser
                + " (Name,Password) " + "VALUES "
                + "('"+user.getName()+"','"+user.getPassword()+"')";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableUser+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}

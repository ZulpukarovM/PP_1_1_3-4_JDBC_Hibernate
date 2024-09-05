package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private Util() {
    }

    private static final String URL = "jdbc:mysql://localhost:3306/newdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection connection;

    public static Connection getConnection() {

        try {
            Class.forName(DRIVER);


            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);


        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = getConfiguration();
            configuration.addAnnotatedClass(User.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/newdb");
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.USER, "root");
        settings.put(Environment.PASS, "root");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.HBM2DDL_AUTO, "update");
        configuration.setProperties(settings);
        return configuration;
    }


    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

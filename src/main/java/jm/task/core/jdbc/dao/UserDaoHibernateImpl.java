package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {

        String sql = """
                 CREATE TABLE IF NOT EXISTS `newdb`.`users` (
                  `id` INT NOT NULL AUTO_INCREMENT,
                  `name` VARCHAR(45) NOT NULL,
                  `lastName` VARCHAR(45) NOT NULL,
                  `age` INT NOT NULL,
                  PRIMARY KEY (`id`),
                  UNIQUE INDEX `idusers_UNIQUE` (`id` ASC) VISIBLE)\
                """;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS `newdb`.`users`";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(new User(name, lastName, age));
        transaction.commit();
    }


    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(session.get(User.class, id));
        transaction.commit();

    }

    @Override
    public List<User> getAllUsers() {
        return sessionFactory.openSession().createQuery("from User", User.class).list();
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("delete from User").executeUpdate();
        transaction.commit();
    }
}

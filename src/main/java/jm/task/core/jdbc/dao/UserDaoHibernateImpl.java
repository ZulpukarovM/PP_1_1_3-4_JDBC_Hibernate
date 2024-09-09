package jm.task.core.jdbc.dao;

import jakarta.persistence.Query;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
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
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createNativeQuery(sql).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();

        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }


    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS `newdb`.`users`";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createNativeQuery(sql).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();

        } catch (HibernateException e) {
            throw new HibernateException(e);
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(new User(name, lastName, age));
            transaction.commit();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }


    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.get(User.class, id));
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {

            return session.createQuery("from User", User.class).list();
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }
}

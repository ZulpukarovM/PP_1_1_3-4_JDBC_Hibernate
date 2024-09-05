package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Магомед", "Зулпукаров", (byte) 20);
        userService.saveUser("Абдул", "Ахмедов", (byte) 21);
        userService.saveUser("Ахмедхан", "Ахмедханов", (byte) 23);
        userService.saveUser("Амир", "Сердеров", (byte) 19);
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closeConnection();
        Util.closeSessionFactory();

    }
}

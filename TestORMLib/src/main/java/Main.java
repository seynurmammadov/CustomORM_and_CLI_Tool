import Core.Manager;
import Exceptions.ORMExceptions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ORMExceptions, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Manager manager = new Manager();
        Person p =        manager.findById(Person.class,2);
        p.age=20;
        manager.delete(p);
    }
}

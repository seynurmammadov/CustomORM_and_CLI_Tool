import Core.Manager;
import Exceptions.ORMExceptions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ORMExceptions, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Manager manager = new Manager();
        Person yy =  new Person(500,"Igbal","Surname",22);
        Person ss = new Person();
        ss.id = 850;
        ss.age =50;

        manager.create(ss);
    }
}

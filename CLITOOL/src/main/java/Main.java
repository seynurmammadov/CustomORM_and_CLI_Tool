import az.menu.Menu;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("cli_tool");
        EntityManager em= emf.createEntityManager();
        Menu.menu(em);
    }
}

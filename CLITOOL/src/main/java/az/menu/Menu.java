package az.menu;

import az.exceptions.MyExpeption;
import az.helpers.ConsoleReader;
import az.helpers.Tools;

import javax.persistence.EntityManager;

public class Menu {

    public static void menu(EntityManager em){
        Tools.titlePrint("Console application");
        System.out.println("- 1 Movies");
        System.out.println("- 2 Personal");
        System.out.println("- 0 Get out of the system");
        Tools.printLine();
        try {
            int val = ConsoleReader.readNumber();
            switch (val){
                case 1:
                    MenuMovies.menu(em);
                    break;
                case 2:
                    MenuPersonal.menu(em);
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    throw  new NullPointerException();
            }
        } catch (MyExpeption e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            Tools.errorNotify();
        }
        menu(em);
    }
}

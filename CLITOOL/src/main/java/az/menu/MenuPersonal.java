package az.menu;

import az.exceptions.MyExpeption;
import az.helpers.ConsoleReader;
import az.helpers.Tools;
import az.menu.actions.person.GenderActions;
import az.menu.actions.person.PersonActions;
import az.menu.actions.person.ProfessionActions;

import javax.persistence.EntityManager;

public class MenuPersonal {
    public static void menu(EntityManager entityManager){
        Tools.titlePrint(" Actions for personal");
        System.out.println("- 1 Personal");
        System.out.println("- 2 Gender");
        System.out.println("- 3 Profession");
        System.out.println("- 0 Back");
        Tools.printLine();
        try {
            int val = ConsoleReader.readNumber();
            switch (val){
                case 1:
                    PersonActions.menu(entityManager);
                    break;
                case 2:
                    GenderActions.menu(entityManager);
                    break;
                case 3:
                    ProfessionActions.menu(entityManager);
                    break;
                case 0:
                    break;
            }
        } catch (MyExpeption e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            Tools.errorNotify();
        }
    }

}

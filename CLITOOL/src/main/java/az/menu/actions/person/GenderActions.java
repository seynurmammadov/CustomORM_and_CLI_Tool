package az.menu.actions.person;

import az.exceptions.EmptyStringException;
import az.exceptions.MyExpeption;
import az.exceptions.NegativeNumberException;
import az.exceptions.NoDataFoundException;
import az.helpers.ConsoleReader;
import az.helpers.Tools;
import az.models.person.Gender;
import az.repositories.person.GenderRepo;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class GenderActions {
    public static void menu(EntityManager entityManager) {
        GenderRepo actions = new GenderRepo(entityManager);
        Tools.titlePrint(" Actions for gender");
        System.out.println("- 1 Add new gender");
        if(!Tools.isEmptyCollection(actions.getAll())){
            System.out.println("- 2 Show genders");
            System.out.println("- 3 Delete gender by id");
        }
        System.out.println("- 0 Back");
        Tools.printLine();
        try {
            int val = ConsoleReader.readNumber();
            switch (val) {
                case 1:
                    add(actions);
                    break;
                case 0:
                    break;
            }
            if(!Tools.isEmptyCollection(actions.getAll())){
                switch (val) {
                    case 2:
                        show(actions);
                        break;
                    case 3:
                        delete(actions);
                        break;
                }
            }
        }  catch (MyExpeption e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            Tools.errorNotify();
        }
    }
    public static void add(GenderRepo actions) throws EmptyStringException {
        System.out.println("Enter gender name:");
        String name = ConsoleReader.readString();
        actions.save(Gender.builder().name(name).build());
    }
    public static void show(GenderRepo actions) throws NoDataFoundException {
        List<Gender> genderList =actions.getAll();
        show(genderList);
    }
    public static void delete(GenderRepo actions) throws NoDataFoundException, NegativeNumberException {
        show(actions);
        System.out.println("Enter gender id:");
        long id = ConsoleReader.readLong();
        actions.delete(id);
    }
    public static void show(Collection<Gender> collection) throws NoDataFoundException {
        if (Tools.isEmptyCollection(collection)){
            throw new NoDataFoundException();
        }
        String leftAlignFormat = "|%-17s |%-17s |%n";
        System.out.format("+------------------+------------------+%n");
        System.out.format("|        Id        |       Name       |%n");
        System.out.format("+------------------+------------------+%n");
        int i = 0;
        for (Gender gender: collection) {
            System.out.format(leftAlignFormat,
//                    (i + 1) + ". "+
                    gender.getId(),
                    gender.getName()
            );
            i++;
        }
        System.out.format("+------------------+------------------+%n");
    }
}

package az.menu.actions.person;

import az.exceptions.EmptyStringException;
import az.exceptions.MyExpeption;
import az.exceptions.NegativeNumberException;
import az.exceptions.NoDataFoundException;
import az.helpers.ConsoleReader;
import az.helpers.Tools;
import az.models.person.Profession;
import az.repositories.person.ProfessionRepo;


import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class ProfessionActions {

    public static void menu(EntityManager entityManager) {
        ProfessionRepo actions = new ProfessionRepo(entityManager);
        Tools.titlePrint(" Actions for personal");
        System.out.println("- 1 Add new profession");
        if(actions.getAll().size()!=0){
            System.out.println("- 2 Show professions");
            System.out.println("- 3 Delete profession by id");
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
            if(actions.getAll().size()!=0){
                switch (val) {
                    case 2:
                        show(actions);
                        break;
                    case 3:
                        delete(actions);
                        break;
                }
            }
        } catch (MyExpeption e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            Tools.errorNotify();
        }
    }
    public static void add(ProfessionRepo actions) throws EmptyStringException {
        System.out.println("Enter profession name:");
        String name = ConsoleReader.readString();
        System.out.println("Enter profession description:");
        String description = ConsoleReader.readString();
        actions.save(Profession.builder().name(name).description(description).build());
    }
    public static void show(ProfessionRepo actions) throws NoDataFoundException {
        List<Profession> professionList =actions.getAll();
        show(professionList);
    }
    public static void delete(ProfessionRepo actions) throws NoDataFoundException, NegativeNumberException {
        show(actions);
        System.out.println("Enter profession id:");
        long id = ConsoleReader.readLong();
        actions.delete(id);
    }

    public static void show(Collection<Profession> collection) throws NoDataFoundException {
        if (Tools.isEmptyCollection(collection)){
            throw new NoDataFoundException();
        }
        String leftAlignFormat = "|%-17s |%-17s |%n";
        System.out.format("+------------------+------------------+%n");
        System.out.format("|        Id        |       Name       |%n");
        System.out.format("+------------------+------------------+%n");
        int i = 0;
        for (Profession profession: collection) {
            System.out.format(leftAlignFormat,
//                    (i + 1) + ". "+
                    profession.getId(),
                    profession.getName()
            );
            i++;
        }
        System.out.format("+------------------+------------------+%n");
    }
}
package az.menu.actions.movie;

import az.exceptions.EmptyStringException;
import az.exceptions.MyExpeption;
import az.exceptions.NegativeNumberException;
import az.exceptions.NoDataFoundException;
import az.helpers.ConsoleReader;
import az.helpers.Tools;
import az.models.movie.Language;
import az.repositories.movie.LanguageRepo;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;


public class LanguageActions {
    public static void menu(EntityManager entityManager) {
        LanguageRepo actions = new LanguageRepo(entityManager);
        Tools.titlePrint(" Actions for language");
        System.out.println("- 1 Add new language");
        if(!Tools.isEmptyCollection(actions.getAll())){
            System.out.println("- 2 Show languages");
            System.out.println("- 3 Delete language by id");
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
    public static void add(LanguageRepo actions) throws EmptyStringException {
        System.out.println("Enter language name:");
        String name = ConsoleReader.readString();
        actions.save(Language.builder().name(name).build());
    }
    public static void show(LanguageRepo actions) throws NoDataFoundException {
        List<Language> languageList =actions.getAll();
        show(languageList);
    }
    public static void delete(LanguageRepo actions) throws NoDataFoundException, NegativeNumberException {
        show(actions);
        System.out.println("Enter language id:");
        long id = ConsoleReader.readLong();
        actions.delete(id);
    }
    public static void show(Collection<Language> collection) throws NoDataFoundException {
        if (Tools.isEmptyCollection(collection)){
            throw new NoDataFoundException();
        }
        String leftAlignFormat = "|%-17s |%-17s |%n";
        System.out.format("+------------------+------------------+%n");
        System.out.format("|        Id        |       Name       |%n");
        System.out.format("+------------------+------------------+%n");
        int i = 0;
        for (Language language: collection) {
            System.out.format(leftAlignFormat,
//                    (i + 1) + ". "+
                    language.getId(),
                    language.getName()
            );
            i++;
        }
        System.out.format("+------------------+------------------+%n");
    }
}

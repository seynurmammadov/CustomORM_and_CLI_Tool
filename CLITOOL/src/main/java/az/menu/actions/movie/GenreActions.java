package az.menu.actions.movie;

import az.exceptions.EmptyStringException;
import az.exceptions.MyExpeption;
import az.exceptions.NegativeNumberException;
import az.exceptions.NoDataFoundException;
import az.helpers.ConsoleReader;
import az.helpers.Tools;
import az.models.movie.Genre;
import az.repositories.movie.GenreRepo;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;


public class GenreActions {
    public static void menu(EntityManager entityManager) {
        GenreRepo actions = new GenreRepo(entityManager);
        Tools.titlePrint(" Actions for genre");
        System.out.println("- 1 Add new genre");
        if(!Tools.isEmptyCollection(actions.getAll())){
            System.out.println("- 2 Show genre");
            System.out.println("- 3 Delete genre by id");
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
    public static void add(GenreRepo actions) throws EmptyStringException {
        System.out.println("Enter genre name:");
        String name = ConsoleReader.readString();
        actions.save(Genre.builder().name(name).build());
    }
    public static void show(GenreRepo actions) throws NoDataFoundException {
        List<Genre> genreList =actions.getAll();
        show(genreList);
    }
    public static void delete(GenreRepo actions) throws NoDataFoundException, NegativeNumberException {
        show(actions);
        System.out.println("Enter genre id:");
        long id = ConsoleReader.readLong();
        actions.delete(id);
    }
    public static void show(Collection<Genre> collection) throws NoDataFoundException {
        if (Tools.isEmptyCollection(collection)){
            throw new NoDataFoundException();
        }
        String leftAlignFormat = "|%-17s |%-17s |%n";
        System.out.format("+------------------+------------------+%n");
        System.out.format("|        Id        |       Name       |%n");
        System.out.format("+------------------+------------------+%n");
        int i = 0;
        for (Genre genre: collection) {
            System.out.format(leftAlignFormat,
//                    (i + 1) + ". "+
                    genre.getId(),
                    genre.getName()
            );
            i++;
        }
        System.out.format("+------------------+------------------+%n");
    }
}

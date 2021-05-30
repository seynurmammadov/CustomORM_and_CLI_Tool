package az.menu;

import az.exceptions.MyExpeption;
import az.helpers.ConsoleReader;
import az.helpers.Tools;
import az.menu.actions.movie.GenreActions;
import az.menu.actions.movie.LanguageActions;
import az.menu.actions.movie.MovieActions;

import javax.persistence.EntityManager;

public class MenuMovies {
    public static void menu(EntityManager entityManager){
        Tools.titlePrint(" Actions for movies");
        System.out.println("- 1 Movies");
        System.out.println("- 2 Genre");
        System.out.println("- 3 Languages");
        System.out.println("- 0 Back");
        Tools.printLine();
        try {
            int val = ConsoleReader.readNumber();
            switch (val){
                case 1:
                    MovieActions.menu(entityManager);
                    break;
                case 2:
                    GenreActions.menu(entityManager);
                    break;
                case 3:
                    LanguageActions.menu(entityManager);
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

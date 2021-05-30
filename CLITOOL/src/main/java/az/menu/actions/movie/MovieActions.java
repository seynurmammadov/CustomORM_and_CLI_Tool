package az.menu.actions.movie;

import az.exceptions.*;
import az.helpers.ConsoleReader;
import az.helpers.Tools;
import az.menu.actions.person.PersonActions;
import az.models.movie.Genre;
import az.models.movie.Language;
import az.models.movie.Movie;
import az.models.person.Person;
import az.repositories.movie.GenreRepo;
import az.repositories.movie.LanguageRepo;
import az.repositories.movie.MovieRepo;
import az.repositories.person.PersonRepo;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieActions {
    public static void menu(EntityManager entityManager) throws NotEnoughDataException {
        GenreRepo genreRepo = new GenreRepo(entityManager);
        LanguageRepo languageRepo = new LanguageRepo(entityManager);
        PersonRepo personRepo = new PersonRepo(entityManager);
        boolean directors = personRepo.findByProfession("Director").size() == 0;
        boolean actors = personRepo.findByProfession("Actor").size() == 0;

        if (Tools.isEmptyCollection(genreRepo.getAll()) || Tools.isEmptyCollection(languageRepo.getAll()) || directors || actors) {
            throw new NotEnoughDataException();
        }
        MovieRepo actions = new MovieRepo(entityManager);
        Tools.titlePrint(" Actions for movie");
        System.out.println("- 1 Add new movie");
        if (!Tools.isEmptyCollection(actions.getAll())) {
            System.out.println("- 2 Edit and delete");
            System.out.println("- 3 Show all movies");
            System.out.println("- 4 Show movie by genre");
        }
        System.out.println("- 0 Back");
        Tools.printLine();
        try {
            int val = ConsoleReader.readNumber();
            switch (val) {
                case 1:
                    add(actions, languageRepo, genreRepo, personRepo);
                    break;
                case 0:
                    break;
            }
            if (!Tools.isEmptyCollection(actions.getAll())) {
                switch (val) {
                    case 2:
                        search(actions,languageRepo,personRepo,genreRepo);
                        break;
                    case 3:
                        show(actions);
                        break;
                    case 4:
                        showByGenres(actions,languageRepo,personRepo,genreRepo);
                        break;
                }
            }
        } catch (MyExpeption e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            Tools.errorNotify();
        }
    }

    public static void add(MovieRepo actions, LanguageRepo languageRepo, GenreRepo genreRepo, PersonRepo personRepo) throws IncorrectDateTypeException, NoDataFoundException, EmptyStringException, NegativeNumberException, EmailValidateException, PhoneValidateException {
        System.out.println("Enter movie name:");
        String name = ConsoleReader.readString();
        System.out.println("Enter movie description:");
        String description = ConsoleReader.readString();
        System.out.println("Enter movie age restriction:");
        int age_restriction = ConsoleReader.readNumber();
        System.out.println("Enter movie release date (31-12-2021):");
        LocalDate release_date = ConsoleReader.readDate(ConsoleReader.readString());
        System.out.println("Enter movie publisher company name:");
        String publisher_company = ConsoleReader.readString();
        System.out.println("Enter movie box office:");
        long box_office = ConsoleReader.readLong();
        System.out.println("Enter movie creator country name:");
        String country = ConsoleReader.readString();

        List<Language> languages = setLanguages(languageRepo);
        List<Genre> genres = setGenres(genreRepo);
        List<Person> actors = setPerson(personRepo, "Actor");
        List<Person> directors = setPerson(personRepo, "Director");
        actors.addAll(directors);
        Movie movie = Movie.builder().name(name).persons(actors).age_restriction(age_restriction)
                .box_office(box_office).description(description).release_date(release_date).publisher_company(publisher_company)
                .country(country).languages(languages).genres(genres).build();
        actions.save(movie);

    }

    public static void showByGenres(MovieRepo actions,LanguageRepo languageRepo,PersonRepo personRepo,GenreRepo genreRepo) throws NegativeNumberException, IncorrectDateTypeException, NoDataFoundException, EmptyStringException, EmailValidateException, PhoneValidateException {
        GenreActions.show(genreRepo);
        System.out.println("Select genre by id:");
        long id = ConsoleReader.readNumber();
        Genre genre = genreRepo.findById(id);
        show(genre.getMovies());
        editNdelete(actions,languageRepo,personRepo,genreRepo);
    }

    public static void search(MovieRepo actions,LanguageRepo languageRepo,PersonRepo personRepo,GenreRepo genreRepo) throws NoDataFoundException, IncorrectDateTypeException, EmptyStringException, NegativeNumberException, EmailValidateException, PhoneValidateException {
        Tools.titlePrint("Search by name");
        System.out.println("Enter name:");
        String name = ConsoleReader.readString();
        List<Movie> p = actions.findByName(name);
        show(p);
        editNdelete(actions,languageRepo,personRepo,genreRepo);
    }

    public static void editNdelete(MovieRepo actions,LanguageRepo languageRepo,PersonRepo personRepo,GenreRepo genreRepo) throws IncorrectDateTypeException, NoDataFoundException, NegativeNumberException, EmptyStringException {
        System.out.println("Enter movie id:");
        long id = ConsoleReader.readLong();
        System.out.println("What do you want to do?");
        System.out.println("1. Edit");
        System.out.println("2. Delete");
        int val = ConsoleReader.readNumber();
        switch (val) {
            case 1:
                edit(actions, languageRepo, personRepo,genreRepo, id);
                break;
            case 2:
                delete(actions, id);
                break;
            default:
                System.out.println("We return you to main menu!");
        }
    }

    public static void edit(MovieRepo actions,LanguageRepo languageRepo,PersonRepo personRepo,GenreRepo genreRepo, long id) throws IncorrectDateTypeException, NoDataFoundException, EmptyStringException, NegativeNumberException {
        Movie movie = actions.findById(id);
        System.out.println("What do you want edit?");
        System.out.println("1. Name");
        System.out.println("2. Age Restriction");
        System.out.println("3. Description");
        System.out.println("4. Release date");
        System.out.println("5. Publisher Company");
        System.out.println("6. Box Office");
        System.out.println("7. Country");
        System.out.println("8. Languages");
        System.out.println("9. Genres:");
        System.out.println("10. Actors:");
        System.out.println("11. Directors:");
        int edit = ConsoleReader.readNumber();
        switch (edit) {
            case 1:
                System.out.println("Enter movie name:");
                String name = ConsoleReader.readString();
                movie.setName(name);
                break;
            case 2:
                System.out.println("Enter movie age restriction:");
                int age_restriction = ConsoleReader.readNumber();
                movie.setAge_restriction(age_restriction);
                break;
            case 3:
                System.out.println("Enter movie description:");
                String description = ConsoleReader.readString();
                movie.setDescription(description);
                break;
            case 4:
                System.out.println("Enter movie release date (31-12-2021):");
                LocalDate release_date = ConsoleReader.readDate(ConsoleReader.readString());
                movie.setRelease_date(release_date);
                break;
            case 5:
                System.out.println("Enter movie publisher company name:");
                String publisher_company = ConsoleReader.readString();
                movie.setPublisher_company(publisher_company);
                break;
            case 6:
                System.out.println("Enter movie box office:");
                long box_office = ConsoleReader.readLong();
                movie.setBox_office(box_office);
                break;
            case 7:
                System.out.println("Enter movie creator country name:");
                String country = ConsoleReader.readString();
                movie.setCountry(country);
                break;
            case 8:
                List<Language> languages = setLanguages(languageRepo);
                movie.setLanguages(languages);
                break;
            case 9:
                List<Genre> genres = setGenres(genreRepo);
                movie.setGenres(genres);
                break;
            case 10:
                List<Person> actors = setPerson(personRepo, "Actor");
                List<Person> personList =movie.getPersons();
                personList.removeIf(p-> p.getProfession().getName().equals("Actor"));
                actors.addAll(personList);
                movie.setPersons(actors);
                break;
            case 11:
                List<Person> directors = setPerson(personRepo, "Director");
                List<Person> persons =movie.getPersons();
                persons.removeIf(p-> p.getProfession().getName().equals("Director"));
                directors.addAll(persons);
                movie.setPersons(directors);
                break;
            default:
                System.out.println("We return you to main menu!");
                break;
        }
        actions.save(movie);
    }

    public static void delete(MovieRepo actions, long id) throws NoDataFoundException, EmptyStringException {
        System.out.println("Are you sure?(y/n)");
        String y = ConsoleReader.readString();
        if ("y".equals(y)) {
            actions.delete(id);
        } else {
            System.out.println("We return you to main menu!");
        }
    }

    public static void show(MovieRepo actions) throws NoDataFoundException, NegativeNumberException {
        List<Movie> movies = actions.getAll();
        show(movies);
        System.out.println("Enter movie id to check details or enter 0 to leave:");
        long id = ConsoleReader.readLong();
        showDetails(actions.findById(id));

    }

    public static void show(Collection<Movie> collection) throws NoDataFoundException {
        if (Tools.isEmptyCollection(collection)) {
            throw new NoDataFoundException();
        }
        String leftAlignFormat = "|%-17s |%-17s |%-17s |%-17s |%-17s |%-17s |%-17s |%-17s |%n";
        System.out.format("+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+%n");
        System.out.format("|        Id        |       Name       |  Age Restriction |    Description   |   Release Date   |      Company     |    Box Office    |     Country      |%n");
        System.out.format("+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+%n");
        for (Movie movie : collection) {
            System.out.format(leftAlignFormat,
                    movie.getId(),
                    movie.getName(),
                    movie.getAge_restriction(),
                    movie.getDescription(),
                    movie.getRelease_date(),
                    movie.getPublisher_company(),
                    movie.getBox_office(),
                    movie.getCountry()
            );
        }
        System.out.format("+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+%n");
    }

    public static void showDetails(Movie movie) throws NoDataFoundException {
        Tools.titlePrint("Movie Details");
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        show(movies);
        Tools.titlePrint("Genres");
        GenreActions.show(movie.getGenres());
        Tools.titlePrint("Languages");
        LanguageActions.show(movie.getLanguages());
        Tools.titlePrint("Directors and Actors");
        PersonActions.show(movie.getPersons());
    }

    public static List<Language> setLanguages(LanguageRepo languageRepo) throws NoDataFoundException, NegativeNumberException, EmptyStringException {
        List<Language> languageList = languageRepo.getAll();
        List<Language> languageAdd = new ArrayList<>();
        String lang;
        while (true) {
            LanguageActions.show(languageList);
            System.out.println("Enter movie language id:");
            long language_id = ConsoleReader.readNumber();
            Language language = languageList.stream()
                    .filter(language1 -> language1.getId() == language_id)
                    .findAny()
                    .orElse(null);
            languageAdd.add(language);
            languageList.remove(language);
            if (languageList.size() != 0) {
                System.out.println("Do you want add one more language?(y/n)");
                lang = ConsoleReader.readString();
                if (!lang.equals("y")) {
                    break;
                }
            } else break;
        }
        return languageAdd;
    }

    public static List<Genre> setGenres(GenreRepo genreRepo) throws NoDataFoundException, NegativeNumberException, EmptyStringException {
        List<Genre> genreList = genreRepo.getAll();
        List<Genre> genreAdd = new ArrayList<>();
        String gen;
        while (true) {
            GenreActions.show(genreList);
            System.out.println("Enter movie genre id:");
            long language_id = ConsoleReader.readNumber();
            Genre genre = genreList.stream()
                    .filter(language1 -> language1.getId() == language_id)
                    .findAny()
                    .orElse(null);
            genreAdd.add(genre);
            genreList.remove(genre);
            if (genreList.size() != 0) {
                System.out.println("Do you want add one more genre?(y/n)");
                gen = ConsoleReader.readString();
                if (!gen.equals("y")) {
                    break;
                }
            } else break;
        }
        return genreAdd;
    }

    public static List<Person> setPerson(PersonRepo personRepo, String name) throws NoDataFoundException, NegativeNumberException, EmptyStringException {
        List<Person> personList = personRepo.findByProfession(name);
        List<Person> personAdd = new ArrayList<>();
        String per;
        while (true) {
            PersonActions.show(personList);
            System.out.println("Enter movie " + name + " id:");
            long language_id = ConsoleReader.readNumber();
            Person person = personList.stream()
                    .filter(language1 -> language1.getId() == language_id)
                    .findAny()
                    .orElse(null);
            personAdd.add(person);
            personList.remove(person);
            if (personList.size() != 0) {
                System.out.println("Do you want add one more " + name + "s?(y/n)");
                per = ConsoleReader.readString();
                if (!per.equals("y")) {
                    break;
                }
            } else break;
        }
        return personAdd;
    }
}

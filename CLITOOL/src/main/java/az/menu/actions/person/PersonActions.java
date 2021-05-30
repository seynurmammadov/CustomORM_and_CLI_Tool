package az.menu.actions.person;

import az.exceptions.*;
import az.helpers.ConsoleReader;
import az.helpers.Tools;
import az.models.person.Gender;
import az.models.person.Person;
import az.models.person.PersonDetail;
import az.models.person.Profession;
import az.repositories.person.GenderRepo;
import az.repositories.person.PersonRepo;
import az.repositories.person.ProfessionRepo;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class PersonActions {
    public static void menu(EntityManager entityManager) throws NotEnoughDataException {
        GenderRepo actionsGender = new GenderRepo(entityManager);
        ProfessionRepo actionsProfession = new ProfessionRepo(entityManager);

        if(Tools.isEmptyCollection(actionsGender.getAll())||Tools.isEmptyCollection(actionsProfession.getAll())){
            throw new NotEnoughDataException();
        }
        PersonRepo actions = new PersonRepo(entityManager);
        Tools.titlePrint(" Actions for personal");
        System.out.println("- 1 Add new person");
        if(!Tools.isEmptyCollection(actions.getAll())){
            System.out.println("- 2 Edit and delete");
            System.out.println("- 3 Show all persons");
            System.out.println("- 4 Show persons by professions");
        }
        System.out.println("- 0 Back");
        Tools.printLine();
        try {
            int val = ConsoleReader.readNumber();
            switch (val) {
                case 1:
                    add(actions,actionsGender,actionsProfession);
                    break;
                case 0:
                    break;
            }
            if(!Tools.isEmptyCollection(actions.getAll())){
                switch (val) {
                    case 2:
                        search(actions,actionsGender,actionsProfession);
                        break;
                    case 3:
                        show(actions);
                        break;
                    case 4:
                        showByProfessions(actions,actionsGender,actionsProfession);
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
    public static void add(PersonRepo actions,GenderRepo genderRepo,ProfessionRepo professionRepo) throws IncorrectDateTypeException, NoDataFoundException, EmptyStringException, NegativeNumberException, EmailValidateException, PhoneValidateException {
        System.out.println("Enter person name:");
        String name = ConsoleReader.readString();
        System.out.println("Enter person surname:");
        String surname = ConsoleReader.readString();
        System.out.println("Enter person lastname:");
        String lastname = ConsoleReader.readString();
        System.out.println("Enter person birthdate(31-12-2021):");
        LocalDate date = ConsoleReader.readDate(ConsoleReader.readString());

        GenderActions.show(genderRepo);
        System.out.println("Enter gender id:");
        long gender_id = ConsoleReader.readNumber();
        Gender gender= genderRepo.findById(gender_id);

        ProfessionActions.show(professionRepo);
        System.out.println("Enter profession id:");
        long profession_id = ConsoleReader.readNumber();
        Profession pf= professionRepo.findById(profession_id);

        System.out.println("Enter person address:");
        String address = ConsoleReader.readString();
        System.out.println("Enter person phone number:");
        String phonenumber = ConsoleReader.readPhone();
        System.out.println("Enter person email:");
        String email = ConsoleReader.readEmail();

        PersonDetail pd= PersonDetail.builder().address(address).phone(phonenumber).email(email).build();
        Person p = Person.builder().name(name).surname(surname).lastname(lastname).birthdate(date).gender(gender).profession(pf).person_detail(pd).build();
        actions.save(p);
    }
    public static void showByProfessions(PersonRepo actions,GenderRepo genderRepo,ProfessionRepo professionRepo) throws NegativeNumberException, IncorrectDateTypeException, NoDataFoundException, EmptyStringException, EmailValidateException, PhoneValidateException {
        ProfessionActions.show(professionRepo);
        System.out.println("Select profession by id:");
        long id = ConsoleReader.readNumber();
        Profession pf=professionRepo.findById(id);
        show(pf.getPersons());
        editNdelete(actions,genderRepo,professionRepo);
    }
    public static void search(PersonRepo actions,GenderRepo genderRepo,ProfessionRepo professionRepo) throws NoDataFoundException, IncorrectDateTypeException, EmptyStringException, NegativeNumberException, EmailValidateException, PhoneValidateException {
        Tools.titlePrint("Search by name");
        System.out.println("Enter name:");
        String name= ConsoleReader.readString();
        List<Person> p = actions.findByName(name);
        show(p);
       editNdelete(actions,genderRepo,professionRepo);
    }
    public static void editNdelete(PersonRepo actions,GenderRepo genderRepo,ProfessionRepo professionRepo) throws IncorrectDateTypeException, NoDataFoundException, NegativeNumberException, EmptyStringException, EmailValidateException, PhoneValidateException {
        System.out.println("Enter person id:");
        long id = ConsoleReader.readLong();
        System.out.println("What do you want to do?");
        System.out.println("1. Edit");
        System.out.println("2. Delete");
        int val = ConsoleReader.readNumber();
        switch (val){
            case 1:
                edit(actions,genderRepo,professionRepo,id);
                break;
            case 2:
                delete(actions,id);
                break;
            default:
                System.out.println("We return you to main menu!");
        }
    }

    public static void edit(PersonRepo actions,GenderRepo genderRepo,ProfessionRepo professionRepo,long id) throws IncorrectDateTypeException, NoDataFoundException, EmptyStringException, NegativeNumberException, PhoneValidateException, EmailValidateException {
        Person person = actions.findById(id);
        System.out.println("What do you want edit?");
        System.out.println("1. Name");
        System.out.println("2. Surname");
        System.out.println("3. Lastname");
        System.out.println("4. Birthdate");
        System.out.println("5. Gender");
        System.out.println("6. Profession");
        System.out.println("7. Address");
        System.out.println("8. Phone number");
        System.out.println("9. Email:");
        int edit = ConsoleReader.readNumber();
        switch (edit){
            case 1:
                System.out.println("Enter person name:");
                String p_name = ConsoleReader.readString();
                person.setName(p_name);
                break;
            case 2:
                System.out.println("Enter person surname:");
                String p_surname = ConsoleReader.readString();
                person.setSurname(p_surname);
                break;
            case 3:
                System.out.println("Enter person lastname:");
                String p_lastname = ConsoleReader.readString();
                person.setLastname(p_lastname);
                break;
            case 4:
                System.out.println("Enter person birthdate(31-12-2021):");
                LocalDate date = ConsoleReader.readDate(ConsoleReader.readString());
                person.setBirthdate(date);
                break;
            case 5:
                GenderActions.show(genderRepo);
                System.out.println("Enter gender id:");
                long gender_id = ConsoleReader.readNumber();
                Gender gender= genderRepo.findById(gender_id);
                person.setGender(gender);
                break;
            case 6:
                ProfessionActions.show(professionRepo);
                System.out.println("Enter profession id:");
                long profession_id = ConsoleReader.readNumber();
                Profession pf= professionRepo.findById(profession_id);
                person.setProfession(pf);
                break;
            case 7:
                System.out.println("Enter person address:");
                String address = ConsoleReader.readString();
                person.getPerson_detail().setAddress(address);
                break;
            case 8:
                System.out.println("Enter person phone number:");
                String phonenumber = ConsoleReader.readPhone();
                person.getPerson_detail().setPhone(phonenumber);
                break;
            case 9:
                System.out.println("Enter person email:");
                String email = ConsoleReader.readEmail();
                person.getPerson_detail().setEmail(email);
                break;
            default:
                System.out.println("We return you to main menu!");
                break;
        }
        actions.save(person);
    }
    public static void delete(PersonRepo actions,long id) throws NoDataFoundException, EmptyStringException {
        System.out.println("Are you sure?(y/n)");
        String y = ConsoleReader.readString();
        if ("y".equals(y)) {
            actions.delete(id);
        } else {
            System.out.println("We return you to main menu!");
        }
    }
    public static void show(PersonRepo actions) throws NoDataFoundException {
        List<Person> personList =actions.getAll();
        show(personList);
    }
    public static void show(Collection<Person> collection) throws NoDataFoundException {
        if (Tools.isEmptyCollection(collection)){
            throw new NoDataFoundException();
        }
        String leftAlignFormat = "|%-17s |%-17s |%-17s |%-17s |%-17s |%-17s |%-17s |%-17s |%-27s |%-17s |%n";
        System.out.format("+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+----------------------------+------------------+%n");
        System.out.format("|        Id        |       Name       |      Surname     |      Lastname    |     Bitrhdate    |      Gender      |    Profession    |     Address      |            Email           |   Phone Number   |%n");
        System.out.format("+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+----------------------------+------------------+%n");
        int i = 0;
        for (Person person: collection) {
            System.out.format(leftAlignFormat,
//                    (i + 1) + ". "+
                    person.getId(),
                    person.getName(),
                    person.getSurname(),
                    person.getLastname(),
                    person.getBirthdate(),
                    person.getGender().getName(),
                    person.getProfession().getName(),
                    person.getPerson_detail().getAddress(),
                    person.getPerson_detail().getEmail(),
                    person.getPerson_detail().getPhone()
            );
            i++;
        }
        System.out.format("+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+----------------------------+------------------+%n");

    }
}

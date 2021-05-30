import Annotations.MyColumn;
import Annotations.MyEntity;
import Annotations.MyID;

@MyEntity("persons")
public class Person {
    @MyID
    int id;
    @MyColumn
    String name;
    @MyColumn
    String surname;
    @MyColumn
    int age;

    public Person(){

    }
    public Person(int id, String name, String surname, int age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }
}


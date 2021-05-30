package az.repositories.person;

import az.exceptions.NoDataFoundException;
import az.interfaces.ICRUD;
import az.models.person.Person;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class PersonRepo implements ICRUD<Person> {
    private EntityManager entityManager;
    public PersonRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Person findById(long id) throws NoDataFoundException {
        Person person = entityManager.find(Person.class, id);
        if(person==null){
            throw new NoDataFoundException();
        }
        return person;
    }
    @Override
    public List<Person> getAll() {
        List<Person> p ;
        try {
            p = entityManager.createNamedQuery("Persons.getAll",Person.class).getResultList();
        }
        catch (Exception e){
            return new ArrayList<>();
        }
        return p;
    }
    @Override
    public void delete(long id) throws NoDataFoundException {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(findById(id));
            entityManager.getTransaction().commit();
            System.out.println("DELETED!");
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new NoDataFoundException();
        }
    }
    @Override
    public void save(Person person) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(person);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
    @Override
    public List<Person> findByName(String name) {
        List<Person> p ;
        try {
            p = entityManager.createNamedQuery("Persons.findByName", Person.class)
                    .setParameter("name", name).getResultList();
        }
        catch (Exception e){
            return new ArrayList<>();
        }
        return p;
    }
    public List<Person> findByProfession(String name) {
        List<Person> p ;
        try {
            p = entityManager.createNamedQuery("Persons.findByProfession", Person.class)
                    .setParameter("name", name).getResultList();
        }
        catch (Exception e){
            return new ArrayList<>();
        }
        return p;
    }
}

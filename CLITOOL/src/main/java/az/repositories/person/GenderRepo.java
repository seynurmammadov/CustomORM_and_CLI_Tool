package az.repositories.person;

import az.exceptions.NoDataFoundException;
import az.interfaces.ISimpleCRUD;
import az.models.person.Gender;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class GenderRepo implements ISimpleCRUD<Gender> {
    private EntityManager entityManager;
    public GenderRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Gender findById(long id) throws NoDataFoundException {
        Gender gender = entityManager.find(Gender.class, id);
        if(gender==null){
            throw new NoDataFoundException();
        }
        return gender;
    }
    @Override
    public List<Gender> getAll() {
        List<Gender> g ;
        try {
           g= entityManager.createNamedQuery("Gender.getAll",Gender.class).getResultList();
        }
        catch (Exception e){
            return new ArrayList<>();
        }
        return g;
    }
    @Override
    public void delete(long id) throws NoDataFoundException {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(findById(id));
            entityManager.getTransaction().commit();
            System.out.println("DELETED!");
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new NoDataFoundException();
        }
    }
    @Override
    public void save(Gender gender) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(gender);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}

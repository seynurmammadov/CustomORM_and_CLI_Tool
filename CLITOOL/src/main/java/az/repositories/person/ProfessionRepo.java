package az.repositories.person;

import az.exceptions.NoDataFoundException;
import az.interfaces.ICRUD;
import az.interfaces.ISimpleCRUD;
import az.models.person.Person;
import az.models.person.Profession;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfessionRepo implements ISimpleCRUD<Profession> {
    private EntityManager entityManager;
    public ProfessionRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Profession findById(long id) throws NoDataFoundException {
        Profession profession = entityManager.find(Profession.class, id);
        if(profession==null){
            throw new NoDataFoundException();
        }
        return profession;
    }
    @Override
    public List<Profession> getAll() {
        List<Profession> p ;
        try {
           p= entityManager.createNamedQuery("Professions.getAll",Profession.class).getResultList();
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
            System.out.println("DELETED!");
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new NoDataFoundException();
        }
    }
    @Override
    public void save(Profession profession) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(profession);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}

package az.repositories.movie;

import az.exceptions.NoDataFoundException;
import az.interfaces.ISimpleCRUD;
import az.models.movie.Language;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class LanguageRepo  implements ISimpleCRUD<Language> {
    private EntityManager entityManager;
    public LanguageRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Language findById(long id) throws NoDataFoundException {
        Language language = entityManager.find(Language.class, id);
        if(language==null){
            throw new NoDataFoundException();
        }
        return language;
    }
    @Override
    public List<Language> getAll() {
        List<Language> languages ;
        try {
            languages= entityManager.createNamedQuery("Languages.getAll",Language.class).getResultList();
        }
        catch (Exception e){
            return new ArrayList<>();
        }
        return languages;
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
    public void save(Language language) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(language);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}

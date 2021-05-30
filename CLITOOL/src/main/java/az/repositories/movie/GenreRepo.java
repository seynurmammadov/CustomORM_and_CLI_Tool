package az.repositories.movie;

import az.exceptions.NoDataFoundException;
import az.interfaces.ISimpleCRUD;
import az.models.movie.Genre;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class GenreRepo implements ISimpleCRUD<Genre> {
    private EntityManager entityManager;
    public GenreRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Genre findById(long id) throws NoDataFoundException {
        Genre genre = entityManager.find(Genre.class, id);
        if(genre==null){
            throw new NoDataFoundException();
        }
        return genre;
    }
    @Override
    public List<Genre> getAll() {
        List<Genre> g ;
        try {
            g= entityManager.createNamedQuery("Genre.getAll",Genre.class).getResultList();
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
    public void save(Genre genre) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(genre);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}

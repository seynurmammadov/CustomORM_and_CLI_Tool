package az.repositories.movie;

import az.exceptions.NoDataFoundException;
import az.interfaces.ICRUD;
import az.models.movie.Movie;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class MovieRepo implements ICRUD<Movie> {
    private EntityManager entityManager;
    public MovieRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Movie findById(long id) throws NoDataFoundException {
        Movie movie = entityManager.find(Movie.class, id);
        if(movie==null){
            throw new NoDataFoundException();
        }
        return movie;
    }
    @Override
    public List<Movie> getAll() {
        List<Movie> movies ;
        try {
            movies = entityManager.createNamedQuery("Movies.getAll",Movie.class).getResultList();
        }
        catch (Exception e){
            return new ArrayList<>();
        }
        return movies;
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
    public void save(Movie movie) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(movie);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
    @Override
    public List<Movie> findByName(String name) {
        List<Movie> movies ;
        try {
            movies = entityManager.createNamedQuery("Movies.findByName", Movie.class)
                    .setParameter("name", name).getResultList();
        }
        catch (Exception e){
            return new ArrayList<>();
        }
        return movies;
    }
}

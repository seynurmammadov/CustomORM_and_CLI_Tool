package az.interfaces;


import az.exceptions.NoDataFoundException;

import java.util.List;

public interface ISimpleCRUD<T> {
    T findById(long id)  throws NoDataFoundException;
    List<T> getAll();
    void delete(long id) throws NoDataFoundException;
    void save(T value);
}

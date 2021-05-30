package az.interfaces;

import az.exceptions.NoDataFoundException;

import java.util.List;

public interface ICRUD<T> extends ISimpleCRUD<T>{
    List<T> findByName(String name) throws NoDataFoundException;
}

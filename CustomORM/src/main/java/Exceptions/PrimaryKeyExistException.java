package Exceptions;

public class PrimaryKeyExistException extends ORMExceptions{
    public PrimaryKeyExistException() {
        super("Object with this id already exist!");
    }
}

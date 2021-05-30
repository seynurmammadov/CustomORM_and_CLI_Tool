package Exceptions;

public class ClassHasNoIdException extends ORMExceptions{
    public ClassHasNoIdException(String message) {
        super(message+" class has no id");
    }
}
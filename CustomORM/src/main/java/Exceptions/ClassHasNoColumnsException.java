package Exceptions;

public class ClassHasNoColumnsException extends ORMExceptions{
    public ClassHasNoColumnsException(String message) {
        super(message+" class has no column");
    }
}
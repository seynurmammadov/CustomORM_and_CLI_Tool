package Exceptions;

public class ClassNotEntityException  extends ORMExceptions{
    public ClassNotEntityException(String message) {
        super(message+" class is not entity");
    }
}

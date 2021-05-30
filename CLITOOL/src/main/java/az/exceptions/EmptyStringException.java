package az.exceptions;

public class EmptyStringException extends MyExpeption{
    public EmptyStringException() {
        super("String is empty!");
    }
}
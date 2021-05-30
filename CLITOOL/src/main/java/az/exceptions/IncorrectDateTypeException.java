package az.exceptions;

public class IncorrectDateTypeException extends MyExpeption{
    public IncorrectDateTypeException() {
        super("Incorrect date type format!");
    }
}
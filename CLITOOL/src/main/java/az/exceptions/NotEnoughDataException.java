package az.exceptions;

public class NotEnoughDataException extends MyExpeption{
    public NotEnoughDataException() {
        super("Not Enough Data found!");
    }
}
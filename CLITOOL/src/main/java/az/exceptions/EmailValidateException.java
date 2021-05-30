package az.exceptions;

public class EmailValidateException extends MyExpeption{
    public EmailValidateException() {
        super("Email is invalid!");
    }
}

package az.exceptions;

public class PhoneValidateException extends MyExpeption{
    public PhoneValidateException() {
        super("Phone number is invalid!");
    }
}
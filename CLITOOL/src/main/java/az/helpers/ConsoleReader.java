package az.helpers;

import az.exceptions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConsoleReader {
    public static void checkString(String val) throws EmptyStringException {
        if(val.length()==0){
            throw new EmptyStringException();
        }
    }
    public static String readString() throws EmptyStringException {
        Scanner scanner = Tools.generateScanner();
        String val = scanner.nextLine().trim();
        checkString(val);
        return val;
    }
    public static String readEmail() throws EmptyStringException, EmailValidateException {
        String val= readString();
        if (val.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return val;
        }
        throw new EmailValidateException();
    }
    public static String readPhone() throws EmptyStringException, PhoneValidateException {
        String val= readString();
         if(val.matches("\\d{10,12}")){
           return val;
         };
        throw new PhoneValidateException();
    }
    public static boolean isPositive(int num){
        return num >= 0;
    }
    public static boolean isPositive(long num){
        return num >= 0;
    }
    public static int readNumber() throws NegativeNumberException {
        Scanner scanner = Tools.generateScanner();
        int num= scanner.nextInt();
        if(!isPositive(num)) throw new NegativeNumberException();
        return num;
    }
    public static long readLong() throws NegativeNumberException {
        Scanner scanner = Tools.generateScanner();
        long num= scanner.nextLong();
        if(!isPositive(num)) throw new NegativeNumberException();
        return num;
    }
    public static LocalDate readDate(String str) throws IncorrectDateTypeException {
        DateTimeFormatter format= DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            return LocalDate.parse(str,format);
        }
        catch(Exception e){
           throw new IncorrectDateTypeException();
        }
    }
    public static int readIndex() throws NegativeNumberException {
        Scanner scanner = Tools.generateScanner();
        int index= scanner.nextInt()-1;
        if(!ConsoleReader.isPositive(index)) throw  new NegativeNumberException();
        return index;
    }
}

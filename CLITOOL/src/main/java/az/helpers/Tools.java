package az.helpers;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

public class Tools {
    public static void errorNotify()  {
        System.out.println("Incorrect value! We return you to the menu! \nPress enter to continue!");
        sleep();
    }
    public static void sleep()  {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void titlePrint(String title){
        StringBuilder stringBuilder = new StringBuilder(title);
        int c=76-title.length();
        for (int i=0;i<(c/2);i++){
            stringBuilder.insert(0,"-");
        }
        for (int i=0;i<(c/2);i++){
            stringBuilder.insert(stringBuilder.length(),"-");
        }
        if(stringBuilder.length()<76){
            stringBuilder.insert(stringBuilder.length(),"-");
        }
        printLine();
        System.out.println(stringBuilder);
        printLine();

    }
    public static void printLine(){
        System.out.println("----------------------------------------------------------------------------");
    }
    public static Scanner generateScanner(){
        return new Scanner(System.in);
    }
    public static boolean isEmptyCollection(Collection collection){
        return collection.size()==0;
    }
}

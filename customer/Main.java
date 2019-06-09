import java.util.Scanner;
import java.sql.* ;

public class Main {
    public static boolean connected = false;
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Func func;
        IO.overallUsage();
        do {
            System.out.printf("\n$: ");
            func = IO.read_input(scanner);
        } while(func != Func.QUIT);
    }
}

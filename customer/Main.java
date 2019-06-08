import java.util.Scanner;
import java.sql.* ;

public class Main {
//    public static final int CONNECT = 0;
//    public static final int SEARCH = 1;
//    public static final int SHOW = 2;
//    public static final int RESERVE = 3;
//    public static final int UPDATE = 4;
//    public static final int CANCEL = 5;
//    public static final int HISTORY = 6;
//    public static final int QUIT = 7;
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

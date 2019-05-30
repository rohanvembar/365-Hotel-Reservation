import java.util.Scanner;


public class IO {
    public static void overallUsage() {
        System.out.printf(
                "-----------------------------------------------------------\n" +
                "                          USAGE                        \n" +
                "-----------------------------------------------------------\n" +
                "connect\n" +
                "	connect to reservation database\n\n" +
                "search [-checkin yyyy/mm/dd] [-checkout yyyy/mm/ddd]\n" +
                "       [-beds x] [-decor x] [-occupants x]\n" +
                "	search for rooms fitting specified requirements\n\n" +
                "show yyyy/mm/dd\n" +
                "	show the availability for each room on the specified\n" +
                "	day\n\n" +
                "reserve -user user_id -card card_num -room room_id\n" +
                "        -checkin yyyy/mm/dd -checkout yyyy/mm/dd\n" +
                "        -adults num_adults\n" +
                "	reserve a room\n\n" +
                "update -res reservation_num [-user user_id]\n" +
                "       [-card card_num] [-room room_id]\n" +
                "       [-checkin yyyy/mm/dd] [-checkout yyyy/mm/dd]\n" +
                "       [-adults x]\n" +
                "	update reservation\n\n" +
                "cancel -res reservation_num\n" +
                "	cancel reservation\n\n" +
                "history user_id\n" +
                "	display active and past reservations\n\n" +
                "quit\n" +
                "	disconnect from server\n" +
                "-----------------------------------------------------------\n");
    }

    public static Func read_input(Scanner scanner) {
        String command[] = scanner.nextLine().toLowerCase().split("\\s+");
        if(command.length == 0) {
            return Func.NONE;
        }
        switch(command[0]) {
            case "connect":
                return Func.CONNECT;
            case "search":
                return Func.SEARCH;
            case "show":
                return Func.SHOW;
            case "reserve":
                return Func.RESERVE;
            case "update":
                return Func.UPDATE;
            case "cancel":
                return Func.CANCEL;
            case "history":
                return Func.HISTORY;
            case "quit":
                return Func.QUIT;
            default:
                System.out.printf("%s: Invalid command\n", command[0]);
                return Func.NONE;
        }
    }
}

import java.util.Scanner;


public class IO {
	public static String connect_usage = "connect\n";
	public static String search_usage = "search \n";
	public static String show_usage =  "show\n";
	public static String reserve_usage = "reserve -user user_id -card card_num -room room_id\n" +
							            "        -checkin yyyy-mm-dd -checkout yyyy-mm-dd\n" +
							            "        -adults num_adults\n";
	public static String update_usage = "update\n";
	public static String cancel_usage = "cancel -res reservation_num\n";
	public static String history_usage = "history\n";
	public static String quit_usage = "quit\n";
	
    public static void overallUsage() {
        System.out.printf(
                "-----------------------------------------------------------\n" +
                "                          USAGE                        \n" +
                "-----------------------------------------------------------\n" +
                "%s" +
                "	connect to reservation database\n\n" +
                "%s" +
                "	search for rooms fitting specified requirements\n\n" +
                "%s" +
                "	show the availability for each room on the specified\n" +
                "	day\n\n" +
                "%s" +
                "	reserve a room\n\n" +
                "%s" +
                "	update reservation\n\n" +
                "%s" +
                "	cancel reservation\n\n" +
                "%s" +
                "	display active and past reservations\n\n" +
                "%s" +
                "	disconnect from server\n" +
                "-----------------------------------------------------------\n",
                connect_usage, search_usage, show_usage, reserve_usage, update_usage,
                cancel_usage, history_usage, quit_usage);
    }

    public static double read_double(String arg) {
		try {
			double num = Double.parseDouble(arg);
			return num;
		}
		catch (Exception e) {
			return -1;
		}
    }
    
    public static int read_int(String arg) {
		try {
			int num = Integer.parseInt(arg);
			return num;
		}
		catch (Exception e) {
			return -1;
		}
    }
    
    public static Func read_input(Scanner scanner) {
        String command[] = scanner.nextLine().toLowerCase().split("\\s+");
        if(command.length == 0) {
            return Func.NONE;
        }
        switch(command[0]) {
			case "connect":
            	if(Main.connected == false) {
					Database.connect_to_db();
            		return Func.CONNECT;
            	}
            	if(Database.connect_to_db() == null) {
					System.err.println("Failed to connect to database");
					break;
            	}
            	else {
            		Main.connected = true;
				}
                return Func.CONNECT;
            case "search":
            	if(Main.connected == false) {
					System.err.println("Must connect to database");
					break;
            	}
                Database.search_db(command);
                return Func.SEARCH;
            case "show":
            	if(Main.connected == false) {
					System.err.println("Must connect to database");
					break;
            	}
            	Database.show_availabilities();
                return Func.SHOW;
            case "reserve":
            	if(Main.connected == false) {
					System.err.println("Must connect to database");
					break;
            	}
            	Database.reserve_room(command);
                return Func.RESERVE;
            case "update":
            	if(Main.connected == false) {
					System.err.println("Must connect to database");
					break;
            	}
            	Database.update_reservation(command);
                return Func.UPDATE;
            case "cancel":
            	if(Main.connected == false) {
					System.err.println("Must connect to database");
					break;
            	}
            	Database.cancel_room(command);
                return Func.CANCEL;
			case "history":
            	if(Main.connected == false) {
					System.err.println("Must connect to database");
					break;
            	}
            	Database.get_history();
                return Func.HISTORY;
            case "quit":
            	if(Main.connected == true) {
					Database.disconnect();
            	}
                return Func.QUIT;
            default:
                System.err.printf("%s: Invalid command\n", command[0]);
                return Func.NONE;
		}
		return Func.NONE;
    }
}

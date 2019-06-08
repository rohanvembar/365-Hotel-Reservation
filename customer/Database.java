import java.sql.* ;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Database {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String JDNC_DB_URL = "jdbc:mysql://csc365.toshikuboi.net:3306/sec03group05";

    static final String JDBC_USER = "sec03group05";
    static final String JDBC_PASS = "group05@sec03";

    public static Connection db_connection;
	
	public static Connection connect_to_db() {
        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);
            db_connection = connection;
            Main.connected = true;
            return connection;
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        return null;
	}
	
	public static boolean is_valid_date(String date) {
		String format = "yyyy/MM/dd";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		if(date == null) {
			return false;
		}
		try {
			if(sdf.parse(date) == null) {
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void disconnect() {
		try {
			db_connection.close();
			Main.connected = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void get_history(String[] commands)
	{
		if(commands.length != 3) {
			System.err.printf("Usage: %s", IO.history_usage);
		}
		if(commands[1] != "-res") {
			System.err.printf("Usage: %s", IO.history_usage);
		}
		int res_num = IO.read_int(commands[1]);
		if(res_num == -1) {
			return;
		}
//		history query
	}
	
	public static void cancel_room(String[] commands) {
		int res_num = IO.read_int(commands[1]);
		if(res_num == -1) {
			return;
		}
//		cancel room query
	}
	
	public static void reserve_room(String[] commands) {
		if(commands.length != 13) {
			System.err.printf("Usage: %s", IO.reserve_usage);
		}
		int i = 1;
		while(i < commands.length) {
			switch(commands[i]) {
				case "-user":
					return;
				case "-card":
					return;
				case "-room":
					return;
				case "-checkin":
					return;
				case "-checkout":
					return;
				case "-adults":
					return;
				default:
					System.err.printf("Usage: %s", IO.reserve_usage);
			}
		}
	}
	
	public static void show_availabilities(String[] commands)
	{
		if(commands.length != 2) {
			System.err.printf("Usage: %s\n", IO.show_usage);
			return;
		}
		if(!is_valid_date(commands[1])) {
			System.err.printf("Usage: %s\n", IO.show_usage);
			return;
		}
	}
	
	public static void search_db(String[] commands)
	{
		int i = 1; // skip first command
		String query = "";
		if(commands.length % 2 != 1) {
			System.err.printf("Usage: %s", IO.search_usage);
			return;
		}
		
		Room room = new Room();
//		The system shall allow users to search for availabilities of rooms specifying day
//		(checkout and check-in dates), the type of room (single, double, twin, etc), the
//		decor, the price range, the number of rooms, and the number of occupants.
		
		while(i < commands.length) {
			switch(commands[i]) {
				case "-checkin":
					room.in = commands[i+1];
					if(!is_valid_date(room.in)) {
						System.err.println("Invalid check-in date");
						System.err.printf("Usage: %s\n", IO.search_usage);
						return;
					}
					i+=2;
					break;
				case "-checkout":
					room.out = commands[i+1];
					if(!is_valid_date(room.out)) {
						System.err.println("Invalid check out date");
						System.err.printf("Usage: %s\n", IO.search_usage);
						return;
					}
					i+=2;
					break;
				case "-beds":
					room.beds = IO.read_int(commands[i+1]);
					if(room.beds == -1) {
						System.err.println("Invalid number of beds");
						return;
					}
					i+=2;
					break;
				case "-decor":
					room.decor = commands[i+1];
					i+=2;
					break;
				case "-occupants":
					room.occupants = IO.read_int(commands[i+1]);
					if(room.occupants == -1) {
						System.err.println("Invalid number of occupants");
						return;
					}
					i+=2;
					break;
				case "-upper":
					room.upper = IO.read_double(commands[i+1]);
					if(room.upper == -1) {
						System.err.println("Invalid upper price limit");
						return;
					}
					i+=2;
					break;
				case "-lower":
					room.lower = IO.read_double(commands[i+1]);
					if(room.upper == -1) {
						System.err.println("Invalid lower price limit");
						return;
					}
					i+=2;
					break;
				case "-type":
					room.type = commands[i+1];
					i+=2;
					break;
				default:
					System.err.printf("Usage: %s", IO.search_usage);
					return;
			}
		}
		room.print_room();
	}
}

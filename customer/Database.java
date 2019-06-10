import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.time.LocalDate;

public class Database {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String JDNC_DB_URL = "jdbc:mysql://csc365.toshikuboi.net:3306/sec03group05";

	static final String JDBC_USER = "sec03group05";
	static final String JDBC_PASS = "group05@sec03";

	public static Connection db_connection;

	public static Connection connect_to_db() {
		try {
			// Check if the driver class is available
			Class.forName(JDBC_DRIVER).newInstance();

			// Do the base connection
			Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);
			db_connection = connection;
			Main.connected = true;
			return connection;
		} catch (Exception sqlException) {
			System.out.println("Something went wrong with the database");
		}
		return null;
	}

	public static boolean is_valid_date(String date) {
		String format = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		if (date == null) {
			return false;
		}
		try {
			if (sdf.parse(date) == null) {
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
			System.out.println("Something went wrong with the database");
		}
	}

	public static void displayReservation(String resId) {
		try {

			PreparedStatement preparedStatement = db_connection.prepareStatement(
					"select Reservations.id, firstName, lastName, roomName, checkIn, checkOut, rate from Reservations join Customers on Reservations.custID = Customers.id join Rooms on Reservations.roomID = Rooms.roomID where Reservations.id = ?");
			preparedStatement.setString(1, resId);
			ResultSet resultSet = preparedStatement.executeQuery();

			printResultSet(resultSet);

		} catch (Exception sqlException) {
			System.out.println("Something went wrong with the database");
		}
	}

	public static void update_reservation(String[] commands) {

		String query = "update Reservations set roomID = ?, checkIn = ?, checkout = ? where id = ?";
		String curRoomId = "";
		Date curCheckIn = null;
		Date curCheckOut = null;
		Scanner scanner = new Scanner(System.in);
		System.out.print("enter reservation id: ");
		String resId = scanner.nextLine();

		try {

			PreparedStatement preparedStatement = db_connection
					.prepareStatement("select roomID, checkIn, checkOut from Reservations where id = ?");
			preparedStatement.setString(1, resId);
			ResultSet resultSet = preparedStatement.executeQuery();
			int size = 0;
			if (resultSet != null) {
				resultSet.last();
				size = resultSet.getRow();
			}
			if (size == 0) {
				System.out.println("No reservation found with that ID. Try again.");
				return;
			}
			resultSet.beforeFirst();
			while (resultSet.next()) {
				curRoomId = resultSet.getString("roomID");
				System.out.println("Current room id: " + curRoomId);
				curCheckIn = resultSet.getDate("checkIn");
				System.out.println(
						"Current check in date: " + LocalDate.parse(curCheckIn.toString()).plusDays(1).toString());
				curCheckOut = resultSet.getDate("checkOut");
				System.out.println(
						"Current check out date: " + LocalDate.parse(curCheckOut.toString()).plusDays(1).toString());
			}

		} catch (Exception sqlException) {
			System.out.println("Something went wrong with the database");
		}

		System.out.println("enter new room ID (leave blank to not change)");
		String roomID = scanner.nextLine();

		System.out.println("enter new check in date yyyy-mm-dd (leave blank to not change)");
		String checkin = scanner.nextLine();

		System.out.println("enter new check out date yyyy-mm-dd (leave blank to not change)");
		String checkout = scanner.nextLine();

		if (roomID.isEmpty() && checkin.isEmpty() && checkout.isEmpty()) {
			System.out.println("Your reservation was not changed.");
			displayReservation(resId);
			return;
		}
		try {

			PreparedStatement preparedStatement = db_connection.prepareStatement(query);
			if (roomID.isEmpty()) {
				preparedStatement.setString(1, curRoomId);
			} else {
				preparedStatement.setString(1, roomID);
			}
			if (checkin.isEmpty()) {
				preparedStatement.setDate(2, curCheckIn);
			} else {
				preparedStatement.setDate(2, java.sql.Date.valueOf(checkin));
			}
			if (checkout.isEmpty()) {
				preparedStatement.setDate(3, curCheckOut);
			} else {
				preparedStatement.setDate(3, java.sql.Date.valueOf(checkout));
			}
			preparedStatement.setString(4, resId);
			preparedStatement.executeUpdate();

		} catch (java.sql.SQLIntegrityConstraintViolationException fke) {
			System.out.println(
					"Room ID isn't valid or your new dates are not available.\n Your reservation was not changed.");
			return;
		} catch (Exception sqlException) {
			System.out.println("Something went wrong with the database");
		}
		System.out.println("Your reservation was successfully updated.");
		displayReservation(resId);

	}

	final public static void printResultSet(ResultSet rs) throws SQLException {
		int size = 0;
		if (rs != null) {
			rs.last();
			size = rs.getRow();
		}
		if (size == 0) {
			System.out.println("No results found. Try again.");
			return;
		}
		rs.beforeFirst();

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();

		for (int i = 1; i <= columnsNumber; i++) {
			if (i > 1)
				System.out.print("\t | \t");
			System.out.print(rsmd.getColumnName(i));
		}
		System.out.println("");

		while (rs.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1)
					System.out.print("    |    ");
				System.out.print(rs.getString(i));
			}
			System.out.println("");
		}
	}

	public static void get_history() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter your first name: ");
		String firstName = scanner.nextLine();
		System.out.print("Enter your last name: ");
		String lastName = scanner.nextLine();
		try {
			PreparedStatement preparedStatement = db_connection.prepareStatement(
					"select firstName, lastName, roomName, checkIn, checkOut, rate from Reservations join Customers on Reservations.custID = Customers.id join Rooms on Reservations.roomID = Rooms.roomID where firstName = ? and lastname = ?");
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			ResultSet resultSet = preparedStatement.executeQuery();

			printResultSet(resultSet);

		} catch (Exception sqlException) {
			System.out.println("Something went wrong with the database");
		}
	}

	public static void cancel_room(String[] commands) {
		if (commands.length != 2) {
			System.err.printf("Usage: %s", IO.cancel_usage);
		}
		int res_num = IO.read_int(commands[1]);
		if (res_num == -1) {
			System.err.printf("Invalid room id\n");
			System.err.printf("Usage: %s", IO.cancel_usage);
			return;
		}
		// cancel room query
	}

	public static void reserve_room(String[] commands) {
		if (commands.length != 13) {
			System.err.printf("Usage: %s", IO.reserve_usage);
			return;
		}
		int i = 1;
		Reservation res = new Reservation();
		while (i < commands.length) {
			switch (commands[i]) {
			case "-user":
				res.user_id = IO.read_int(commands[i + 1]);
				if (res.user_id == -1) {
					System.err.println("Invalid user ID");
					System.err.printf("Usage: %s\n", IO.reserve_usage);
					return;
				}
				i += 2;
				break;
			case "-card":
				res.card = IO.read_int(commands[i + 1]);
				if (res.card == -1) {
					System.err.println("Invalid card number");
					System.err.printf("Usage: %s\n", IO.reserve_usage);
					return;
				}
				i += 2;
				break;
			case "-room":
				res.room = IO.read_int(commands[i + 1]);
				if (res.room == -1) {
					System.err.println("Invalid room number");
					System.err.printf("Usage: %s\n", IO.reserve_usage);
					return;
				}
				i += 2;
				break;
			case "-checkin":
				res.checkin = commands[i + 1];
				if (!is_valid_date(res.checkin)) {
					System.err.println("Invalid check-in date");
					System.err.printf("Usage: %s\n", IO.reserve_usage);
					return;
				}
				i += 2;
				break;
			case "-checkout":
				res.checkout = commands[i + 1];
				if (!is_valid_date(res.checkout)) {
					System.err.println("Invalid check-out date");
					System.err.printf("Usage: %s\n", IO.reserve_usage);
					return;
				}
				i += 2;
				break;
			case "-adults":
				res.num_adults = IO.read_int(commands[i + 1]);
				if (res.num_adults == -1) {
					System.err.println("Invalid number of adults");
					System.err.printf("Usage: %s\n", IO.reserve_usage);
					return;
				}
				i += 2;
				break;
			default:
				System.err.printf("Usage: %s", IO.reserve_usage);
				return;
			}
		}
		res.print_res();
	}

	public static void show_availabilities() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("enter date (yyyy-mm-dd)");
		String userDate = scanner.nextLine();
		try {

			PreparedStatement preparedStatement = db_connection.prepareStatement("select roomName, popularity, rate, "
					+ "IF(? >= nextAvail, \"Available\", \"Not Available\") as available, nextAvail, bedType, numBeds, "
					+ "maxOccupancy from (select roomID, max(checkout) as nextAvail, round(sum(datediff(checkout, "
					+ "checkin))/180, 2) as popularity from Reservations where checkout >= date_sub(?, interval 180 day)"
					+ " group by roomID) tmp join Rooms on tmp.roomID = Rooms.roomID");
			preparedStatement.setDate(1, java.sql.Date.valueOf(userDate));
			preparedStatement.setDate(2, java.sql.Date.valueOf(userDate));

			ResultSet resultSet = preparedStatement.executeQuery();

			printResultSet(resultSet);

		} catch (Exception sqlException) {
			System.out.println("Something went wrong with the database");
		}
	}

	public static void search_db(String[] commands) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("enter bed type (Double, Queen, King) or leave blank for all");
		String bedtype = scanner.nextLine();
		System.out.println("enter decor (traditional, rustic, modern) or leave blank for all");
		String decor = scanner.nextLine();
		System.out.println("enter max rate (per night) or leave blank for no maximum");
		String maxrate = scanner.nextLine();
		System.out.println("enter min rate (per night) or leave blank for no minimum");
		String minrate = scanner.nextLine();
		System.out.println("enter number of guests");
		String numguests = scanner.nextLine();
		System.out.println("enter check in date (yyyy-mm-dd)");
		String checkin = scanner.nextLine();
		System.out.println("enter check out date (yyyy-mm-dd)");
		String checkout = scanner.nextLine();
		try {

			PreparedStatement preparedStatement = db_connection.prepareStatement("select roomName, numBeds, bedType,"
					+ " maxOccupancy, rate, decor from Rooms where bedType LIKE ? and decor LIKE ? and rate < ? and rate > ? "
					+ "and maxOccupancy >= ? and roomID not in (select roomID from Reservations where (checkIn >= ? and "
					+ "checkOut <= ?) and roomID in (select roomID from Rooms where bedType LIKE ? and decor LIKE ? and rate "
					+ "< ? and rate > ? and maxOccupancy >= ?))");
			if (bedtype.isEmpty()) {
				preparedStatement.setString(1, "%");
				preparedStatement.setString(8, "%");

			} else {
				preparedStatement.setString(1, bedtype);
				preparedStatement.setString(8, bedtype);

			}
			if (decor.isEmpty()) {
				preparedStatement.setString(2, "%");
				preparedStatement.setString(9, "%");

			} else {
				preparedStatement.setString(2, decor);
				preparedStatement.setString(9, decor);
			}
			if (maxrate.isEmpty()) {
				preparedStatement.setString(3, "9999");
				preparedStatement.setString(10, "9999");

			} else {
				preparedStatement.setString(3, maxrate);
				preparedStatement.setString(10, maxrate);

			}
			if (minrate.isEmpty()) {
				preparedStatement.setString(4, "0");
				preparedStatement.setString(11, "0");

			} else {
				preparedStatement.setString(4, minrate);
				preparedStatement.setString(11, minrate);

			}
			preparedStatement.setString(5, numguests);
			preparedStatement.setDate(6, java.sql.Date.valueOf(checkin));
			preparedStatement.setDate(7, java.sql.Date.valueOf(checkout));
			preparedStatement.setString(12, numguests);

			ResultSet resultSet = preparedStatement.executeQuery();

			printResultSet(resultSet);

		} catch (Exception sqlException) {
			System.out.println("Something went wrong with the database.");
		}
	}
}

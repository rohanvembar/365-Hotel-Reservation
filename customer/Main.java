import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.Scanner;
import java.sql.* ;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String JDNC_DB_URL = "jdbc:mysql://csc365.toshikuboi.net:3306/sec03group05";

    static final String JDBC_USER = "sec03group05";
    static final String JDBC_PASS = "group05@sec03";

	public static boolean connected = false;
    public static void main(String args[]) {
        //displayReservationsFromName();
        //displayReservation();
        //changeReservation();
        displayAvailAndPop();
        //searchRooms();
    }

    final public static void printResultSet(ResultSet rs) throws SQLException {

        // Prepare metadata object and get the number of columns.
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        // Print column names (a header).
        for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1) System.out.print("\t | \t");
            System.out.print(rsmd.getColumnName(i));
        }
        System.out.println("");

        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print("\t | \t");
                System.out.print(rs.getString(i));
            }
            System.out.println("");
        }
    }
    public static void displayAvailAndPop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter date (yyyy-mm-dd)");
        String userDate = scanner.nextLine();
        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement("select roomName, popularity, rate, "+
                    "IF(? >= nextAvail, \"Available\", \"Not Available\") as available, nextAvail, bedType, numBeds, " +
                    "maxOccupancy from (select roomID, max(checkout) as nextAvail, round(sum(datediff(checkout, " +
                    "checkin))/180, 2) as popularity from Reservations where checkout >= date_sub(?, interval 180 day)"+
                    " group by roomID) tmp join Rooms on tmp.roomID = Rooms.roomID");
            preparedStatement.setDate(1, java.sql.Date.valueOf(userDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(userDate));

            ResultSet resultSet = preparedStatement.executeQuery();
            printResultSet(resultSet);

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }

    }
    public static void changeReservation() {
        String query = "update Reservations set roomID = ?, checkIn = ?, checkout = ? where id = ?";
        String curRoomId = "";
        Date curCheckIn = null;
        Date curCheckOut = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter reservation id");
        String resId = scanner.nextLine();

        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement("select roomID, checkIn, checkOut from Reservations where id = ?");
            preparedStatement.setString(1, resId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                curRoomId = resultSet.getString("roomID");
                System.out.println("Current room id: " + curRoomId);
                curCheckIn = resultSet.getDate("checkIn");
                System.out.println("Current check in date: " + curCheckIn.toString());
                curCheckOut = resultSet.getDate("checkOut");
                System.out.println("Current check out date: " + curCheckOut.toString());
            }

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }

        System.out.println("enter new room ID (leave blank to not change)");
        String roomID = scanner.nextLine();

        System.out.println("enter new check in date yyyy-mm-dd (leave blank to not change)");
        String checkin = scanner.nextLine();

        System.out.println("enter new check out date yyyy-mm-dd (leave blank to not change)");
        String checkout = scanner.nextLine();

        if (roomID.length() == 0 && checkin == null && checkout == null) {
            System.out.println("nothing to change");
            return;
        }
        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (roomID.isEmpty()) {
                preparedStatement.setString(1, curRoomId);
            }
            else {
                preparedStatement.setString(1, roomID);
            }
            if (checkin.isEmpty()) {
                preparedStatement.setDate(2, curCheckIn);
            }
            else {
                preparedStatement.setDate(2, java.sql.Date.valueOf(checkin));
            }
            if (checkout.isEmpty()) {
                preparedStatement.setDate(3, curCheckOut);
            }
            else {
                preparedStatement.setDate(3, java.sql.Date.valueOf(checkout));
            }
            preparedStatement.setString(4, resId);
            preparedStatement.executeUpdate();

        } catch (java.sql.SQLIntegrityConstraintViolationException fke) {
            System.out.println("invalid room id");
            return;
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        displayReservation(resId);
    }
    public static void displayReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter reservation id");
        String resId = scanner.nextLine();

        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement("select Reservations.id, firstName, lastName, roomName, checkIn, checkOut, rate from Reservations join Customers on Reservations.custID = Customers.id join Rooms on Reservations.roomID = Rooms.roomID where Reservations.id = ?");
            preparedStatement.setString(1, resId);
            ResultSet resultSet = preparedStatement.executeQuery();

            printResultSet(resultSet);

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }
    public static void displayReservation(String resId) {
        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement("select Reservations.id, firstName, lastName, roomName, checkIn, checkOut, rate from Reservations join Customers on Reservations.custID = Customers.id join Rooms on Reservations.roomID = Rooms.roomID where Reservations.id = ?");
            preparedStatement.setString(1, resId);
            ResultSet resultSet = preparedStatement.executeQuery();

            printResultSet(resultSet);

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }
    public static void displayReservationsFromName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter first name");
        String firstName = scanner.nextLine();
        System.out.println("enter last name");
        String lastName = scanner.nextLine();
        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement("select firstName, lastName, roomName, checkIn, checkOut, rate from Reservations join Customers on Reservations.custID = Customers.id join Rooms on Reservations.roomID = Rooms.roomID where firstName = ? and lastname = ?");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();

            printResultSet(resultSet);

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }
    public static void searchRooms() {
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
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement("select roomName, numBeds, bedType,"+
                    " maxOccupancy, rate, decor from Rooms where bedType LIKE ? and decor LIKE ? and rate < ? and rate > ? "+
                    "and maxOccupancy >= ? and roomID not in (select roomID from Reservations where (checkIn >= ? and "+
                    "checkOut <= ?) and roomID in (select roomID from Rooms where bedType LIKE ? and decor LIKE ? and rate "+
                    "< ? and rate > ? and maxOccupancy >= ?))");
            if (bedtype.isEmpty()) {
                preparedStatement.setString(1, "%");
            }
            else {
                preparedStatement.setString(1, bedtype);
            }
            if (decor.isEmpty()){
                preparedStatement.setString(2, "%");
            }
            else {
                preparedStatement.setString(2, decor);
            }
            if (maxrate.isEmpty()) {
                preparedStatement.setString(3, "9999");
            }
            else {
                preparedStatement.setString(3, maxrate);
            }
            if (minrate.isEmpty()) {
                preparedStatement.setString(4, "0");
            }
            else {
                preparedStatement.setString(4, minrate);
            }
            preparedStatement.setString(5, numguests);
            preparedStatement.setDate(6, java.sql.Date.valueOf(checkin));
            preparedStatement.setDate(7, java.sql.Date.valueOf(checkout));
            preparedStatement.setString(8, bedtype);
            preparedStatement.setString(9, decor);
            preparedStatement.setString(10, maxrate);
            preparedStatement.setString(11, minrate);
            preparedStatement.setString(12, numguests);

            ResultSet resultSet = preparedStatement.executeQuery();

            printResultSet(resultSet);

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }
}

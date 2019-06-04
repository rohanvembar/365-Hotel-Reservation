import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

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


    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String JDNC_DB_URL = "jdbc:mysql://csc365.toshikuboi.net:3306/sec03group05";

    static final String JDBC_USER = "sec03group05";
    static final String JDBC_PASS = "group05@sec03";

	public static boolean connected = false;
    public static void main(String args[]) {
        displayReservationsFromName();
        displayReservation();
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

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(rsmd.getColumnName(i)+ " " + columnValue);
                }
                System.out.println("");
            }

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

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(rsmd.getColumnName(i)+ " " + columnValue);
                }
                System.out.println("");
            }

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }
}

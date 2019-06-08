import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Manager {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String JDNC_DB_URL = "jdbc:mysql://csc365.toshikuboi.net:3306/sec03group05";

    static final String JDBC_USER = "sec03group05";
    static final String JDBC_PASS = "group05@sec03";


    public static void getReview() {
        //Scanner scanner = new Scanner(System.in);
        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER).newInstance();

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement("select roomName, Month, sum(rev) as Revenue from\n" +
                    "\t(select roomName, MONTH(checkOut) as Month, r2.rate * DATEDIFF(checkOut, CheckIn) as Rev from \n" +
                    "\tReservations r1 join Rooms r2 on r1.roomID = r2.roomID) as Prices\n" +
                    "    group by roomName, Month\n" +
                    "    order by roomName, Month;");

            ResultSet resultSet = preparedStatement.executeQuery();

            int month = 1;

            Room currentRoom = new Room("filler");
            ArrayList<Room> rooms = new ArrayList<>();

            while (resultSet.next()) {
                if (month == 13){
                    currentRoom.setTotal();
                    rooms.add(currentRoom);
                    month = 1;
                }
                int m = resultSet.getInt("Month");
                int rev = resultSet.getInt("Revenue");

                if (month == 1) {
                    String name = resultSet.getString("roomName");
                    currentRoom = new Room(name);
                }

                currentRoom.addMonthRevenue(m-1, rev);

                month += 1;
            }

            for (Room r : rooms){
                System.out.println(r.toString());
            }

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static String getMonth(int m){
        String month = "";

        switch(m){
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
        }

        return month;
    }

    public static void main(String args[]){
        getReview();
    }
}

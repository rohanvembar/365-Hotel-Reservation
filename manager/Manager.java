import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String JDNC_DB_URL = "jdbc:mysql://csc365.toshikuboi.net:3306/sec03group05";

    static final String JDBC_USER = "sec03group05";
    static final String JDBC_PASS = "group05@sec03";

    private static String connect_usage = "report";
    private static String quit_usage = "quit";


    private static void getReview() {
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

            ManagerRoom currentRoom = new ManagerRoom("filler");
            ArrayList<ManagerRoom> rooms = new ArrayList<>();

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
                    currentRoom = new ManagerRoom(name);
                }

                currentRoom.addMonthRevenue(m-1, rev);

                month += 1;
            }

            String header = String.format("%27s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s\n", "Room Name",
                    "Jan.", "Feb.", "Mar.", "Apr.", "May.", "Jun.", "Jul.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec.", "Total");

            String barrier = "";
            for (int i = 0; i < header.length(); i++){
                barrier += "-";
            }

            System.out.println(barrier);
            System.out.println("YEARLY REPORT");
            System.out.println(barrier);
            System.out.println(header + barrier);

            for (ManagerRoom r : rooms){
                System.out.println(r.toString());
            }

            System.out.println(barrier + "\n");

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

    private static void printUsage(){
        System.out.printf(
                "-----------------------------------------------------------\n" +
                        "                          USAGE                        \n" +
                        "-----------------------------------------------------------\n" +
                        "%s:	Get Yearly Report\n\n" +
                        "%s:	Leave the Manager Portal\n\n" + "Enter Command: ", connect_usage, quit_usage);
    }

    public static void main(String args[]){

        Scanner myObj = new Scanner(System.in);
        while (true) {
            printUsage();
            String response = myObj.nextLine();

            if (response.equals("report")) {
                getReview();
            } else if (response.equals("quit")) {
                System.out.println("\nGoodbye!");
                break;
            } else {
                System.out.println("Invalid Response. Try Again");
            }
        }
    }
}

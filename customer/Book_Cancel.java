import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Book_Cancel {



        static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        static final String JDNC_DB_URL = "jdbc:mysql://csc365.toshikuboi.net:3306/sec03group05";

        static final String JDBC_USER = "sec03group05";
        static final String JDBC_PASS = "group05@sec03";


        public static void cancelReservation() {

            int cust_id;
            //Scanner scanner = new Scanner(System.in);
            try {
                //Check if the driver class is available
                Class.forName(JDBC_DRIVER).newInstance();

                // Do the base connection
                Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);


                Scanner scan = new Scanner(System.in);


                System.out.println("Please enter your First Name : ");
                String first_name = scan.next(); // get string

                System.out.println("Please enter your Last Name : ");
                String last_name = scan.next(); // get string

                System.out.println("Please enter your reservation ID : ");
                int res_id = scan.nextInt(); // get int

                //Cancel a Reservation

                //Find Customer ID from firstname and lastname
                PreparedStatement preparedStatement = connection.prepareStatement
                        ("select * from Customers WHERE firstName = ? AND lastName = ?; ");
                preparedStatement.setString(1, first_name);
                preparedStatement.setString(2, last_name);

                ResultSet resultSet = preparedStatement.executeQuery();
                //Set Reservation Active State
                while (resultSet.next()) {
                    cust_id = resultSet.getInt("id");
                    PreparedStatement preparedStatement1 = connection.prepareStatement
                            ("UPDATE Reservations SET active = ? WHERE custID = ? AND id = ? ; ");
                    preparedStatement1.setInt(1, 0);
                    preparedStatement1.setInt(2, cust_id);
                    preparedStatement1.setInt(3, res_id);

                    preparedStatement1.executeUpdate();
                }


            } catch (Exception sqlException) {
                sqlException.printStackTrace();
            }
        }

        public static void bookReservation(){
            //Book a Reservation

            //Scanner scanner = new Scanner(System.in);
            try {
                //Check if the driver class is available
                Class.forName(JDBC_DRIVER).newInstance();

                // Do the base connection
                Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);


                Scanner scan = new Scanner(System.in);

                System.out.println("enter bed type (Double, Queen, King) or leave blank for all");
                String bedtype = scan.nextLine();
                System.out.println("enter decor (traditional, rustic, modern) or leave blank for all");
                String decor = scan.nextLine();

                System.out.println("Please enter your Minimum Rate or leave blank for all: ");
                String min_rate = scan.nextLine(); // get double

                System.out.println("Please enter your Maximum Rate or leave blank for all: ");
                String max_rate = scan.nextLine(); // get double

                System.out.println("Please enter your First Name : ");
                String first_name = scan.next(); // get string

                System.out.println("Please enter your Last Name : ");
                String last_name = scan.next(); // get string


                System.out.println("Please enter the Amount of Adults in the Room : ");
                int adults = scan.nextInt(); // get integer

                System.out.println("Please enter the Amount of Kids in the Room : ");
                int kids = scan.nextInt(); // get integer



                System.out.println("enter check in date (yyyy-mm-dd)");
                String checkin = scan.next();

                System.out.println("enter check out date (yyyy-mm-dd)");
                String checkout = scan.next();



                System.out.println("Please enter your Card Number  : ");
                int card = scan.nextInt(); // get double

                int occupancy = adults + kids;


                //Search to see what rooms are avaliable

                PreparedStatement preparedStatement = connection.prepareStatement("select roomId, roomName, numBeds, bedType,"+
                        " maxOccupancy, rate, decor from Rooms where bedType LIKE ? and decor LIKE ? and rate < ? and rate > ? "+
                        "and maxOccupancy >= ? and roomID not in (select roomID from Reservations where (checkIn >= ? and "+
                        "checkOut <= ?) and roomID in (select roomID from Rooms where bedType LIKE ? and decor LIKE ? and rate "+
                        "< ? and rate > ? and maxOccupancy >= ?))");

                if (bedtype.isEmpty()) {
                    preparedStatement.setString(1, "%");
                    preparedStatement.setString(8, "%");


                }
                else {
                    preparedStatement.setString(1, bedtype);
                    preparedStatement.setString(8, bedtype);

                }
                if (decor.isEmpty()){
                    preparedStatement.setString(2, "%");
                    preparedStatement.setString(9, "%");

                }
                else {
                    preparedStatement.setString(2, decor);
                    preparedStatement.setString(9, decor);
                }
                if (max_rate.isEmpty()) {
                    preparedStatement.setString(3, "9999");
                    preparedStatement.setString(10, "9999");

                }
                else {
                    preparedStatement.setString(3, max_rate);
                    preparedStatement.setString(10, max_rate);

                }
                if (min_rate.isEmpty()) {
                    preparedStatement.setString(4, "0");
                    preparedStatement.setString(11, "0");

                }
                else {
                    preparedStatement.setString(4, min_rate);
                    preparedStatement.setString(11, min_rate);

                }
                preparedStatement.setInt(5, occupancy);
                preparedStatement.setDate(6, java.sql.Date.valueOf(checkin));
                preparedStatement.setDate(7, java.sql.Date.valueOf(checkout));
                preparedStatement.setInt(12, occupancy);

                ResultSet resultSet = preparedStatement.executeQuery();
                //System.out.println(preparedStatement);

                //AVALIABLE ROOMS WHICH ARE READY TO BOOK


                resultSet.next();
                String roomID = resultSet.getString("roomID");
                String roomName = resultSet.getString("roomName");
                int numBeds = resultSet.getInt("numBeds");
                String bedType = resultSet.getString("bedType");
                int max_occupancy = resultSet.getInt("maxOccupancy");
                int rate = resultSet.getInt("rate");
                decor = resultSet.getString("decor");

                //INSERT CREDIT CARD NUMBER

                PreparedStatement preparedStatement1 = connection.prepareStatement
                        (" insert into Cards (cardNum)"
                                + " values (?);");
                preparedStatement1.setInt(1, card);
                preparedStatement1.executeUpdate();



                //CHECK IF CUSTOMER IN DATABASE: IF NOT ADD CUSTOMER

                try{//FIND CUSTOMER IN DATABASE
                    PreparedStatement preparedStatement2 = connection.prepareStatement
                            ("select *" +
                                    "from Customers " +
                                    "where firstName = ? and lastName = ?;");
                    preparedStatement2.setString(1, first_name);
                    preparedStatement2.setString(2, last_name);

                    ResultSet resultSet1 = preparedStatement2.executeQuery();
                    resultSet.next();
                    String firstName = resultSet1.getString("firstName");
                    String lastName = resultSet1.getString("lastName");


                }
                //IF CUSTOMER NOT IN DATABASE: ADD CUSTOMER
                catch (Exception sqlException){


                    PreparedStatement preparedStatement3 = connection.prepareStatement
                            (" insert into Customers (id, firstName, lastName)"
                                    + " values (?, ?, ?);");

                    preparedStatement3.setString(2, first_name);
                    preparedStatement3.setString(3, last_name);
                    preparedStatement3.setInt(1, 207);
                    preparedStatement3.executeUpdate();

                }


                //GET CUSTOMER ID
                PreparedStatement preparedStatement5 = connection.prepareStatement
                        ("select id " +
                                "from Customers " +
                                "where  firstName = ? and lastName = ?;");

                preparedStatement5.setString(1, first_name);
                preparedStatement5.setString(2, last_name);
                ResultSet resultSet5 = preparedStatement5.executeQuery();
                resultSet5.next();
                int c_id = resultSet5.getInt("id");


                //INSERT CUSTOMER + ROOM INTO RESERVATIONS
                PreparedStatement preparedStatement4 = connection.prepareStatement
                        (" insert into Reservations (id, custID, roomID, cardNum, checkIn, checkOut, numAdults, numKids, active)"
                                + " values (?, ?, ?, ?, ?, ?, ?, ?, ? );");

                preparedStatement4.setInt(1, 99866 );
                preparedStatement4.setInt(2, c_id);
                preparedStatement4.setString(3, roomID);
                preparedStatement4.setInt(4, card);

                preparedStatement4.setString(5, checkin);
                preparedStatement4.setString(6, checkout);
                preparedStatement4.setInt(7, adults);
                preparedStatement4.setInt(8, kids);
                preparedStatement4.setInt(9, 1);
                preparedStatement4.executeUpdate();


                //PRINT CONFIRMATION

                PreparedStatement preparedStatement6 = connection.prepareStatement
                        ("select id " +
                                "from Reservations " +
                                "where  custId = ? and roomID = ? and checkIn = ? and checkOut = ? and cardNum = ?;");

                preparedStatement6.setInt(1, c_id);
                preparedStatement6.setString(2, roomID);
                preparedStatement6.setString(3, checkin);
                preparedStatement6.setString(4, checkout);
                preparedStatement6.setInt(5, card);

                ResultSet resultSet6 = preparedStatement6.executeQuery();
                resultSet6.next();
                int r_id = resultSet6.getInt("id");

                System.out.println(" ");
                System.out.println("Reservation Confirmed:" + "\n*RESERVATION NUMBER*: " + r_id +
                        "\nCustomer: " + first_name + " " + last_name + "   Customer ID: " + c_id
                        + "\nRoom: " + roomName + "\nCheckIn: " + checkin + " Checkout: " + checkout
                        + "\nOccupancy: " + occupancy + "\n");

            } catch (Exception sqlException) {
                System.out.println("No Rooms Avaliable Matching Preferences");
                sqlException.printStackTrace();
            }
        }



        public static void main(String args[]) {
        /*Scanner scanner = new Scanner(System.in);
        Func func;
        IO.overallUsage();
        do {
            func = IO.read_input(scanner);
        } while(func != Func.QUIT);
        */
            bookReservation();

        }


}

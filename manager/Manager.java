import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            PreparedStatement preparedStatement = connection.prepareStatement("select * from Reservations Limit 5");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String result = resultSet.toString();
                System.out.println(result);
            }

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void main(String args[]){
        getReview();
    }
}

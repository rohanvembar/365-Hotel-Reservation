import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Manager {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String JDNC_DB_URL = "jbdc:mysql://toshikuboi.net:3306/sec03group05";

    static final String JDBC_USER = "sec03group05";
    static final String JDBC_PASS = "group05@sec03";


    public static void getReview() {
        //Scanner scanner = new Scanner(System.in);
        try {
            //Check if the driver class is available
            Class.forName(JDBC_DRIVER);

            // Do the base connection
            Connection connection = DriverManager.getConnection(JDNC_DB_URL, JDBC_USER, JDBC_PASS);

            // Add the statement
            PreparedStatement preparedStatement = connection.prepareStatement("");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String coffeeName = resultSet.getString("COF_NAME");
                int supplierID = resultSet.getInt("SUP_ID");
                float price = resultSet.getFloat("PRICE");
                int sales = resultSet.getInt("SALES");
                int total = resultSet.getInt("TOTAL");
                System.out.println(coffeeName + "\t" + supplierID +
                        "\t" + price + "\t" + sales +
                        "\t" + total);
            }

        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }
}

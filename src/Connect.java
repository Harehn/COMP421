import java.sql.* ;
import java.util.Scanner;
class Connect
{
    private static int sqlCode = 0;
    private static String sqlState = "00000";

    public static void main ( String [ ] args ) throws SQLException
    {
        Scanner scanner = new Scanner(System.in);
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }

        String url = "jdbc:db2://winter2024-comp421.cs.mcgill.ca:50000/COMP421";
        String your_userid = "cs421g65";
        String your_password = "DaJiNi#65";

        Connection con = DriverManager.getConnection (url,your_userid,your_password) ;
        Statement statement = con.createStatement ( ) ;

        boolean exit = false;
        while (!exit) {
            // Need both queries and modifications
            System.out.println("Music Store Main Menu:");
            System.out.println("    1. Register New Client");
            System.out.println("    2. Modification...");
            System.out.println("    3. Get All Valid Discount Codes");
            System.out.println("    4. Query...");
            System.out.println("    5. Query w/ submenu");
            System.out.println("    6. Quit");
            System.out.print("Please enter your option: ");
            int input = scanner.nextInt();
            scanner.nextLine();

            // Calls function depending on input (write functions below)
            switch(input) {
                case 1:
                    registerNewClient(con);
                    break;
                case 2:
                    // function
                    break;
                case 3:
                    getAllValidDiscountCodes(con);
                    break;
                case 4:
                    // function
                    break;
                case 5:
                    // function
                    break;
                case 6:
                    exit = true;
                    System.out.println("Quitting...");
                    break;
                default:
                    System.out.println("\nPlease enter a number between 1-6\n");
                    break;
            }
        }
        statement.close ( ) ;
        con.close ( ) ;
    }

    // Case 1
    public static void registerNewClient(Connection con) {
        try
        {
            Statement statement = con.createStatement();
            String insertSQL = "INSERT INTO Client VALUES (\'michael42111@example.com\', \'Unit 2509 Box 6821 DPO AP 94156\'," +
                    " \'5740 George Spring Apt. 746 Kylestad, Ak 15801\', 4932930475935340, 514692157701289)";
            System.out.println(insertSQL);
            statement.executeUpdate(insertSQL);
            System.out.println("CLIENT REGISTERED\n");

            insertSQL = "INSERT INTO Client VALUES (\'jessicamcgill1@example.com\', \'584 David Meadows Suite 128 Johnsonfurt, RI 82182\'," +
                    " \'USNV Price FPO AA 56202\', 4709540832081790, 514692157701242)";
            System.out.println(insertSQL);
            statement.executeUpdate(insertSQL);
            System.out.println("CLIENT REGISTERED\n");
            System.out.println("DONE\n");

        }
        catch (SQLException e)
        {
            sqlCode = e.getErrorCode();
            sqlState = e.getSQLState();
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);
        }
    }

    // Case 3
    public static void getAllValidDiscountCodes(Connection con) {
        try
        {
            Statement statement = con.createStatement();
            String querySQL = "SELECT code, valid, amt, note FROM Discount WHERE valid = TRUE";
            System.out.println(querySQL);
            java.sql.ResultSet rs = statement.executeQuery (querySQL) ;

            System.out.println("\nValid Discount Codes:");
            while (rs.next())
            {
                int code = rs.getInt("code");
                boolean valid = rs.getBoolean("valid");
                int amt = rs.getInt ( "amt") ;
                String note = rs.getString ("note");
                System.out.printf("Code: %d, Valid: %b, Amount: %d, Note: %s\n", code, valid, amt, note);
            }
            System.out.println ("\nDONE\n");
        }
        catch (SQLException e)
        {
            sqlCode = e.getErrorCode();
            sqlState = e.getSQLState();

            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);
        }
    }
}

//        // Creating a table
//        try
//        {
//            String createSQL = "CREATE TABLE " + tableName + " (id INTEGER, name VARCHAR (25)) ";
//            System.out.println (createSQL ) ;
//            statement.executeUpdate (createSQL ) ;
//            System.out.println ("DONE");
//        }
//        catch (SQLException e)
//        {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
//        // Inserting Data into the table
//        try
//        {
//            String insertSQL = "INSERT INTO " + tableName + " VALUES ( 1 , \'Vicki\' ) " ;
//            System.out.println ( insertSQL ) ;
//            statement.executeUpdate ( insertSQL ) ;
//            System.out.println ( "DONE" ) ;
//
//            insertSQL = "INSERT INTO " + tableName + " VALUES ( 2 , \'Vera\' ) " ;
//            System.out.println ( insertSQL ) ;
//            statement.executeUpdate ( insertSQL ) ;
//            System.out.println ( "DONE" ) ;
//            insertSQL = "INSERT INTO " + tableName + " VALUES ( 3 , \'Franca\' ) " ;
//            System.out.println ( insertSQL ) ;
//            statement.executeUpdate ( insertSQL ) ;
//            System.out.println ( "DONE" ) ;
//
//        }
//        catch (SQLException e)
//        {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
//        // Querying a table
//        try
//        {
//            String querySQL = "SELECT id, name from " + tableName + " WHERE NAME = \'Vicki\'";
//            System.out.println (querySQL) ;
//            java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
//
//            while ( rs.next ( ) )
//            {
//                int id = rs.getInt ( 1 ) ;
//                String name = rs.getString (2);
//                System.out.println ("id:  " + id);
//                System.out.println ("name:  " + name);
//            }
//            System.out.println ("DONE");
//        }
//        catch (SQLException e)
//        {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
//        //Updating a table
//        try
//        {
//            String updateSQL = "UPDATE " + tableName + " SET NAME = \'Mimi\' WHERE id = 3";
//            System.out.println(updateSQL);
//            statement.executeUpdate(updateSQL);
//            System.out.println("DONE");
//
//            // Dropping a table
//            String dropSQL = "DROP TABLE " + tableName;
//            System.out.println ( dropSQL ) ;
//            statement.executeUpdate ( dropSQL ) ;
//            System.out.println ("DONE");
//        }
//        catch (SQLException e)
//        {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
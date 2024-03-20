import java.sql.* ;
import java.util.Scanner;
class Connect
{
    private static int sqlCode = 0;
    private static String sqlState = "00000";
    private static Scanner scanner = new Scanner(System.in);

    public static void main ( String [ ] args ) throws SQLException
    {
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }

        String url = "jdbc:db2://winter2024-comp421.cs.mcgill.ca:50000/COMP421";
        System.out.print("Username:");
        String your_userid = scanner.next();
        scanner.nextLine();
        System.out.print("Password:");
        String your_password = scanner.next();
        scanner.nextLine();
        
        Connection con = DriverManager.getConnection (url,your_userid,your_password) ;
        Statement statement = con.createStatement();

        boolean exit = false;
        while (!exit) {
            // Need both queries and modifications
            System.out.println("Music Store Main Menu:");
            System.out.println("    1. Register New Client");
            System.out.println("    2. Create Table For Users With Points");
            System.out.println("    3. Get All Valid Discount Codes");
            System.out.println("    4. Update Member Points");
            System.out.println("    5. Select Records From Members or Guests (submenu)");
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
                    makeTableUsersWithPoints(con);
                    break;
                case 3:
                    getAllValidDiscountCodes(con);
                    break;
                case 4:
                    updateMemberPoints(con);
                    break;
                case 5:
                    showMembersOrGuests(con);
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
        System.out.println("Program teminated with all resources closed.");
    }
    
    public static String wrap(String var) {
    	return "\'" + var +"\'";
    }
    
    public static String wrapn(String var) {
    	return "\'" + var +"\', ";
    }

    // Case 1
    public static void registerNewClient(Connection con) {
    	System.out.println("-------------------------------------------------------");
        try
        {
            Statement statement = con.createStatement();
            System.out.print("Enter the email of the client:");
            String email = scanner.next();
            scanner.nextLine();
            System.out.println("Do you want to enter billing and shipping address, credit card number and phone number?");
            System.out.println("    1. YES");
            System.out.println("    2. NO");
            System.out.print("Enter your choice: ");
            int option = scanner.nextInt();
            if (option == 1) {
            	System.out.print("Enter your Billing Address:");
            	scanner.nextLine(); //Don't remove. Magic line
            	String baddr = scanner.nextLine();
                
            	System.out.print("Enter your Shipping Address:");
            	String saddr = scanner.nextLine();
                
                
            	System.out.print("Enter your Credit Card Number(16 characters):");
            	String crnum = scanner.next();
                scanner.nextLine();
                
            	System.out.print("Enter your Phone num(15 characters):");
            	String phone = scanner.next();
                scanner.nextLine();
                
                String insertSQL = "INSERT INTO CLIENT VALUES (" + 
                        wrapn(email) + wrapn(baddr) + wrapn(saddr) +  wrapn(crnum)
                        + wrap(phone)
                        +")";
	            System.out.println(insertSQL);
	            statement.executeUpdate(insertSQL);
	            System.out.println("CLIENT REGISTERED\n");
            }
            else if(option == 2) {
            	String insertSQL = "INSERT INTO CLIENT(EMAIL) VALUES (" + 
                        wrap(email) +")";
	            System.out.println(insertSQL);
	            statement.executeUpdate(insertSQL);
	            System.out.println("CLIENT REGISTERED\n");
            }else {
            	System.out.println("Invalid choice, going back...");
            }
            
            System.out.println("DONE\n");

        }
        catch (SQLException e)
        {
            sqlCode = e.getErrorCode();
            sqlState = e.getSQLState();
            if (sqlCode == -803) {
            	System.out.println("The client is already registered.");
            }else {
            	System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            }
        }catch (Exception e) {
        	System.out.println("Unexpected Error.");
        	System.out.println(e);
        }
        System.out.println("-------------------------------------------------------");
    }

    // Case 2
    public static void makeTableUsersWithPoints(Connection con) {
    	System.out.println("-------------------------------------------------------");
        try
        {
            Statement statement = con.createStatement();
            String createSQL = "CREATE TABLE IF NOT EXISTS UsersWithPoints (" +
                    "email varchar(50) NOT NULL, points INTEGER, PRIMARY KEY(email))";
            System.out.println (createSQL ) ;
            statement.executeUpdate (createSQL ) ;
            String insertSQL = "INSERT INTO UsersWithPoints (email, points)" +
                    "SELECT email, points FROM Member WHERE points > 0";
            System.out.println(insertSQL);
            statement.executeUpdate(insertSQL);
        }
        catch (SQLException e)
        {
            sqlCode = e.getErrorCode();
            sqlState = e.getSQLState();
            if (sqlCode == -803) {
            	System.out.println("The table has already been created.");
            }else {
            	System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            }
        }
        try {
        	Statement statement = con.createStatement();
        	String selectSQL = "SELECT * FROM UsersWithPoints LIMIT 10";
            System.out.println(selectSQL);
            java.sql.ResultSet rs = statement.executeQuery(selectSQL);
            System.out.println("Printing a maximum of 10 results from the table.");
            while (rs.next())
            {
                String email = rs.getString("email");
                int points = rs.getInt ( "points") ;
                System.out.printf("Email: %s, Points: %d\n",email, points);
            }
            System.out.println("\nDONE\n");
        }catch (SQLException e){
        	System.out.println("Issues with accessing table.");
        }catch (Exception e) {
        	System.out.println("Unexpected Error.");
        	System.out.println(e);
        }
        System.out.println("-------------------------------------------------------");
    }

    // Case 3
    public static void getAllValidDiscountCodes(Connection con) {
    	System.out.println("-------------------------------------------------------");
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
        }catch (Exception e) {
        	System.out.println("Unexpected Error.");
        	System.out.println(e);
        }
        System.out.println("-------------------------------------------------------");
    }

    // Case 4
    public static void updateMemberPoints(Connection con) {
        System.out.println("-------------------------------------------------------");
        try {
            Statement statement = con.createStatement();
            System.out.print("Enter email of member you want to update points for: ");
            String email = scanner.next();
            scanner.nextLine();

            String checkEmail = "SELECT COUNT(*) FROM Member WHERE email = " + wrap(email);
            ResultSet rs = statement.executeQuery(checkEmail);
            if (rs.next() && rs.getInt(1) == 1) {
                System.out.print("Enter new points: ");
                int points = scanner.nextInt();
                scanner.nextLine();
                String updateSQL = "UPDATE Member SET points = " + points + " WHERE email = " + wrap(email);
                System.out.println(updateSQL);
                statement.executeUpdate(updateSQL);
                System.out.println("\nDONE\n");
            }
            else {
                System.out.println("Email does not exist");
            }
        }
        catch (SQLException e)
        {
            sqlCode = e.getErrorCode();
            sqlState = e.getSQLState();
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }catch (Exception e) {
            System.out.println("Unexpected Error.");
            System.out.println(e);
        }
        System.out.println("-------------------------------------------------------");
    }


    // Case 5
    public static void showMembersOrGuests(Connection con) {
    	System.out.println("-------------------------------------------------------");
        try
        {
            Statement statement = con.createStatement();
            System.out.println("Select Table To Show First 10 Records:");
            System.out.println("    1. Members");
            System.out.println("    2. Guests");
            System.out.println("Please enter a number between 1 and 2: \n");
            int input = scanner.nextInt();
            scanner.nextLine();
            String table = "";
            switch (input) {
                case 1:
                    table = "Member";
                    break;
                case 2:
                    table = "Guest";
                    break;
                default:
                    System.out.println("\nInvalid input\n");
                    return;
            }
            String querySQL = "SELECT * FROM " + table + " FETCH FIRST 10 ROWS ONLY";
            System.out.println (querySQL) ;
            java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
            while ( rs.next ( ) )
            {
                if ("Member".equals(table)) {
                    String email = rs.getString("email");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    System.out.printf("EMAIL: %s, NAME: %s %s\n",
                            email, firstName, lastName);
                }
                else {
                    String email = rs.getString("email");
                    System.out.printf("Email: %s\n", email);
                }
            }
            System.out.println ("\nDONE\n");
        }
        catch (SQLException e)
        {
            sqlCode = e.getErrorCode();
            sqlState = e.getSQLState();
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }catch (Exception e) {
        	System.out.println("Unexpected Error.");
        	System.out.println(e);
        }
        System.out.println("-------------------------------------------------------");
    }
}
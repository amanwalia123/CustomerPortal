package Application;
import java.util.*;
import java.lang.*;
import java.sql.*;

public class SingletonDBModel {

    private Connection conDB; // Connection to the database system.
    private String url; // URL: Which database?

    //Purchase Information

    /* Id of the customer selected */
    public int custId;

    /*Club for book selected to buy*/
    public String bookClub;
    /*
     * Once user finds the book to buy from its category, this will serve
     * to keep reference for the book identification.
     */
    public List<BookInfo> selBooks = null;

    //Singleton Instance of this class
    private static SingletonDBModel instance = null;

    private SingletonDBModel() {

        try {
            // Register the driver with DriverManager.
            Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // URL: Which database?
        url = "jdbc:db2:c3421m";

        // Initialize the connection.
        try {
            // Connect with a fall-thru id & password
            conDB = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.print("\nSQL: database connection error.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Let's have autocommit turned off.  No particular reason here.
        try {
            conDB.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.print("\nFailed trying to turn autocommit off.\n");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static SingletonDBModel getInstance() {
        if (instance == null) {
            instance = new SingletonDBModel();
        }
        return instance;
    }

    //query database

    public Customer find_customer(int custID) {

        Customer c = null;
        String queryText; // The SQL text.
        PreparedStatement querySt = null; // The query handle.
        ResultSet answers = null; // A cursor.

        queryText = "SELECT * " + "FROM YRB_CUSTOMER C " + "WHERE C.CID = " + custID;

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch (SQLException e) {
            System.out.println("find Customer Query failed in prepare stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            answers = querySt.executeQuery();
        } catch (SQLException e) {
            System.out.println("find Customer Query failed in execute stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Any answer?

        try {
            if (answers.next()) {

                int cid = answers.getInt("cid");
                String custName = answers.getString("name");
                String custCity = answers.getString("city");

                c = new Customer(cid, custName, custCity);

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch (SQLException e) {
            System.out.print("find Customer Query failed in closing cursor stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch (SQLException e) {
            System.out.print("\"find Customer Query failed in closing handle stage\"");
            System.out.println(e.toString());
            System.exit(0);
        }

        //Storing the customer ID inside class
        if (c != null) {
            this.custId = custID;
        }

        return c;

    }

    public List<String> fetch_categories() {

        String queryText; // The SQL text.
        PreparedStatement querySt = null; // The query handle.
        ResultSet answers = null; // A cursor.
        List<String> categories = new ArrayList<String>();

        queryText = "SELECT * FROM YRB_CATEGORY";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch (SQLException e) {
            System.out.println("find Categories Query failed in prepare stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            answers = querySt.executeQuery();
        } catch (SQLException e) {
            System.out.println("find Categories Query failed in execute stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Any answer?

        try {
            while (answers.next()) {
                String category = answers.getString("cat");
                categories.add(category);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch (SQLException e) {
            System.out.print("find Categories Query failed in closing cursor stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch (SQLException e) {
            System.out.print("\"find Categories Query failed in closing handle stage\"");
            System.out.println(e.toString());
            System.exit(0);
        }

        return categories;

    }

    public List<BookInfo> find_book(String title, String category) {

        String queryText; // The SQL text.
        PreparedStatement querySt = null; // The query handle.
        ResultSet answers = null; // A cursor.
        List<BookInfo> books = new ArrayList<BookInfo>();

        queryText = "SELECT B.TITLE,B.YEAR,B.LANGUAGE,B.WEIGHT,B.CAT" + " FROM YRB_BOOK B where B.CAT ='" + category
                + "' AND B.TITLE = ? ";

        //System.out.println(queryText);

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch (SQLException e) {
            System.out.println("find Book Query failed in prepare stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setString(1, title);
            answers = querySt.executeQuery();
        } catch (SQLException e) {
            System.out.println("find Book Query failed in execute stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Any answer?

        try {
            while (answers.next()) {
                String bookTitle = answers.getString("title");
                int year = answers.getInt("year");
                String language = answers.getString("language");
                int weight = answers.getInt("weight");
                String cat = answers.getString("cat");

                BookInfo book = new BookInfo(bookTitle, year, language, weight, cat);

                books.add(book);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch (SQLException e) {
            System.out.print("find Book Query failed in closing cursor stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch (SQLException e) {
            System.out.print("find Book Query failed in closing handle stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        if(!books.isEmpty())
            selBooks = new ArrayList<BookInfo>(books);

        return books;

    }

    public double min_price(String cat, String title, int year) {

        String queryText; // The SQL text.
        PreparedStatement querySt = null; // The query handle.
        ResultSet answers = null; // A cursor.
        //Minimum Price for the given book
        double minPrice = 0.00;

        queryText = "WITH " + "MYDATA " + "as " + "(SELECT O.PRICE as PRICE , O.CLUB as CLUB " + "FROM YRB_OFFER O "
                + "WHERE  O.TITLE ='" + title + "' and O.YEAR = " + year + " and EXISTS(SELECT *" + "FROM YRB_MEMBER M "
                + "WHERE M.CID = " + this.custId + " and M.CLUB = O.CLUB)" + ") " + "SELECT * " + "FROM MYDATA "
                + "WHERE MYDATA.PRICE = (SELECT MIN(MYDATA.PRICE) " + "FROM MYDATA)";

        //System.out.println(queryText);

        //queryText = "SELECT MIN(O.PRICE) as PRICE " +
        //          "FROM YRB_OFFER O WHERE O.TITLE = '"+title+"' and O.YEAR = "+year;

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch (SQLException e) {
            System.out.println("find Minimum Price Query failed in prepare stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            answers = querySt.executeQuery();
        } catch (SQLException e) {
            System.out.println("find Minimum Price Query failed in execute stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Any answer?

        try {
            if (answers.next()) {

                minPrice = answers.getDouble("price");
                bookClub = answers.getString("club");

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch (SQLException e) {
            System.out.print("find Minimum Price Query failed in closing cursor stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch (SQLException e) {
            System.out.print("find Minimum Price Query failed in closing handle stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        return minPrice;

    }

    //update database

    public void insert_purchase(int cid, String club, String title, int year, int quantity) {

        String queryText; // The SQL text.
        PreparedStatement querySt = null; // The query handle.


        queryText = "INSERT INTO YRB_PURCHASE (cid,club,title,year,when,qnty) " + "VALUES (" + cid + ", '" + club
                + "', '" + title + "'," + year + ",CURRENT_TIMESTAMP," + quantity + ")";

        //System.out.println(queryText);

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch (SQLException e) {
            System.out.println("Inserting Entry failed in prepare stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            int worked = querySt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Inserting Entry failed in execute stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch (SQLException e) {
            System.out.print("Inserting entry failed in closing handle stage");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Commit.  Okay, here nothing to commit really, but why not...
        try {
            conDB.commit();

        } catch (SQLException e) {
            System.out.print("\nFailed trying to commit.\n");
            e.printStackTrace();
            System.exit(0);

        }
        // Close the connection.
        try {
            conDB.close();

        } catch (SQLException e) {
            System.out.print("\nFailed trying to close the connection.\n");
            e.printStackTrace();
            System.exit(0);

        }

    }

}

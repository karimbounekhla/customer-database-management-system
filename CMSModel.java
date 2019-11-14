package exercise234CMS;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CMSModel {
    public Connection jdbc_connection;
    public PreparedStatement pStatement;
    public String tableName = "Client";
    public String databaseName = "mydb";
    // Table will be created in existing database - no createDB() method
    public String connectionInfo = "jdbc:mysql://localhost/" + databaseName +
            "?verifyServerCertificate=false&useSSL=true",
            login          = "root",
            password       = "karim123",
            dataFile = "ENSF593-Lab8/src/exercise234CMS/clients.txt";

    public CMSModel()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            jdbc_connection = DriverManager.getConnection(connectionInfo, login, password);
            createTable();
            fillTable();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Client> getSearchResults(String searchCriteria, String searchQuery) {
        String sql;
        if (searchCriteria.equals("id")) {
            sql = "SELECT * FROM " + tableName + " WHERE id=?";
        } else if (searchCriteria.equals("lastName")) {
            sql = "SELECT * FROM " + tableName + " WHERE lastName=?";
        } else if (searchCriteria.equals("clientType")) {
            sql = "SELECT * FROM " + tableName + " WHERE clientType=?";
        } else {
            return null;
        }

        ArrayList<Client> searchResults = new ArrayList<>();

        try {
            pStatement = jdbc_connection.prepareStatement(sql);
            pStatement.setString(1, searchQuery);

            ResultSet rs = pStatement.executeQuery();
            while (rs.next()) {
                Client toAdd = new Client(rs.getString("firstName"), rs.getString("lastName"),
                                          rs.getString("address"), rs.getString("postalCode"),
                                          rs.getString("phoneNumber"),
                                          rs.getString("clientType"));
                toAdd.setId(rs.getInt("id"));
                searchResults.add(toAdd);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    /**
     * Add a client to the database table
     * @param client the Client
     */
    public void addClient(Client client)
    {
        String sql = "INSERT INTO " + tableName + " VALUES (null, ?, ?, ?, ?, ?, ?);";
        try {
            pStatement = jdbc_connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, client.getFirstName());
            pStatement.setString(2, client.getLastName());
            pStatement.setString(3, client.getAddress());
            pStatement.setString(4, client.getPostalCode());
            pStatement.setString(5, client.getPhoneNumber());
            pStatement.setString(6, String.valueOf(client.getClientType()));
            pStatement.executeUpdate();

            ResultSet generatedID = pStatement.getGeneratedKeys();
            if (generatedID.next()) {
                client.setId(generatedID.getInt(1));
            }
            System.out.println(client.getId());
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClient(int id, Client client) {
        String sql = "UPDATE " + tableName + " SET " +
                "firstName = ?, " +
                "lastName = ?, " +
                "address = ?, " +
                "postalCode = ?, " +
                "phoneNumber = ?, " +
                "clientType = ? " +
                "WHERE id = ?;";

        try {
            pStatement = jdbc_connection.prepareStatement(sql);
            pStatement.setString(1, client.getFirstName());
            pStatement.setString(2, client.getLastName());
            pStatement.setString(3, client.getAddress());
            pStatement.setString(4, client.getPostalCode());
            pStatement.setString(5, client.getPhoneNumber());
            pStatement.setString(6, client.getClientType());
            pStatement.setInt(7, id);
            pStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?;";

        try {
            pStatement = jdbc_connection.prepareStatement(sql);
            pStatement.setInt(1, id);
            pStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create table if it hasn't been created yet.
     */
    private void createTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT AUTO_INCREMENT, " +
                "firstName VARCHAR(20) NOT NULL, " +
                "lastName VARCHAR(20) NOT NULL, " +
                "address VARCHAR(50) NOT NULL, " +
                "postalCode CHAR(7) NOT NULL, " +
                "phoneNumber CHAR(12) NOT NULL, " +
                "clientType CHAR(1) NOT NULL, " +
                "PRIMARY KEY (id))";
        try {
            pStatement = jdbc_connection.prepareStatement(sql);
            pStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * If Table is empty, fills the data table with all the tools from the text file 'clients.txt' if found
     * Program assumes that data in the .txt file is already correct.
     */
    private void fillTable()
    {
        try {
            // Ensures that table is empty before importing data - this ensures data isn't added more than once
            pStatement = jdbc_connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                return;
            }

            Scanner sc = new Scanner(new FileReader(dataFile));
            while(sc.hasNext())
            {
                String clientInfo[] = sc.nextLine().split(";");
                addClient(new Client(clientInfo[0], clientInfo[1], clientInfo[2],
                        clientInfo[3], clientInfo[4], clientInfo[5]));
            }
            sc.close();
        } catch(FileNotFoundException e) {
            System.err.println("File " + dataFile + " Not Found!");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CMSModel cm = new CMSModel();

        ArrayList<Client> test = cm.getSearchResults("id", "5");
        System.out.println(test);

        System.out.println(test.get(0).getClass());
    }

}

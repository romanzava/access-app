/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.access;

/**
 *
 * @author Lopo
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class DatabaseConnector {
 
	static Connection aConnection = null;
	static PreparedStatement aStatement = null;
        private List<User> userList;

    public DatabaseConnector() throws SQLException {
        userList = UserList.getInstance();
    }
        
	public void makeJDBCConnection() {
 
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			log("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			return;
		}
 
		try {
			// DriverManager: The basic service for managing a set of JDBC drivers.                       
			aConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/portal?useSSL=true", "portal", "123456");
			if (aConnection != null) {
				log("Connection Successful! Enjoy. Now it's time to push data");
			} else {
				log("Failed to make connection!");
			}
		} catch (SQLException e) {
			log("MySQL Connection Failed!");
			return;
		}
 
	}
 
	public void addDataToDB(String name, String surname, String uid, String validity, String access) {
                this.dbConnect();
		try {
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String insertQueryStatement = "INSERT  INTO  users  VALUES  (?,?,?,?,?,?,?,?,?)";
 
			aStatement = aConnection.prepareStatement(insertQueryStatement);
			aStatement.setString(1, name);
			aStatement.setString(2, surname);
			aStatement.setString(3, uid);
			aStatement.setString(4, validity);
                        aStatement.setString(5, access);
                        aStatement.setTimestamp(6, timestamp); //created column
                        aStatement.setString(7, null); // edited column
                        aStatement.setString(8, "1"); // 1 - user activated // 0 - user deactivated
                        aStatement.setString(9, null); // deleted column
			// execute insert SQL statement
			aStatement.executeUpdate();
			log(name + surname + " added successfully");
                        this.closeDB();
		} catch (SQLException e) {
                    log("Could not get all users from DB");
		}
	}
        
        public boolean updateData(User user){
            this.dbConnect();
            try {
                String updateQueryStatement = "UPDATE users set name = ?, surname = ?, validity = ?,"
                        + "access = ?, edited = ? where uid = ?";
                
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                
                aStatement = aConnection.prepareStatement(updateQueryStatement);
		aStatement.setString(1, user.getName());
		aStatement.setString(2, user.getSurname());
		aStatement.setString(3, user.getValid());
		aStatement.setString(4, String.valueOf(user.getAcctype()));
                aStatement.setTimestamp(5, timestamp); // edited column
                aStatement.setString(6, user.getUid()); 
                aStatement.execute();
                this.closeDB();
                return true;
            } catch (
 
		SQLException e) {
			e.printStackTrace();
                        return false;
		}
        }
        
        public void deleteDataFromDB(String uid) {
                this.dbConnect();
		try {
			String insertQueryStatement = "UPDATE users set status = 0, deleted = ? where uid = ?";
 
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        
			aStatement = aConnection.prepareStatement(insertQueryStatement);
                        aStatement.setTimestamp(1, timestamp);
			aStatement.setString(2, uid);
                        
			// execute insert SQL statement
			aStatement.executeUpdate();
			log(uid + " deleted successfully");
                        this.closeDB();
		} catch (
 
		SQLException e) {
			e.printStackTrace();
		}
	}
 
	public void getAllUsers() {
            this.dbConnect();
		try {                       
			String getQueryStatement = "SELECT * FROM users where status = 1";
                        //String getQueryStatement = "SELECT * FROM users";
                        
			aStatement = aConnection.prepareStatement(getQueryStatement);
 
			// Execute the Query, and get a java ResultSet
			ResultSet rs = aStatement.executeQuery();
                        if (!equality()){userList.clear();}
			// Let's iterate through the java ResultSet
			while (rs.next()) {
				String name = rs.getString("Name");
				String surname = rs.getString("Surname");
				String uid = rs.getString("Uid");
				String validity = rs.getString("Validity");
                                String access = rs.getString("Access");
 
				// Simply Print the results
				System.out.format("%s, %s, %s, %s, %s\n", name, surname, uid, validity, access);
                                if (!equality()){
                                    userList.add(new User(name,surname,uid,validity,Integer.parseInt(access)));        
                                }
                        }
                        this.closeDB();
		} catch (SQLException e) {
		}
                
	}
 
        private boolean equality(){
            this.dbConnect();
		try {
			String getQueryStatement = "SELECT count(*) as num FROM users where status = 1";
 
			aStatement = aConnection.prepareStatement(getQueryStatement);
 
			// Execute the Query, and get a java ResultSet
			ResultSet rs = aStatement.executeQuery();
                // Let's iterate through the java ResultSet
                while (rs.next()) {
                        System.out.println(rs.getInt("num"));
                        return rs.getInt("num") == userList.size();
                }
                
        } catch(SQLException e) {
			e.printStackTrace();
		}
            return false;
        }
        
        public boolean isDuplicatePK(String uid){
            this.dbConnect();
            try {
                String getQueryStatement = "SELECT count(*) as num FROM users where uid = ?";
                
                aStatement = aConnection.prepareStatement(getQueryStatement);
                aStatement.setString(1, uid);
                
                // Execute the Query, and get a java ResultSet
                ResultSet rs = aStatement.executeQuery();
                while (rs.next()) {
                        return rs.getBoolean("num");
                }
                this.closeDB();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
        
	// Simple log utility
	private static void log(String string) {
		System.out.println(string);
 
	}
        
        public void closeStatement() throws SQLException{
            aStatement.close();
        }
        
        public void closeConnection() throws SQLException{
            aConnection.close();
        }
    
    private void dbConnect(){
        this.makeJDBCConnection();
    }
    
    private void closeDB(){
            try {
                this.closeStatement();
                this.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}

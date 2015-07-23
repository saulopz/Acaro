package br.org.subverse.chatbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static private Connection connection = null;
    static private String url = "";
    static private String username = "";
    static private String password = "";
    static private String dbdriver = "";

    static public void setConfig(String iurl, String uname, String pass,
	    String dbdrv) {
	url = iurl;
	username = uname;
	password = pass;
	dbdriver = dbdrv;
    }

    static public Connection getConnection() {
	try {
	    if (connection == null || connection.isClosed()) {
		Class.forName(dbdriver);
		if (username.equals("")) {
		    connection = DriverManager.getConnection(url);
		} else {
		    connection = DriverManager.getConnection(url, username,
			    password);
		}
	    }
	} catch (SQLException sqle) {
	    connection = null;
	    System.out.println(sqle.getMessage());
	} catch (ClassNotFoundException cnfe) {
	    connection = null;
	    System.out.println(cnfe.getMessage());
	}
	return connection;
    }

    static public void close() {
	if (connection == null) {
	    return;
	}
	try {
	    connection.close();
	} catch (SQLException e) {
	    connection = null;
	}
	connection = null;
    }
}

package chatbot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import program.DatabaseConnection;


public class VariableList {
    private String client;
    private List<Variable> variables;

    public VariableList(String client) {
	this.client = client;
	variables = new ArrayList<Variable>();
	load();
    }

    public String get(String name) {
	for (Variable var : variables) {
	    if (var.getName().equals(name)) {
		return var.getValue();
	    }
	}
	return "";
    }

    public void set(String name, String value) {
	for (Variable var : variables) {
	    if (var.getName().equals(name) && var.isGlobal()) {
		var.setValue(value);
		return;
	    }
	}
	variables.add(new Variable(false, name, value));
    }

    public void load() {
	try {
	    Connection con = DatabaseConnection.getConnection();
	    Statement stmt = con.createStatement();
	    String query = "select * from variable where client='" + client
		    + "' or client=''";
	    ResultSet rs = stmt.executeQuery(query);
	    while (rs.next()) {
		Boolean global = false;
		if (rs.getString("client").equals("")) {
		    global = true;
		}
		variables.add(new Variable(global, rs.getString("name"), rs
			.getString("value")));
	    }
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	System.out.println(this);
    }

    private void save() {
	try {
	    Connection con = DatabaseConnection.getConnection();
	    Statement stmt = con.createStatement();
	    String query = "select email from client where email='" + client
		    + "'";
	    ResultSet rs = stmt.executeQuery(query);
	    if (rs.next()) {
		query = "delete from variable where client='" + client + "'";
	    } else {
		query = "insert into client (email) values ('" + client + "')";
	    }
	    stmt.executeUpdate(query);
	    Boolean write = false;
	    query = "insert into variable (client, name, value) values ";
	    System.out.println(variables);
	    for (Variable var : variables) {
		if (!var.isGlobal()) {
		    if (write) {
			query += ", ";
		    }
		    query += "('" + client + "', '" + var.getName() + "', '"
			    + var.getValue() + "')";
		    write = true;
		}
	    }
	    System.out.println("SEMA: " + query);
	    if (write) {
		stmt.executeUpdate(query);
	    }
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public String toString() {
	String out = "";
	for (Variable var : variables) {
	    out += var + "\n";
	}
	return out;
    }

    public void done() {
	save();
    }
}

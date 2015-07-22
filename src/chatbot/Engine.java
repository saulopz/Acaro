package chatbot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import program.DatabaseConnection;


public class Engine {
    private VariableList var;

    public Engine(String client) {
	var = new VariableList(client);
    }

    public String run(String message) {
	String out = getCategory(message);
	if (out.equals("")) {
	    out = getScape();
	}
	return out;
    }

    private String getCategory(String message) {
	String msg = "";
	try {
	    Connection con = DatabaseConnection.getConnection();
	    Statement stmt = con.createStatement();
	    String category = "";

	    // Try to pick a category from the current topic
	    String query = "select * from pattern p, category c where c.topic='"
		    + var.get("topic")
		    + "' and p.category=c.name and '"
		    + message
		    + "' like upper(p.content) order by random() limit 1";
	    System.out.println(query);
	    ResultSet rs = stmt.executeQuery(query);
	    if (rs.next()) {
		category = rs.getString("name");
		String topic = rs.getString("topic");
		if (!topic.equals("")) {
		    var.set("topic", topic);
		}
	    }
	    // If not found, try to get a category without topic
	    if (!var.get("topic").equals("") && category.equals("")) {
		query = "select * from pattern p, category c where c.topic='' "
			+ "and p.category=c.name and '" + message
			+ "' like upper(p.content) order by random() limit 1";
		System.out.println(query);
		rs = stmt.executeQuery(query);
		if (rs.next()) {
		    category = rs.getString("name");
		}
	    }
	    // Try to catch a template
	    if (!category.equals("")) {
		// Conditional is not implemented for while
		query = "select * from template where category='" + category
			+ "' order by random() limit 1";
		System.out.println(query);
		rs = stmt.executeQuery(query);
		if (rs.next()) {
		    msg = rs.getString("content");
		}
	    }
	    stmt.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return msg;
    }

    public void setVar(String name, String value) {
	var.set(name, value);
    }

    public String getVar(String name) {
	return var.get(name);
    }

    private String getScape() {
	String msg = "";
	try {
	    Connection con = DatabaseConnection.getConnection();
	    Statement stmt = con.createStatement();
	    String query = "select content from scape order by random() limit 1";
	    ResultSet rs = stmt.executeQuery(query);
	    if (rs.next()) {
		msg = rs.getString("content");
	    }
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return msg;
    }

    public void done() {
	var.done();
    }
}

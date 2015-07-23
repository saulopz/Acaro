package br.org.subverse.chatbot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatBot {
    private String client;
    private String logFile;
    private String botName = "MyBot";
    private VariableList var;

    public ChatBot(String client) {
	startClient(client);
	var = new VariableList(client);
	getBotName();
    }

    private void startClient(String client) {
	this.client = client;
	logFile = "log/" + client + ".log";
	try {
	    Connection con = DatabaseConnection.getConnection();
	    Statement stmt = con.createStatement();
	    String query = "select email from client where email='" + client
		    + "'";
	    ResultSet rs = stmt.executeQuery(query);
	    if (!rs.next()) {
		query = "insert into client (email) values ('" + client + "')";
		stmt.executeUpdate(query);
	    }
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public String send(String message) {
	log(client, message);
	// remove accentuation
	String msg = Normalizer.normalize(message, Normalizer.Form.NFD)
		.replaceAll("[^\\p{ASCII}]", "").toUpperCase();
	// remove special chars
	msg = msg.replaceAll("[^A-Za-z0-9]", " ");
	// remove white spaces exceeding
	msg = msg.replaceAll("\\s+", " ");
	// get robot response
	msg = getCategory(msg);
	if (msg.equals("")) {
	    msg = getScape();
	}
	log(getBotName(), msg);
	return msg;
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

    private void log(String speaker, String message) {
	try {
	    SimpleDateFormat format = new SimpleDateFormat(
		    "yyyy-MM-dd hh:mm:ss");
	    String dateStr = format.format(new Date());
	    String line = "(" + dateStr + ") " + speaker + ": " + message;
	    BufferedWriter bw = new BufferedWriter(
		    new FileWriter(logFile, true));
	    bw.write(line);
	    bw.newLine();
	    bw.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void setVar(String name, String value) {
	var.set(name, value);
    }

    public String getVar(String name) {
	return var.get(name);
    }

    public String getClient() {
	return client;
    }

    public String getBotName() {
	String name = getVar("botname");
	if (!name.equals("")) {
	    botName = name;
	}
	return botName;
    }

    public void done() {
	var.done();
    }
}

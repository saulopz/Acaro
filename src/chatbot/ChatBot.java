package chatbot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

import program.DatabaseConnection;


public class ChatBot {
    private String client;
    private String logFile;
    private Engine engine;
    private String botName = "MyBot";

    public ChatBot(String client) {
	startClient(client);
	engine = new Engine(client);
	String name = engine.getVar("botname");
	if (!name.equals("")) {
	    botName = name;
	}
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
	    if (rs.next()) {
		return;
	    }
	    query = "insert into client (email) values ('" + client + "')";
	    stmt.executeUpdate(query);
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public String send(String message) {
	log(client, message);
	// Remove accentuation
	String msg = Normalizer.normalize(message, Normalizer.Form.NFD)
		.replaceAll("[^\\p{ASCII}]", "").toUpperCase();
	// remove special chars
	msg = msg.replaceAll("[^A-Za-z0-9]", " ");
	// remove white spaces exceeding
	msg = msg.replaceAll("\\s+", " ");
	// Run chatbot
	msg = engine.run(msg);
	log(botName, msg);
	return msg;
    }

    private void log(String speaker, String message) {
	try {
	    Date curDate = new Date();
	    SimpleDateFormat format = new SimpleDateFormat(
		    "yyyy-MM-dd hh:mm:ss");
	    String dateStr = format.format(curDate);
	    String line = "(" + dateStr + ") " + speaker + ": " + message;
	    FileWriter fw = new FileWriter(logFile, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    bw.write(line);
	    bw.newLine();
	    bw.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void setVar(String name, String value) {
	if (engine == null) {
	    return;
	}
	engine.setVar(name, value);
    }

    public String getVar(String name) {
	if (engine == null) {
	    return "";
	}
	return engine.getVar(name);
    }

    public String getClient() {
	return client;
    }

    public String getBotName() {
	return botName;
    }

    public void done() {
	engine.done();
    }
}

package br.org.subverse.chatbot;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Main {
    private JFrame frame;
    private JTextArea chatlog;
    private JTextArea input;
    private String client;
    private ChatBot bot;

    public Main(String email) {
	DatabaseConnection.setConfig("jdbc:postgresql://localhost/acaro",
		"saulo", "1234", "org.postgresql.Driver");
	client = email;
	bot = new ChatBot(client);
	frame = new JFrame("My Simple Chatbot");
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setDefaultCloseOperation(0);
	frame.setLayout(new BorderLayout());

	JPanel panelLog = new JPanel();
	panelLog.setBorder(new TitledBorder(new EtchedBorder(), "Chat log"));
	chatlog = new JTextArea(16, 58);
	chatlog.setEditable(false);
	JScrollPane scrollLog = new JScrollPane(chatlog);
	scrollLog
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	panelLog.add(scrollLog);

	JPanel panelInput = new JPanel();
	panelInput.setBorder(new TitledBorder(new EtchedBorder(), "Input"));
	input = new JTextArea(3, 58);
	JScrollPane scrollInput = new JScrollPane(input);
	scrollInput
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	panelInput.add(scrollInput);

	input.addKeyListener(new KeyListener() {
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    e.consume();
		    input.setText(input.getText().trim());
		    if (!input.getText().equals("")) {
			if (!chatlog.getText().equals("")) {
			    chatlog.append("\n");
			}
			chatlog.append(client + ": " + input.getText());
			chatlog.append("\n" + bot.getBotName() + ": "
				+ bot.send(input.getText()));
			input.setText("");
			input.requestFocus();
		    }
		}
	    }

	    @Override
	    public void keyTyped(KeyEvent e) {
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }
	});

	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	menuBar.add(fileMenu);

	JMenuItem reloadAction = new JMenuItem(
		"Load a knowledge base directory");
	reloadAction.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		JFileChooser file = new JFileChooser();
		file.setCurrentDirectory(new File(System
			.getProperty("user.dir")));
		file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int i = file.showSaveDialog(null);
		if (i != 1) {
		    File kbpath = file.getSelectedFile();
		    String kbPath = kbpath.getPath();
		    KnowledgeBase kb = new KnowledgeBase(kbPath);
		    bot.setVar("path", kbPath);
		    kb.load();
		}
	    }
	});
	JMenuItem exitAction = new JMenuItem("Exit");
	exitAction.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		done();
		System.exit(0);
	    }
	});
	fileMenu.add(reloadAction);
	fileMenu.add(exitAction);

	frame.setJMenuBar(menuBar);
	frame.add(panelLog, BorderLayout.PAGE_START);
	frame.add(panelInput, BorderLayout.PAGE_END);
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	frame.setResizable(false);
	input.requestFocus();
    }

    public void done() {
	bot.done();
	String kbpath = bot.getVar("path");
	System.out.println(kbpath);
	if (!kbpath.equals("")) {
	    KnowledgeBase kb = new KnowledgeBase(kbpath);
	    kb.done();
	}
	DatabaseConnection.close();
    }

    public static void main(String[] arguments) {
	String user = JOptionPane.showInputDialog("Please, enter your user");
	if (!user.equals("")) {
	    new Main(user);
	}
    }
}

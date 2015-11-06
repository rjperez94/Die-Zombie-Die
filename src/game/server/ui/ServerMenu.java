package game.server.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import game.Game;
import game.network.server.Server;

/**
 * <code>ServerMenu</code> is a class designed to display hosting options as a
 * dedicated server. It provides options for port, map and players.
 *
 * @author Kieran Lewis
 * @author Brendan Goodenough
 * @author Gordon Adams
 * @version 0.1.0
 */

public class ServerMenu implements ActionListener {
	private JPanel container, optionPanel, logPanel;
	private JLabel portLabel, mapLabel, maxPlayersLabel;
	private JTextField port, map, maxPlayers;
	private JButton toggle;
	private JTextArea log;
	private JScrollPane scroll;

	private Server server;

	public ServerMenu() {
		log = new JTextArea("Server Stopped\n");
		log.setBackground(Color.BLACK);
		log.setForeground(Color.GREEN);
		log.setLineWrap(true);

		scroll = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(200, 180));

		port = createJTextField("8008");
		map = createJTextField("boards/test.txt");
		maxPlayers = createJTextField("2");

		portLabel = new JLabel("Port (default 8008)");
		mapLabel = new JLabel("Map File");
		maxPlayersLabel = new JLabel("Max Players (default 2)");
		toggle = new JButton("Start");

		toggle.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0x808080)),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		toggle.addActionListener(this);

		optionPanel = new JPanel();
		optionPanel.setBackground(new Color(0xefefef));
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.PAGE_AXIS));
		optionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		optionPanel.add(contain(portLabel));
		optionPanel.add(contain(port));
		optionPanel.add(contain(mapLabel));
		optionPanel.add(contain(map));
		optionPanel.add(contain(maxPlayersLabel));
		optionPanel.add(contain(maxPlayers));
		optionPanel.add(contain(toggle));

		container = new JPanel(new BorderLayout());
		container.setBackground(new Color(0xefefef));
		container.setPreferredSize(new Dimension(640, 480));
		container.add(optionPanel, BorderLayout.NORTH);
		container.add(scroll, BorderLayout.SOUTH);

		JFrame frame = new JFrame(Game.TITLE + " Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(container);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private JTextField createJTextField(String text) {
		JTextField field = new JTextField(text);
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0x808080)),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		field.setPreferredSize(new Dimension(200, 40));
		return field;
	}

	private JPanel contain(JComponent component) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new Color(0xefefef));
		panel.add(component);
		return panel;
	}

	public static void main(String[] args) {
		new ServerMenu();
	}

	/**
	 * Method used to append the server messages to the log.
	 *
	 * @param message
	 */
	public void append(final String message) {
		log.append(message);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (toggle.getText().toLowerCase().equals("start")) {
			toggle.setText("Stop");
			try {
				server = new Server(Integer.parseInt(port.getText()), Integer.parseInt(maxPlayers.getText()));
				server.start();
				append("Server Started\n");
			} catch (Exception e) {
				append(e.getMessage());
				append("Failed to start server!\n");
			}
		} else {
			toggle.setText("Start");
			append("Server Stopped\n");
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
				append(e.getMessage());
			}
		}
	}
}

package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import source.Graph.*;

public class Signup extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField textname;
	private JLabel lblFakebook;
	private JButton btnSignup;
	private JTextField textUsername;
	private JTextField BlueFb;
	private JLabel lblfb;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Signup frame = new Signup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Signup() {
		getContentPane().setEnabled(false);
		
		createcomponent();
		createEvent();
		
	}

	private void createEvent() {
		//Placer holder Username
		textUsername.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (textUsername.getText().equals("Username")) {
		            textUsername.setText("");
		            textUsername.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (textUsername.getText().isEmpty()) {
		            textUsername.setForeground(Color.GRAY);
		            textUsername.setText("Username");
		        }
		    }
		    });
		
		////Placer holder name
		textname.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (textname.getText().equals("Name")) {
		            textname.setText("");
		            textname.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (textname.getText().isEmpty()) {
		            textname.setForeground(Color.GRAY);
		            textname.setText("Name");
		        }
		    }
		    });		
		
		////Placer holder password
		passwordField.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (passwordField.getText().equals("Password")) {
		        	passwordField.setText("");
		        	passwordField.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (passwordField.getText().isEmpty()) {
		            passwordField.setForeground(Color.GRAY);
		            passwordField.setText("Password");
		        }
		    }
		    });			
	
	//Sign up action
		btnSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			String name = textname.getText();	
			String userName = textUsername.getText();
			String password  = passwordField.getText();
			try {
	                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
	                        "root", "Conheoanh123");

	                    PreparedStatement st = (PreparedStatement) connection
	                        .prepareStatement("INSERT IGNORE into profile  (username, password, name, status) values (?,?,?,\"Happy\")");
	                    
	                    
	                    st.setString(1, userName);
	                    st.setString(2, password);
	                    st.setString(3, name);
	                    st.execute();
	                    st.close();
	                    
	                    // testing
	                    System.out.println("Done");
	                    Login.FriendshipGraph.addVertex(userName);
	                    
	                   
	                } catch (SQLException sqlException) {
	                    sqlException.printStackTrace();
	                }
					
				}
		});
	}

	private void createcomponent() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 458, 481);
		
		getContentPane().setBackground(SystemColor.window);
		
		lblFakebook = new JLabel("Sign Up \n");
		lblFakebook.setBackground(UIManager.getColor("Desktop.background"));
		lblFakebook.setBounds(139, 69, 220, 59);
		lblFakebook.setHorizontalAlignment(SwingConstants.CENTER);
		lblFakebook.setForeground(SystemColor.activeCaptionText);
		lblFakebook.setFont(new Font("Helvetica Neue", Font.PLAIN, 47));
		
		textname = new JTextField();
		textname.setBounds(157, 165, 170, 51);
		textname.setBackground(Color.WHITE);
		textname.setHorizontalAlignment(SwingConstants.LEFT);
		textname.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		textname.setForeground(Color.GRAY);
		textname.setText("Name");
		
		passwordField = new JPasswordField();
		passwordField.setBounds(157, 323, 170, 51);
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		passwordField.setForeground(Color.GRAY);
		
		passwordField.setText("Password");
		//passwordField.echoCharIsSet("Password");
		
		btnSignup = new JButton("Sign up");
		
		
		btnSignup.setForeground(Color.WHITE);
		btnSignup.setBackground(new Color(0, 128, 0));
		btnSignup.setOpaque(true);
		btnSignup.setBorderPainted(false);
		btnSignup.setBounds(182, 403, 132, 38);
	
		btnSignup.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		
		lblfb = new JLabel("It’s quick and easy.");
		lblfb.setFont(new Font("Avenir", Font.PLAIN, 15));
		lblfb.setBounds(157, 128, 192, 16);
		getContentPane().setLayout(null);
		getContentPane().add(textname);
		getContentPane().add(passwordField);
		getContentPane().add(btnSignup);
		getContentPane().add(lblFakebook);
		getContentPane().add(lblfb);
		
		textUsername = new JTextField();
		textUsername.setBounds(157, 241, 170, 51);
		getContentPane().add(textUsername);
		textUsername.setForeground(Color.GRAY);
		textUsername.setText("Username");
		
		BlueFb = new JTextField();
		BlueFb.setEnabled(false);
		BlueFb.setEditable(false);
		BlueFb.setBackground(UIManager.getColor("Desktop.background"));
		BlueFb.setBounds(-16, -52, 489, 119);
		getContentPane().add(BlueFb);
		BlueFb.setColumns(10);
		
	}
}

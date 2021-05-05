package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import source.Facebook.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import source.Graph.*;


public class Login extends JFrame {
	private JPasswordField passwordField;
	private JTextField textUsername;
	private JLabel lblFakebook;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JButton btnSignup;
	private JButton btnLogin;
	private MyProfile profileObj = null;
	public static UndirectedGraph<String> FriendshipGraph = new UndirectedGraph<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		// for mysql SET @@global.time_zone = '+00:00';
		setTitle("Fakebook");
		setResizable(false);
		
		createDatabase();
		createcomponent();
		createEvent();
		
	}

	private void createDatabase() {
		String[] userAccount = {"Admin", "Cat1", "Fish1", "Dog1", "Lion1", "Elephant1", "Bird1" };
		String[] names = {"Admin", "Cat", "Fish", "Dog", "Lion", "Elephant", "Bird" };
		String[] userPassword = {"Admin123","Cat123", "Fish123", "Dog123", "Lion123", "Elephant123", "Bird123" };
		String[] status = { "Online", "Online", "Offline", "Busy", "Online", "Offline", "Busy" };
		String[] genders = { "Female", "Male", "Female", "Female", "Male", "Female", "Male" };
		String[] picPath = { "admin.png","cat.jpg","fish.jpg","dog.png", "lion.png","elephant.jpg", "bird.png" };
		
	/*	for(int i=0; i<userAccount.length; i ++) {
			File newPic = new File("src/resources/" + picPath[i]);
			String picPath1 = newPic.getAbsolutePath();
		    try {
		    
                Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
                    "root", "Conheoanh123");

                PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("replace into profile  (username, password, name, status, gender, profilePic) values (?,?,?,?,?,?)");

                st.setString(1, userAccount[i]);
                st.setString(2, userPassword[i]);
                st.setString(3, names[i]);
                st.setString(4, status[i]);
                st.setString(5, genders[i]);

				try {
					InputStream is = new FileInputStream(new File(picPath1));
					   st.setBlob(6,is);
		               st.execute();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

                //JOptionPane.showMessageDialog(btnSignup, "Account created successfully");
                st.close();
                
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
			
		}
		
		//generate pictures
//		File newPic = new File("src/resources/dog.png");
//		String picPath1 = newPic.getAbsolutePath();
//		// Upload picture to mysql dataabase
//		try{
//			  	Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
//		                  "root", "Conheoanh123");
//			  	PreparedStatement st = (PreparedStatement) connection.prepareStatement("UPDATE profile SET profilePic= ?  WHERE username = ?");
//		      InputStream is = new FileInputStream(new File(picPath1));
//		      st.setBlob(1,is);
//		      st.setString(2, "Dog1");
//		      st.executeUpdate();
//		  }catch(Exception ex){
//		      ex.printStackTrace();
//		  }

		*/
		// Generate facebook profile's friend data
		for (int i = 0; i < userAccount.length; i++) {
			FriendshipGraph.addVertex(userAccount[i]);
		}
	
		
//		// Generate dummy friendship
//		insertRequest("Cat1", "Fish1");
//		insertRequest("Cat1", "Dog1");
//		insertRequest("Cat1", "Lion1");
//		insertRequest("Lion1",  "Cat1");
//		insertRequest("Lion1", "Bird1");
//		insertRequest("Elephant1", "Fish1");
//		insertRequest("Dog1", "Fish1");
//		insertRequest("Dog1", "Elephant1");
//		insertRequest("Lion1", "Dog1");
//		
		//Loading friendship from mysql database
		try {
			Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
					"root", "Conheoanh123");

			PreparedStatement st = (PreparedStatement) connection
					.prepareStatement("select * from friendship");

			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				FriendshipGraph.addEdge(rs.getString("username"), rs.getString("friendname"));
			}	

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		
		System.out.println("Tracking friendship using graph");
		FriendshipGraph.displayEdges();
		
	}
	
	// Insert friendship into database and updated graph
			public void insertRequest(String userName, String friendName) {	
				FriendshipGraph.addEdge(userName, friendName);
				try {
					Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root",
							"Conheoanh123");
					PreparedStatement st = (PreparedStatement) connection
							.prepareStatement("INSERT IGNORE into friendship SET userName=?, friendName=?");

					st.setString(1, userName);
					st.setString(2, friendName);

					st.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		// Delete friendship into database and updated graph
			public void deleteRequest(String userName, String friendName) {	
				FriendshipGraph.removeEdge(userName, friendName);
				try {
					Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root",
							"Conheoanh123");
					PreparedStatement st = (PreparedStatement) connection
							.prepareStatement("DELETE from friendship where userName=? AND friendName=?");

					st.setString(1, userName);
					st.setString(2, friendName);

					st.execute();
					

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
			

	private void createEvent() {
		// Login action
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String userName = textUsername.getText();
				String password =  passwordField.getText();
				User user = null;
			
                try {
                	int count = 0;
                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
                        "root", "Conheoanh123");

                    PreparedStatement st = (PreparedStatement) connection
                        .prepareStatement("Select username, password, name, status from profile where username=? and password=?");

                    st.setString(1, userName);
                    st.setString(2, password);
               
                
                    ResultSet rs = st.executeQuery();
                   
                    while (rs.next()) {
                    	count++;
                    	//	user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                    	//System.out.println(user.getName());
                    }
                    
                    if(userName.equals("Admin")) {
                    	if(count ==1) {
                    		  dispose();
                              JOptionPane.showMessageDialog(btnLogin, "You have successfully logged in as Admin");     
                              profileObj= new MyProfile();
                              profileObj.setVisible(true);
                    	}
                    	else {
	                        JOptionPane.showMessageDialog(btnLogin, "Account and Password does not match");
                    	}
                    }
                    else {
                    	if(count ==1) {
                    		dispose();
                    		JOptionPane.showMessageDialog(btnLogin, "You have successfully logged in");
                    		FriendshipGraph.displayNeighbor(userName);
                    		profileObj= new MyProfile(userName,password);
                            profileObj.setVisible(true);
                           
                    	}
                    	else {
                        JOptionPane.showMessageDialog(btnLogin, "Wrong Username & Password");
                        JOptionPane.showMessageDialog(btnLogin, "Don't have an account? Sign Up");
                    	}
                    }
                }catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }				
			}
		});
		
		// Signup action
		btnSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Signup signup = new Signup();
				signup.setVisible(true);
			}
		});
		
		
	}
	

	private void createcomponent() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 458, 481);
		
		getContentPane().setBackground(UIManager.getColor("Desktop.background"));
		
		lblFakebook = new JLabel("Fakebook");
		lblFakebook.setHorizontalAlignment(SwingConstants.CENTER);
		lblFakebook.setForeground(Color.WHITE);
		lblFakebook.setFont(new Font("Hiragino Maru Gothic ProN", Font.PLAIN, 44));
		
		textUsername = new JTextField();
		textUsername.setBackground(Color.WHITE);
		textUsername.setHorizontalAlignment(SwingConstants.CENTER);
		textUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		textUsername.setToolTipText("");
		textUsername.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField.setToolTipText("");
		
		lblUsername = new JLabel("Username: ");
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setFont(new Font("Hiragino Kaku Gothic Pro", Font.BOLD, 15));
		lblUsername.setLabelFor(textUsername);
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Hiragino Kaku Gothic Pro", Font.BOLD, 15));
		lblPassword.setLabelFor(passwordField);
		
		btnLogin = new JButton("Login");
		
		btnLogin.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		
		btnSignup = new JButton("Sign up");
	
		btnSignup.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGap(80)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUsername)
								.addComponent(lblPassword))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(textUsername, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
								.addComponent(passwordField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
								.addComponent(btnSignup, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap(120, Short.MAX_VALUE)
							.addComponent(lblFakebook, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)))
					.addGap(117))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(184)
					.addComponent(btnLogin)
					.addContainerGap(191, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(42)
					.addComponent(lblFakebook, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addGap(40)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(textUsername, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addGap(54)
					.addComponent(btnLogin)
					.addGap(29)
					.addComponent(btnSignup)
					.addContainerGap(50, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);		
	}
}

package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import source.Facebook.User;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import source.Facebook.User;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLayeredPane;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JEditorPane;
import javax.swing.border.EtchedBorder;

public class UserProfile extends JFrame {
	private User userAccount;
	private  String friendAccount;
	private  String friendPassword;
	
	private JPanel contentPane;
	private JLabel lblFakebook;
	private JLabel lblName;
	private User user = null;  
	private User friend = null;
	private JButton btnLogout;
	
	private JLabel ImgProfile;
	private ImageIcon format = null;
	String fileName = null;
	Blob person_img = null;
	private JTextField friendSearch;
	private JDesktopPane desktopPane;
	public static JLabel lblStatus;
	private JButton btnViewFriend;
	private String picPath;
	private Connection connection = null;
	private JButton btnUnfriend;
	private JButton btnAddFriend;
	private JButton btnHome;
	private JLabel lblGender;
	
	//64, 374, 117, 29

	/**
	 *	private JButton btnAddFriend;
	private JButton btnUnfr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Profile newProfile = new Profile();
					UserProfile frame = new UserProfile();
					frame.setVisible(true);
					System.out.println("Hehe");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UserProfile() {
		createProfile();
		createcomponent();
		createEvent();	
	}
	
	public UserProfile(User userAccount, String friendAccount, String friendPassword) {
		this.userAccount = userAccount;
		this.friendAccount = friendAccount;
		this.friendPassword = friendPassword;
		createProfile();
		createcomponent();
		createEvent();
	}

	private void createProfile() {
		try {
               	connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
                   "root", "Conheoanh123");

               PreparedStatement st = (PreparedStatement) connection
                   .prepareStatement("Select * from profile");
           
               ResultSet rs = st.executeQuery();
              
               while (rs.next()) {
            	   if(rs.getString("username").equals(friendAccount) && rs.getString("password").equals(friendPassword)) {
            		 	user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            	   }
               }
		} catch (SQLException sqlException) {
            sqlException.printStackTrace();
		}
	}
		

	private void createEvent() {
		
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyProfile obj = new MyProfile(userAccount.getUserAccount(),userAccount.getPassword());
				dispose();
				obj.setVisible(true);				
			}
		});
		
		// Log out
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Login login = new Login();
				login.setVisible(true);
				
			}
		});
		
		// unfriend
				btnUnfriend.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						deleteRequest(userAccount.getUserAccount(), friendAccount);
						btnAddFriend.setVisible(true);
						btnUnfriend.setVisible(false);
					}
				});

				btnAddFriend.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						insertRequest(userAccount.getUserAccount(), friendAccount);
						btnAddFriend.setVisible(false);
						btnUnfriend.setVisible(true);
					}
				});
		
		// Friend search bar
		friendSearch.addFocusListener(new FocusListener() {
		@Override
		    public void focusGained(FocusEvent e) {
		        if (friendSearch.getText().equals(user.getName())) {
		            friendSearch.setText("");
		            friendSearch.setForeground(Color.BLACK);
		        
		            // Enter to search friend 
		    		friendSearch.addKeyListener(new KeyAdapter() {
		    			public void keyPressed(KeyEvent e) {
		    				if (e.getKeyCode()==KeyEvent.VK_ENTER){
		    				     try {
		    				    	 String userSearch = friendSearch.getText();
		    				    	 System.out.println(userSearch);
		    		                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
		    		                        "root", "Conheoanh123");

		    		                    PreparedStatement st = (PreparedStatement) connection
		    		                        .prepareStatement("Select username, password from profile where name=?");

		    		                    st.setString(1, userSearch);		                
		    		                    ResultSet rs = st.executeQuery();
		    		                    
		    		                    if(rs.next()) {
		    		                    	   System.out.println(rs.getString(1) + rs.getString(2));
		    		                    		UserProfile obj = new UserProfile(user,rs.getString(1), rs.getString(2));
		    				                    obj.setVisible(true);
		    		                    	
		    		                    }                  
		    				     	} catch (Exception exception) {
		    				     		exception.printStackTrace();
		    				     	
		    				     	}
		    				     }
		    				}
		    		});
		        }
		    }
		 @Override
		    public void focusLost(FocusEvent e) {
		        if (friendSearch.getText().isEmpty()) {
		            friendSearch.setForeground(Color.GRAY);
		            friendSearch.setText(user.getName());
		        }
		    }				
		    });	
		
		
		// view friends
		btnViewFriend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				friendDisplay2 obj = new friendDisplay2(userAccount, user);
				obj.setVisible(true);
			}
			});
	
	}
	
	private void createcomponent() {
		setTitle("Fakebook");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 458, 481);
		
		getContentPane().setBackground(UIManager.getColor("InternalFrame.background"));
		

		lblFakebook = new JLabel("Fakebook");
		lblFakebook.setHorizontalAlignment(SwingConstants.CENTER);
		lblFakebook.setForeground(Color.WHITE);
		lblFakebook.setFont(new Font("Hiragino Maru Gothic ProN", Font.PLAIN, 44));
		
		
		lblName = new JLabel("Profile");
		lblName.setBounds(184, 277, 94, 16);
		lblName.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setText(user.getName());
		
		btnLogout = new JButton("Log out");
		btnLogout.setBounds(375, 425, 77, 28);
		btnLogout.setBackground(UIManager.getColor("Desktop.background"));
		btnLogout.setOpaque(true);
		btnLogout.setBorderPainted(false);
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setIcon(new ImageIcon(UserProfile.class.getResource("/resources/icons8-facebook-16.png")));
		
		JDesktopPane destopFB = new JDesktopPane();
		destopFB.setBounds(142, 81, 171, 178);
		
		
		ImgProfile = new JLabel("");
		ImgProfile.setBounds(-63, 0, 297, 184);
		try {
              Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
                  "root", "Conheoanh123");

              PreparedStatement st = (PreparedStatement) connection
                  .prepareStatement("select * from profile where username = ?");

              st.setString(1, friendAccount);
              
              ResultSet rs = st.executeQuery();
              if(rs.next()) {
            	  person_img =  rs.getBlob(6);
            	  try {
					processPic(person_img);
				} catch (IOException e) {
					e.printStackTrace();
				}
              }
             st.close();
              
          } catch (SQLException sqlException) {
              sqlException.printStackTrace();
        	  Image imgPicture = new ImageIcon(this.getClass().getResource("/resources/unknownPic.jpg")).getImage();
      		  ImgProfile.setIcon(
      				new ImageIcon(imgPicture.getScaledInstance(ImgProfile.getWidth(), ImgProfile.getHeight(), Image.SCALE_DEFAULT)));
          }

		destopFB.add(ImgProfile);
		getContentPane().setLayout(null);
		getContentPane().add(btnLogout);
		getContentPane().add(lblName);
		getContentPane().add(destopFB);
		
		desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 463, 65);
		getContentPane().add(desktopPane);
		
		friendSearch = new JTextField();
		friendSearch.setBounds(80, 22, 300, 22);
		desktopPane.add(friendSearch);
		friendSearch.setHorizontalAlignment(SwingConstants.CENTER);
		friendSearch.setOpaque(true);
		friendSearch.setBackground(Color.white);
		friendSearch.setColumns(10);
		friendSearch.setForeground(Color.GRAY);
		friendSearch.setText(user.getName());
		
		
		
		btnViewFriend = new JButton("View Friend");
		btnViewFriend.setBounds(292, 374, 117, 29);
		getContentPane().add(btnViewFriend);
		
		lblStatus = new JLabel(user.getCurrentStatus());
		lblStatus.setBounds(39, 337, 370, 47);
		lblStatus.setBackground(SystemColor.window);
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font("Savoye LET", Font.ITALIC, 24));
		getContentPane().add(lblStatus);
		
		btnAddFriend = new JButton("Add Friend");
		btnAddFriend.setBounds(69, 374, 117, 29);
		getContentPane().add(btnAddFriend);
		btnAddFriend.setVisible(false);
		
		btnUnfriend = new JButton("Unfriend");
		btnUnfriend.setBounds(69, 374, 117, 29);
		getContentPane().add(btnUnfriend);
		btnUnfriend.setVisible(false);
		getContentPane().add(lblStatus);
		
		
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font("Savoye LET", Font.ITALIC, 24));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font("Savoye LET", Font.ITALIC, 24));
		getContentPane().add(lblStatus);
		
		btnHome = new JButton("");
		btnHome.setBounds(0, 412, 50, 41);
		btnHome.setIcon(new ImageIcon(UserProfile.class.getResource("/resources/homene (1).jpg")));
		btnHome.setOpaque(true);
		btnHome.setBorderPainted(false);	
		getContentPane().add(btnHome);
		
		
		
		lblGender = new JLabel(user.getGender());
		lblGender.setFont(new Font("STKaiti", Font.PLAIN, 14));
		lblGender.setHorizontalAlignment(SwingConstants.CENTER);
		lblGender.setBounds(194, 305, 61, 16);
		getContentPane().add(lblGender);
		

		boolean isFriend = Login.FriendshipGraph.hasEdge(userAccount.getUserAccount(), friendAccount);
		if (isFriend) {
			btnUnfriend.setVisible(true);
			btnAddFriend.setVisible(false);	
		}
		else {
			if(!(userAccount.equals(friendAccount))){
				btnAddFriend.setVisible(true);
				btnUnfriend.setVisible(false);
			}
			else {
				btnAddFriend.setVisible(false);
				btnUnfriend.setVisible(false);
			}
		}
	}
	
	// Process Pic to retrieve from my sql
	private void processPic(Blob picture) throws IOException {  
		try {
			 InputStream is = picture.getBinaryStream(1, picture.length());
			 BufferedImage imag = ImageIO.read(is);
			 Image image = imag;
             ImageIcon icon =new ImageIcon(image.getScaledInstance(ImgProfile.getWidth(), ImgProfile.getHeight(), Image.SCALE_DEFAULT));
             ImgProfile.setIcon(icon); 
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
	}
	
	// Delete friendship into database and updated graph
		public void deleteRequest(String userName, String friendName) {
			Login.FriendshipGraph.removeEdge(userName, friendName);
			try {
				Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
						"root", "Conheoanh123");
				PreparedStatement st = (PreparedStatement) connection
						.prepareStatement("DELETE from friendship where userName=? AND friendName=?");

				st.setString(1, userName);
				st.setString(2, friendName);
				st.execute();

				PreparedStatement st2 = (PreparedStatement) connection
						.prepareStatement("DELETE from friendship where userName=? AND friendName=?");
				st2.setString(2, userName);
				st2.setString(1, friendName);
				st2.execute();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Insert friendship into database and updated graph
		public void insertRequest(String userName, String friendName) {
			Login.FriendshipGraph.addEdge(userName, friendName);
			try {
				Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
						"root", "Conheoanh123");
				PreparedStatement st = (PreparedStatement) connection
						.prepareStatement("INSERT IGNORE into friendship SET userName=?, friendName=?");

				st.setString(1, userName);
				st.setString(2, friendName);

				st.execute();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
}

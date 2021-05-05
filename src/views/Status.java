package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import source.Facebook.User;

import javax.swing.JDesktopPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Status extends JFrame {
	private String userName; 
	private JPanel contentPane;
	private JTextField textStatus;
	private JButton btnPost;
	private JButton btnCancel;

	public void setUserName(String name) {
		userName = name;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Status frame = new Status();
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
	
	public Status() {
		setTitle("Fakebook");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 377, 238);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 377, 36);
		contentPane.add(desktopPane);
		
		JLabel lblNewLabel = new JLabel("What's on your mind?");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblNewLabel.setBounds(24, 52, 180, 36);
		contentPane.add(lblNewLabel);
		
		textStatus = new JTextField();
		lblNewLabel.setLabelFor(textStatus);
		textStatus.setBounds(24, 88, 327, 70);
		contentPane.add(textStatus);
		textStatus.setColumns(10);
		
		btnPost = new JButton("Post");
		btnPost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
	               	Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
	                   "root", "Conheoanh123");
	               	
	                PreparedStatement st = (PreparedStatement) connection
	                        .prepareStatement("UPDATE profile SET status = ?  WHERE username = ?");
	      
	           
	              st.setString(1, textStatus.getText());
	              st.setString(2, userName);
	            
	              st.execute();
	              
	              // Update on profile
	              PreparedStatement st2 = (PreparedStatement) connection
	                        .prepareStatement("SELECT status from profile WHERE username = ?");
       
	              st2.setString(1, userName);
	              ResultSet rs = st2.executeQuery();
	              if(rs.next()) {
		              MyProfile.lblStatus.setText(rs.getString("status"));       
	              }
	              
	              dispose();
		            	
			} catch (SQLException sqlException) {
	            sqlException.printStackTrace();
			}
				
			}
		});
		btnPost.setBounds(302, 182, 52, 28);
		contentPane.add(btnPost);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(235, 182, 72, 28);
		contentPane.add(btnCancel);
	}

}

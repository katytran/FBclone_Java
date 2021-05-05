package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
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

import source.Facebook.User;
import source.Graph.*;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class UpdateInfo extends JFrame {
	private String username;
	private String password;
	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField textname;
	private JButton btnUpdate;
	private JTextField BlueFb;
	private JPasswordField confirmedPass;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnFemale;
	private String gender;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateInfo frame = new UpdateInfo();
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
	public UpdateInfo() {
		createcomponent();
		createEvent();
	}
	
	public UpdateInfo(String username, String password) {
		this.username = username;
		this.password = password;
		createcomponent();
		createEvent();
	}

	private void createEvent() {
		
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
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			String name = textname.getText();	
						password  = passwordField.getText();
			String confPass = confirmedPass.getText();

			if (rdbtnFemale.isSelected()){
				gender = "Female";
			}
			else
				gender = "Male";
			
			if (!password.equals(confPass))
				JOptionPane.showMessageDialog(btnUpdate, "Password does not match. Please try again");
			else {
				try {
                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
                        "root", "Conheoanh123");

                    PreparedStatement st = (PreparedStatement) connection
                        .prepareStatement("UPDATE profile SET password=?, name=?, gender=? WHERE username = ?");
                    
                    st.setString(1, password);
                    st.setString(2, name);
                    st.setString(3, gender);
                    st.setString(4, username);
                    st.execute();
                    st.close();
                    
                    JOptionPane.showMessageDialog(btnUpdate, "Information successfully updated");
                   
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }	
			}
			}
		});
	}

	private void createcomponent() {
		setTitle("Update Profile");
		getContentPane().setEnabled(false);	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 419, 484);
		getContentPane().setBackground(SystemColor.window);
		
		textname = new JTextField();
		textname.setBounds(119, 89, 170, 51);
		textname.setBackground(Color.WHITE);
		textname.setHorizontalAlignment(SwingConstants.LEFT);
		textname.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		textname.setForeground(Color.GRAY);
		textname.setText("Name");
		
		passwordField = new JPasswordField();
		passwordField.setBounds(119, 177, 170, 51);
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		passwordField.setForeground(Color.GRAY);
		
		passwordField.setText("Password");
		btnUpdate = new JButton("Update");
		
		
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setBackground(new Color(0, 128, 0));
		btnUpdate.setOpaque(true);
		btnUpdate.setBorderPainted(false);
		btnUpdate.setBounds(140, 418, 132, 38);
	
		btnUpdate.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		getContentPane().setLayout(null);
		getContentPane().add(textname);
		getContentPane().add(passwordField);
		getContentPane().add(btnUpdate);
		
		BlueFb = new JTextField();
		BlueFb.setEnabled(false);
		BlueFb.setEditable(false);
		BlueFb.setBackground(UIManager.getColor("Desktop.background"));
		BlueFb.setBounds(-16, -52, 489, 119);
		getContentPane().add(BlueFb);
		BlueFb.setColumns(10);
		
		confirmedPass = new JPasswordField();
		confirmedPass.setText("Password");
		confirmedPass.setHorizontalAlignment(SwingConstants.LEFT);
		confirmedPass.setForeground(Color.GRAY);
		confirmedPass.setBounds(119, 271, 170, 51);
		getContentPane().add(confirmedPass);
		
		JLabel lblNewLabel = new JLabel("New password");
		lblNewLabel.setBounds(119, 149, 91, 16);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Confirmed password");
		lblNewLabel_1.setBounds(119, 243, 153, 16);
		getContentPane().add(lblNewLabel_1);
		
		JRadioButton rdbtnMale = new JRadioButton("Male");
		buttonGroup.add(rdbtnMale);
		rdbtnMale.setBounds(114, 333, 141, 23);
		getContentPane().add(rdbtnMale);
		
		rdbtnFemale = new JRadioButton("Female");
		buttonGroup.add(rdbtnFemale);
		rdbtnFemale.setToolTipText("");
		rdbtnFemale.setBounds(114, 368, 141, 23);
		getContentPane().add(rdbtnFemale);
		
		JButton btnHome = new JButton("");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyProfile obj = new MyProfile(username,password);
				dispose();
				obj.setVisible(true);
			}
		});
		// btnHome.setBackground(new Color(238, 238, 238));
		btnHome.setOpaque(true);
		btnHome.setBorderPainted(false);
		btnHome.setIcon(new ImageIcon(UpdateInfo.class.getResource("/resources/homene (1).jpg")));
		btnHome.setBounds(0, 433, 29, 29);
		getContentPane().add(btnHome);
		
		try {
			Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root",
					"Conheoanh123");

			PreparedStatement st = (PreparedStatement) connection
					.prepareStatement("Select gender from profile where username = ? ");

			st.setString(1, username);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				System.out.println(username);
				gender = rs.getString("gender");
				System.out.println(gender);
				if (gender.equals("Female")){
					rdbtnFemale.setSelected(true);
				}
				else
					rdbtnMale.setSelected(true);					
				}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	
	}
}

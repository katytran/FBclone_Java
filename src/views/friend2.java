package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import source.Facebook.*;
import source.Graph.*;
import source.Graph.VertexInterface;

import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;

public class friend2 extends JFrame {
	private JLabel lblFakebook;
	public static DefaultListModel<User> friendModel = new DefaultListModel<User>();
	private User user = null;
	private User myfriend = null;
	private User friend = null;
	private JList friendList;
	private JLabel lblStatus;
	private JButton btnViewFriend;
	private JLabel lblProfilePic;
	private JLabel lblName;
	private JButton btnUnfriend;
	private JButton btnHome;
	private JButton btnAddFriend;
	private JLabel lblFriendList;
	private JLabel lblGender;

	/**
	 * 
	 * /** Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					friend2 frame = new friend2();
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
	public friend2() {

		createcomponent();
		createEvent();
	}

	public friend2(User user, User friend) {
		this.user = user;
		this.myfriend = friend;
		createcomponent();
		createEvent();
	}

	private void createcomponent() {
		setTitle("Fakebook");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 355);

		getContentPane().setBackground(SystemColor.window);

		lblFakebook = new JLabel("Fakebook");
		lblFakebook.setHorizontalAlignment(SwingConstants.CENTER);
		lblFakebook.setForeground(Color.WHITE);
		lblFakebook.setFont(new Font("Hiragino Maru Gothic ProN", Font.PLAIN, 44));
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 450, 42);
		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);

		JDesktopPane desktopPane_1 = new JDesktopPane();
		desktopPane_1.setBounds(250, 59, 164, 157);
		getContentPane().add(desktopPane_1);

		lblProfilePic = new JLabel("");
		lblProfilePic.setBounds(0, 0, 163, 159);
		desktopPane_1.add(lblProfilePic);

		btnUnfriend = new JButton("Unfriend");
		btnUnfriend.setBounds(221, 296, 100, 29);
		getContentPane().add(btnUnfriend);
		btnUnfriend.setVisible(false);

		btnHome = new JButton("");
		// btnHome.setBackground(new Color(238, 238, 238));
		btnHome.setOpaque(true);
		btnHome.setBorderPainted(false);
		btnHome.setIcon(new ImageIcon(friend2.class.getResource("/resources/homene (1).jpg")));
		btnHome.setBounds(0, 306, 29, 29);
		getContentPane().add(btnHome);

		btnViewFriend = new JButton("View Friend");
		btnViewFriend.setBounds(333, 296, 100, 29);
		getContentPane().add(btnViewFriend);
		btnViewFriend.setVisible(false);

		lblStatus = new JLabel("Status");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(307, 268, 61, 16);
		getContentPane().add(lblStatus);

		lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setBounds(297, 214, 68, 29);
		getContentPane().add(lblName);
		
		lblGender = new JLabel("Gender");
		lblGender.setHorizontalAlignment(SwingConstants.CENTER);
		lblGender.setBounds(307, 244, 61, 16);
		getContentPane().add(lblGender);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 73, 186, 221);
		getContentPane().add(scrollPane);
		
		btnAddFriend = new JButton("Add Friend");
		btnAddFriend.setBounds(219, 297, 102, 26);
		getContentPane().add(btnAddFriend);
		btnAddFriend.setVisible(false);
		
		lblFriendList = new JLabel("");
		lblFriendList.setBounds(22, 50, 186, 16);
		getContentPane().add(lblFriendList);
		lblFriendList.setHorizontalAlignment(SwingConstants.CENTER);
		lblFriendList.setOpaque(true);
		lblFriendList.setText(myfriend.getName() + "'s friends");

		friendList = new JList(friendModel);
		friendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		Iterator<VertexInterface<String>> i = Login.FriendshipGraph.getVertex(myfriend.getUserAccount()).getNeighborIterator();

		while (i.hasNext()) {
			VertexInterface<String> name = i.next();
			String name2 = name.toString();

			try {
				Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
						"root", "Conheoanh123");

				PreparedStatement st = (PreparedStatement) connection
						.prepareStatement("select * from profile where username = ?");

				st.setString(1, name2);

				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					friendModel.addElement(
							new User(rs.getString("username"), rs.getString("password"), rs.getString("name"),
									rs.getString("status"), rs.getString("gender"), rs.getBlob("profilePic")));
				}
				st.close();

			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}

		} // end while
		scrollPane.setViewportView(friendList);
		
		friendList.setSelectedIndex(0);
		if(!friendList.isSelectionEmpty()) {
			display();
		}


	}

	private void createEvent() {
	    btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				friendModel.removeAllElements();
				dispose();
				MyProfile obj = new MyProfile(user.getUserAccount(), user.getPassword());
				obj.setVisible(true);
			}
		});
	    
		// unfriend
		btnUnfriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteRequest(user.getUserAccount(), friend.getUserAccount());
				btnAddFriend.setVisible(true);
				btnUnfriend.setVisible(false);
			}
		});

		btnAddFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertRequest(user.getUserAccount(), friend.getUserAccount());
				btnAddFriend.setVisible(false);
				btnUnfriend.setVisible(true);
			}
		});

		// button viewfriend
		btnViewFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				friend = (User) friendList.getSelectedValue();
				FriendOfFriend viewFriend = new FriendOfFriend(user,friend);
				friendModel.removeAllElements();
				dispose();
				viewFriend.setVisible(true);
			}
		});

		// Render display only name
		friendList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof User) {
					((JLabel) renderer).setText(((User) value).getName());
				}
				return renderer;
			}
		});

		friendList.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				myJListMouseClicked(evt);
			}
		});
	}

	// Mouse click to check each friend
	private void myJListMouseClicked(java.awt.event.MouseEvent evt) {
		display();
	}

	// Process Pic to retrieve from my sql
	private void processPic(Blob picture) throws IOException {
		try {
			InputStream is = picture.getBinaryStream(1, picture.length());
			BufferedImage imag = ImageIO.read(is);
			Image image = imag;
			ImageIcon icon = new ImageIcon(
					image.getScaledInstance(lblProfilePic.getWidth(), lblProfilePic.getHeight(), Image.SCALE_DEFAULT));
			lblProfilePic.setIcon(icon);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}



	}
	private void display() {
		btnViewFriend.setVisible(true);
		friend = (User) friendList.getSelectedValue();
		try {
			processPic(friend.getProfilePicture());
		} catch (IOException e) {
			e.printStackTrace();
		}
		lblStatus.setText(friend.getCurrentStatus());
		lblName.setText(friend.getName());
		lblGender.setText(friend.getGender());
		
		if(Login.FriendshipGraph.hasEdge(user.getUserAccount(), friend.getUserAccount())){
			btnUnfriend.setVisible(true);
		}
		else {
			if(!(user.getUserAccount().equals(friend.getUserAccount()))){
				btnAddFriend.setVisible(true);
			}
			else {
				btnAddFriend.setVisible(false);
				btnUnfriend.setVisible(false);
			}
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

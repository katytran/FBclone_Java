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

public class MyProfile extends JFrame {
	private String userAccount;
	private String userPassword;

	private JPanel contentPane;
	private JLabel lblFakebook;
	private JLabel lblName;
	private User user = null;
	private JButton btnLogout;

	private JLabel ImgProfile;
	private ImageIcon format = null;
	String fileName = null;
	Blob person_img = null;
	private JTextField friendSearch;
	private JDesktopPane desktopPane;
	public static JLabel lblStatus;
	private JButton btnViewFriend;
	private JButton btnChgStatus;
	private JButton btnEditProfile;
	private Connection connection = null;
	private String picPath;
	private JLabel lblGender;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Profile newProfile = new Profile();
					MyProfile frame = new MyProfile();
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
	public MyProfile() {
		createProfile();
		createcomponent();
		createEvent();
	}

	public MyProfile(String userAccount, String userPassword) {
		this.userAccount = userAccount;
		this.userPassword = userPassword;

		createProfile();
		createcomponent();
		createEvent();
	}

	private void createProfile() {
		try {
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root",
					"Conheoanh123");

			PreparedStatement st = (PreparedStatement) connection
					.prepareStatement("Select * from profile");

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if (rs.getString("username").equals(userAccount) && rs.getString("password").equals(userPassword)) {
					user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				}
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	private void createEvent() {
		// Log out
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Login login = new Login();
				login.setVisible(true);

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
							if (e.getKeyCode() == KeyEvent.VK_ENTER) {
								try {
									String userSearch = friendSearch.getText();
									if (!userSearch.equals(user.getName())) {
										Connection connection = (Connection) DriverManager.getConnection(
												"jdbc:mysql://localhost:3306/login", "root", "Conheoanh123");

										PreparedStatement st = (PreparedStatement) connection.prepareStatement(
												"Select username, password from profile where name=?");

										st.setString(1, userSearch);
										ResultSet rs = st.executeQuery();

										if (rs.next()) {
											UserProfile obj = new UserProfile(user,rs.getString(1), rs.getString(2));
											dispose();
											obj.setVisible(true);
											
										}
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

		// Change profile
		ImgProfile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "JPG", "GIF", "gif",
						"JPEG", "png", "PNG");
				fileChooser.setFileFilter(filter);
				fileChooser.addChoosableFileFilter(filter);
				int result = fileChooser.showSaveDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String path = selectedFile.getAbsolutePath();
					ImageIcon MyImage = new ImageIcon(path);
					Image img = MyImage.getImage();
					Image newImage = img.getScaledInstance(ImgProfile.getWidth(), ImgProfile.getHeight(),
							Image.SCALE_SMOOTH);
					ImageIcon image = new ImageIcon(newImage);

					ImgProfile.setIcon(image);
					picPath = path;
					System.out.println("Path of cat: " + picPath);

					System.out.println("path cat direct" + MyProfile.class.getResource("/resources/cat.jpg"));
				} else if (result == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(fileChooser, "No picture selected");
				}

				// Upload picture to mysql dataabase
				try {
					Connection connection = (Connection) DriverManager
							.getConnection("jdbc:mysql://localhost:3306/login", "root", "Conheoanh123");
					PreparedStatement st = (PreparedStatement) connection
							.prepareStatement("UPDATE profile SET profilePic= ?  WHERE username = ?");
					InputStream is = new FileInputStream(new File(picPath));
					st.setBlob(1, is);
					st.setString(2, userAccount);
					st.executeUpdate();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});

		// update status
		btnChgStatus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Status newStatus = new Status();
				newStatus.setUserName((user.getUserAccount()));
				newStatus.setVisible(true);

			}
		});

		// update info
		btnEditProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateInfo obj = new UpdateInfo(userAccount, userPassword);
				dispose();
				obj.setVisible(true);
			}
		});

		// view friends
		btnViewFriend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				friendDisplay obj = new friendDisplay(user);
				dispose();
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
		lblName.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblName.setBounds(184, 277, 94, 16);
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setText(user.getName());

		btnLogout = new JButton("Log out");
		btnLogout.setBounds(375, 425, 77, 28);
		btnLogout.setBackground(UIManager.getColor("Desktop.background"));
		btnLogout.setOpaque(true);
		btnLogout.setBorderPainted(false);
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setIcon(new ImageIcon(MyProfile.class.getResource("/resources/icons8-facebook-16.png")));

		JDesktopPane destopFB = new JDesktopPane();
		destopFB.setBounds(142, 81, 171, 178);

		ImgProfile = new JLabel("");
		ImgProfile.setBounds(-63, 0, 297, 184);
		try {
			Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
					"root", "Conheoanh123");

			PreparedStatement st = (PreparedStatement) connection
					.prepareStatement("select * from profile where username = ?");

			st.setString(1, userAccount);

			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				person_img = rs.getBlob(6);
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
			ImgProfile.setIcon(new ImageIcon(
					imgPicture.getScaledInstance(ImgProfile.getWidth(), ImgProfile.getHeight(), Image.SCALE_DEFAULT)));
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

		btnEditProfile = new JButton("Update Info");
		btnEditProfile.setBounds(28, 376, 117, 29);
		getContentPane().add(btnEditProfile);

		btnChgStatus = new JButton("Update Status");
		btnChgStatus.setBounds(169, 376, 117, 29);
		getContentPane().add(btnChgStatus);

		btnViewFriend = new JButton("View Friend");
		btnViewFriend.setBounds(307, 376, 117, 29);
		getContentPane().add(btnViewFriend);

		lblStatus = new JLabel(user.getCurrentStatus());
		lblStatus.setBackground(SystemColor.window);
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font("Savoye LET", Font.ITALIC, 25));
		lblStatus.setBounds(39, 329, 385, 45);
		getContentPane().add(lblStatus);
		
		lblGender = new JLabel(user.getGender());
		lblGender.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblGender.setHorizontalAlignment(SwingConstants.CENTER);
		lblGender.setBounds(184, 305, 84, 16);
		getContentPane().add(lblGender);

	}

	// Process Pic to retrieve from my sql
	private void processPic(Blob picture) throws IOException {
		try {
			InputStream is = picture.getBinaryStream(1, picture.length());
			BufferedImage imag = ImageIO.read(is);
			Image image = imag;
			ImageIcon icon = new ImageIcon(
					image.getScaledInstance(ImgProfile.getWidth(), ImgProfile.getHeight(), Image.SCALE_DEFAULT));
			ImgProfile.setIcon(icon);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	}
}

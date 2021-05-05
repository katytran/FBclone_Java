// @author Canh Hanh Chi Tran
// class Facebook 
package source.Facebook;

import java.sql.Blob;

public class User {
	private String userAccount = "";
	private String password = "";
	private String name = "";
	private String currentStatus = "Happy";
	private String gender = "";
	private Blob profilePicture = null;
	private ListInterface<User> friendLists = new AList<User>();

	// constructor
	public User(String userAccount, String password) {
		this.userAccount = userAccount;
		this.password = password;
		this.name = userAccount; 
	}

	public User(String userAccount, String password, String name, String currentStatus, String gender) {
		this.userAccount = userAccount;
		this.password = password;
		this.name = name;
		this.currentStatus = currentStatus;
		this.gender = gender;
	}
	
	public User(String userAccount, String password, String name, String currentStatus, String gender, Blob profilePic) {
		this.userAccount = userAccount;
		this.password = password;
		this.name = name;
		this.currentStatus = currentStatus;
		this.gender = gender;
		this.profilePicture = profilePic;
	}

	public String getGender() {
		return gender;
	}

	public Blob getProfilePicture() {
		return profilePicture;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setProfilePicture(Blob profilePicture) {
		this.profilePicture = profilePicture;
	}

	// Setter and getter
	public String getUserAccount() {
		return userAccount;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	// Method of User class

	// Return array of friendlist user.
	public Object[] getFriendList() {
		return friendLists.toArray();
	}

	// Add a friend to friendlist of user
	public void addFriend(User user) {
		if (!this.friendLists.contains(user))
			friendLists.add(user);
	}

	// Clear all friends of user
	public void removeAllFriend() {
		friendLists.clear();
	}

	// Remove friend from friendlist
	public boolean removeFriend(String name) {
		boolean found = false;
		int index = 1;
		while (!found && (index <= friendLists.getLength())) {
			if (friendLists.getEntry(index).getName().equals(name)) {
				friendLists.remove(index);
				found = true;
			}
			index++;
		} // end while
		return found;
	}

	// View friendist of user
	public void viewFriendList() {
		displayList(friendLists);
	}

	// Display friendlist of user
	private static void displayList(ListInterface<User> friendList) {
		System.out.println("Friend list has " + friendList.getLength() + " friend(s):");
		Object[] listArray = friendList.toArray();
		for (int i = 0; i < listArray.length; i++) {
			System.out.print(((User) listArray[i]).getName() + " ");
		}
		System.out.println();
	}

	// View profile of user
	public void viewProfile() {
		System.out.println("Name: " + name);
		System.out.println("Current status: " + currentStatus);
		System.out.printf(name + "'s ");
		displayList(friendLists);
	}

}

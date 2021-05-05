// @author Canh Hanh Chi Tran
// class Facebook 
package source.Facebook;

import java.util.Scanner;

import source.Graph.DirectedGraph;
import source.Graph.LinkedStack;
import source.Graph.StackInterface;
import source.Graph.UndirectedGraph;

public class Facebook {
	final static Scanner scanner = new Scanner(System.in);
	private ListInterface<User> userList = new LList<User>();
	private UndirectedGraph<String> FriendshipGraph = new UndirectedGraph<>();
	private StackInterface<String> path = new LinkedStack<>();

	Facebook() {

		// Create some Facebook profile
		String[] userAccount = { "Cat1", "Fish1", "Dog1", "Lion1", "Elephant1", "Bird1" };
		String[] names = { "Cat", "Fish", "Dog", "Lion", "Elephant", "Bird" };
		String[] userPassword = { "Cat123", "Fish123", "Dog123", "Lion123", "Elephant123", "Bird123" };
		String[] status = { "Online", "Offline", "Busy", "Online", "Offline", "Busy" };
		String[] genders = { "Female", "Male", "Female", "Female", "Male", "Female", "Male" };
		
		// Make user Admin
		userList.add(new User("Admin", "Admin123"));

		for (int i = 0; i < names.length; i++) {
			userList.add(new User(userAccount[i], userPassword[i], names[i], status[i]));
		}

		// Generate facebook profile's friend data
		for (int i = 0; i < names.length; i++) {
			FriendshipGraph.addVertex(userAccount[i]);
		}

		// Hardcode database
		User cat = checkUser("Cat");
		User fish = checkUser("Fish");
		User dog = checkUser("Dog");
		User lion = checkUser("Lion");
		User elephant = checkUser("Elephant");
		User bird = checkUser("Bird");

		FriendshipGraph.addEdge("Cat1", "Fish1");
		cat.addFriend(fish);
		fish.addFriend(cat);

		FriendshipGraph.addEdge("Cat1", "Dog1");
		cat.addFriend(dog);
		dog.addFriend(cat);
		
		FriendshipGraph.addEdge("Cat1", "Lion1");
		cat.addFriend(lion);
		lion.addFriend(cat);

		FriendshipGraph.addEdge("Fish1", "Lion1");
		fish.addFriend(lion);
		lion.addFriend(fish);

		FriendshipGraph.addEdge("Dog1", "Lion1");
		dog.addFriend(lion);
		lion.addFriend(dog);

		FriendshipGraph.addEdge("Lion1", "Elephant1");
		lion.addFriend(elephant);
		elephant.addFriend(lion);

		FriendshipGraph.addEdge("Fish1", "Bird1");
		fish.addFriend(bird);
		bird.addFriend(fish);

	}

	// Welcome message
	public void welcome() {
		System.out.println("Welcome to Facebook");
		System.out.println("Connect with friends and the world around you on Facebook.");
	}

	// Login
	public void login() {
		System.out.printf("\nUsername: ");
		String userName = scanner.nextLine();

		System.out.printf("Password: ");
		String userPassword = scanner.nextLine();

		// Check if userName is valid
		boolean userNameValid = false;
		boolean userAdmin = false;
		boolean userPassValid = false;
		User myUser = null;

		User adminUser = userList.getEntry(1);

		for (int i = 1; i < userList.getLength(); i++) {
			// check userName
			myUser = userList.getEntry(i);
			if (myUser.getUserAccount().equals(userName)) {
				userNameValid = true;
				// check userPassword
				if (userList.getEntry(i).getPassword().equals(userPassword)) {
					// return user object
					userPassValid = true;
					break;
				}
			}
		}

		if (myUser.equals(adminUser)) {
			adminOption(userList, myUser);
		}

		else if (userNameValid && userPassValid) {
			System.out.println("Welcome back to Facebook");
			userOption(myUser);
		} else if (userNameValid && !userPassValid) {
			System.out.println("Account password does not match. Please try again");
			login();
		}

		else {
			System.out.println("The email you’ve entered doesn’t match any account. Sign up for an account.");
			System.out.println("Sign Up. It’s quick and easy");
			createProfile();
		}

	}

	// Create profile
	public void createProfile() {
		System.out.printf("\nUsername: ");
		String userName = scanner.nextLine();

		System.out.printf("Password: ");
		String userPassword = scanner.nextLine();

		User newUser = new User(userName, userPassword);
		FriendshipGraph.addVertex(userName);
		userList.add(newUser);
		userOption(newUser);

	}

	// Admin option
	private void adminOption(ListInterface<User> userList, User user) {
		System.out.println("\n1. Display all Facebook user");
		System.out.println("2. Adding friendship between users");
		System.out.println("3. Removing friendship between users");
		System.out.println("4. Search a profile");
		System.out.println("5. Delete a profile");
		getAdminInputs(userList, user);
	}

	private void getAdminInputs(ListInterface<User> userList2, User user) {
		int input;
		while (true) {
			System.out.println("\nPlease choose option from 1 to 5, or choose 6 to exit.");
			input = scanner.nextInt();

			// Update profile
			if (input == 1)
				displayList();

			// addFriend
			if (input == 2)
				addingFriendship();

			// Remove friend
			if (input == 3)
				removingFriendship();

			// Search profile
			if (input == 4)
				searchProfile();

			// Delete profile
			if (input == 5) {
				deletingProfile();

			}

			if (input == 6) {
				System.out.println("\nClosing Facebook App ....");
				System.out.println("\nXXXXXXXXXXXXXXXXXXXXXXXXXX!");
				System.out.println("See you again!");
				return;
			}

		}

	}

	// Display list of Facebook user
	private void displayList() {
		System.out.println("Facebook has " + userList.getLength() + " user(s).");
		Object[] listArray = userList.toArray();
		for (int index = 1; index < listArray.length; index++) {
			System.out.print(((User) listArray[index]).getUserAccount() + " ");
		}
		System.out.println();
	}

	// Adding friendship
	private void addingFriendship() {
		System.out.println();
		System.out.println("Displaying all user in the system");
		displayList();
		scanner.nextLine();
		while (true) {
			System.out.println("--------- Setting Up friendship Between  <First user> and <Second user>  ---------");
			System.out.printf("First User: ");
			String user1 = scanner.nextLine();
			System.out.printf("Second User: ");
			String user2 = scanner.nextLine();

			makeFriendShip(user1, user2);

			System.out.println("Type \"Done\" to finish adding friendship");
			String end = scanner.nextLine();
			if (end.equals("Done"))
				break;
		}
	}
	
	// Making friendship
	public void makeFriendShip(String user1, String user2) {

		// Add user in Userlist
		User usern1 = checkUser(user1);
		User usern2 = checkUser(user2);

		usern1.addFriend(usern2);
		usern2.addFriend(usern1);

		// System.out.println("Adding friend in userlIst");
		// System.out.println("Dislaying " + user1 + "'s friendlist");
		// usern1.viewFriendList();

		// System.out.println("Dislaying " + user2 + "'s friendlist");
		// usern2.viewFriendList();
		System.out.println();
		System.out.println("Testing adding friend using graph");
		FriendshipGraph.addEdge(usern1.getUserAccount(), usern2.getUserAccount());
		FriendshipGraph.displayEdges();
	}

	// Removing friendship
	private void removingFriendship() {
		System.out.println();
		System.out.println("Displaying all user in the system");
		displayList();
		scanner.nextLine();
		while (true) {
			System.out.println("--------- Setting Up friendship Between  <First user> and <Second user>  ---------");
			System.out.printf("First User: ");
			String user1 = scanner.nextLine();
			System.out.printf("Second User: ");
			String user2 = scanner.nextLine();

			removeFriendShip(user1, user2);

			System.out.println("Type \"Done\" to finish setting friendship");
			String end = scanner.nextLine();
			if (end.equals("Done"))
				break;
		}

	}

	// Remove friendship
	private void removeFriendShip(String user1, String user2) {
		// Add user in Userlist
		// System.out.println("Removing in userlist");
		User usern1 = checkUser(user1);
		User usern2 = checkUser(user2);

		usern1.removeFriend(user2);
		usern2.removeFriend(user1);

		// Testing removing using userlist
		// System.out.println("Dislaying " + user1 + "'s friendlist");
		// usern1.viewFriendList();

		// System.out.println("Dislaying " + user2 + "'s friendlist");
		// usern2.viewFriendList();
		System.out.println();
		System.out.println("Testing removing using graph:");
		FriendshipGraph.removeEdge(usern1.getUserAccount(), usern2.getUserAccount());
		FriendshipGraph.displayEdges();
	}

	private void deletingProfile() {

		scanner.nextLine();
		System.out.println("\n---- Deleting a profile ----");
		System.out.printf("Input a profile's name: ");

		String nameProfile = scanner.nextLine();
		User userProfile = checkUserAccount(nameProfile);

		if (userProfile != null) {
			int userPos = getUserIndex(userProfile);
			if (userPos != 1) {
				userList.remove(userPos);
			} else
				System.out.println("Cannot delete admin profile");
		} else
			System.out.println(
					"\n!!!Invalid Facebook Account! Cannot remove [" + nameProfile + "] frome Facebook userList !!!\n");

	}

	// User choice list
	public void userOption(User user) {
		System.out.println("Facebook Account sucessfully created!");
		System.out.println("\n1. Update and edit the profile");
		System.out.println("2. Add new friend");
		System.out.println("3. Remove friend");
		System.out.println("4. View profile");
		System.out.println("5. Search profile");
		System.out.println("6. Delete profile");
		System.out.println("7. Get suggested friend");
		System.out.println("8. View friend's friendlist");
		System.out.println("9. Exit");
		getUserInputs(user);
	}

	// Input user choice
	private void getUserInputs(User user) {
		int input;
		while (true) {
			System.out.println("\nPlease choose option from 1 to 8, or choose 9 to exit.");
			input = scanner.nextInt();

			// Update profile
			if (input == 1)
				updateProfile(user);

			// addFriend
			if (input == 2)
				addFriend(user);

			// Remove friend
			if (input == 3)
				removeFriend(user);

			// View Profile
			if (input == 4)
				viewProfile(user);

			// Search profile
			if (input == 5)
				searchProfile();

			// Delete profile
			if (input == 6) {
				deleteProfile(user);
			}
			
			// Get suggested friend
			if (input == 7) {
				// Only works for this user.
				System.out.println("People you may know");
				int pos = getUserIndex(user);
				String name = userList.getEntry(pos).getUserAccount();
				FriendshipGraph.Traverse(name);
			}

			if (input == 8) {
				System.out.println("Showing your friendlist: ");
				user.viewFriendList();
				scanner.nextLine();
				System.out.println("Viewing friend's friendlist: ");
				String name = scanner.nextLine();
				User neighbor = checkUser(name);
				FriendshipGraph.displayNeighbor(neighbor.getUserAccount());
			}

			if (input == 9) {
				System.out.println("\nClosing Facebook App ....");
				System.out.println("\nXXXXXXXXXXXXXXXXXXXXXXXXXX!");
				System.out.println("See you again!");
				return;
			}
		}
	}

	
	// Update profile
	private void updateProfile(User user) {
		scanner.nextLine();
		System.out.println("Update your profile");
		System.out.printf("\nName: ");
		String name = scanner.nextLine();
		user.setName(name);

		System.out.printf("Current status: ");
		String currentStatus = scanner.nextLine();
		user.setCurrentStatus(currentStatus);
	}

	// Adding friend
	private void addFriend(User user) {
		scanner.nextLine();
		System.out.println("\n----Adding Friend----\n");
		System.out.printf("Input your friend's account: ");
		String friendName = scanner.nextLine();
		User friend = checkUser(friendName);

		if (friend == null) {
			System.out
					.println("\n!!!Invalid Facebook Account! Cannot add [" + friendName + "] to your friendList !!!\n");
		} else {
			user.addFriend(friend);
			friend.addFriend(user);
			System.out.println("\n" + friendName + " was sucessfully added to your friend list.");
			user.viewFriendList();

			// updated Graph
			String thisUsername = user.getUserAccount();
			FriendshipGraph.addEdge(thisUsername, friend.getUserAccount());
			FriendshipGraph.displayEdges();
		}
	}

	// Remove friend
	private void removeFriend(User user) {

		user.viewFriendList();
		User oldfriend = null;
		scanner.nextLine();
		System.out.println("\n---- Deleting a friend ----");
		System.out.printf("Input your friend's name: ");

		String friendName = scanner.nextLine();
		boolean result = user.removeFriend(friendName);

		if (result) {
			System.out.println("\nRemoving " + friendName + ".....");
			System.out.println("\n" + friendName + " was sucessfully removed from your friend list.");
			// Remove "this" accout from oldfriend's friendlist
			oldfriend = checkUser(friendName);
			oldfriend.removeFriend(user.getName());

			// Verify delete method works for both this.user and its friend
			user.viewFriendList();
			oldfriend.viewProfile();

			// updated Graph
			String thisUsername = user.getUserAccount();

			FriendshipGraph.removeEdge(thisUsername, oldfriend.getUserAccount());
			FriendshipGraph.displayEdges();
		}

		else {
			System.out.println("\n!!!Invalid Facebook Account!");
		}
	}

	// Search profile
	private void searchProfile() {
		scanner.nextLine();
		System.out.println("\n---- Search Facebook Account ----");
		System.out.printf("Username: ");
		String name1 = scanner.nextLine();
		System.out.println("\nSearching......");
		User fbProfile = checkUser(name1);

		if (fbProfile == null) {
			System.out.println("\nInvalid Account. No Result Found!");
		} else {
			System.out.println("\n---- Searching completed ----");
			fbProfile.viewProfile();
		}
	}

	// View profile
	private void viewProfile(User user) {
		System.out.println("\nLoading Your Facebook Profile....\n");
		user.viewProfile();
	}

	// Delete choice
	private void deleteProfile(User user) {
		System.out.println("\n---- Delete your profile ----");

		// Get this user Name
		String thisUser = user.getName();

		// REmove this user's name from all of it's friend's friendlist
		Object[] userFriend = user.getFriendList();

		for (int i = 0; i < userFriend.length; i++) {
			((User) userFriend[i]).removeFriend(thisUser);
		}

		// Clear all friends of user
		user.removeAllFriend();

		// Remove user from the Facebook's list of users
		int thisUserIndex = getUserIndex(user);
		System.out.println("\nYour profile was deleted!");
		System.out.println("Logging out");
		userList.remove(thisUserIndex);

		// Updated graph
		FriendshipGraph.removeVertex(user.getUserAccount());
		user = null;
		displayList();
		FriendshipGraph.displayEdges();
		login();
	}

	// Check user valid
	private User checkUserAccount(String userAccount) {
		int index = 1;
		while ((index <= userList.getLength())) {
			if (userList.getEntry(index).getUserAccount().equals(userAccount)) {
				return userList.getEntry(index);

			}
			index++;
		}
		return null;
	}

	// Check user valid
	private User checkUser(String userAccount) {
		int index = 1;
		while ((index <= userList.getLength())) {
			if (userList.getEntry(index).getName().equals(userAccount)) {
				return userList.getEntry(index);

			}
			index++;
		}
		return null;
	}

	// Get user index
	private int getUserIndex(User user) {
		int index = 1;
		while ((index < userList.getLength())) {
			if (userList.getEntry(index).equals(user)) {
				break;
			}
			index++;
		}
		if(index !=1)
			return index;
		else
			return -1;
	}

}

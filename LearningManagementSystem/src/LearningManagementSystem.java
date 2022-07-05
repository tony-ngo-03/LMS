import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class LearningManagementSystem {
	private Teacher teacher;
	private Student student;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);

		Introduction(sc);
//		Assignment assignment = new Assignment("Homework 1", 10.0, 2);
//		assignment.createQuestions();
//		System.out.println(assignment.toString());
//		assignment.takeAssignment();
//		assignment.correctVsStudent();
	}
	
	// introduction for the program
	// logs the user in as a teacher or a student/
	// pre: none
	// post: creates a new instance of the teacher or a student
	public static void Introduction(Scanner sc) throws IOException {
		System.out.println("Welcome to the LMS.");
		System.out.println("Please Select: ");
		System.out.println("Please type in 'log in' or 'create'");
		System.out.println("\nLOG IN\tCREATE ACCOUNT");
		String choice = sc.nextLine();
		File logins = createLogins();
		if (choice.toLowerCase().equals("log in")) {
			logIn(sc, logins);
		} else if (choice.toLowerCase().equals("create")) {
			System.out.print("please create a username: ");
			String createUsername = sc.nextLine();
			boolean canContinue = false;
			while (!canContinue) {
				if (canUseUsername(logins, createUsername)) {
					canContinue = true;
					System.out.println();
					System.out.print("please create a password: ");
					String createPassword = putIntoHashCode(sc.nextLine());

					FileWriter fileWriter = new FileWriter(logins.getName(), true);
					fileWriter.write(createUsername + "\t" + createPassword + "\n");
					System.out.println("Username and Password sucessfully created!");
					fileWriter.close();
				} else {
					System.out.println("This username is already taken");
					System.out.print("Please choose a new one: ");
					createUsername = sc.nextLine();
				}
			}

		} else {

		}

	}

	// helper method to determine if the attemptedUsername is already in use
	// pre: none
	// post: returns true if the username was not found in the logins file, false otherwise
	private static boolean canUseUsername(File file, String attemptedUsername)
			throws FileNotFoundException {
		Scanner fileScanner = new Scanner(file);
		while (fileScanner.hasNextLine()) {
			String currentLine = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(currentLine);
			String username = lineScanner.next();
			lineScanner.next();
			if (username.equals(attemptedUsername)) {
				fileScanner.close();
				lineScanner.close();
				return false;
			}
			lineScanner.close();
		}
		fileScanner.close();
		return true;
	}

	// checks the logins file for the username and password to log into the system
	// pre: none
	// post: logs into the system.
	private static void logIn(Scanner sc, File logins) throws FileNotFoundException {
		System.out.print("Enter Username: ");
		String loginUsername = sc.nextLine();
		System.out.println();
		System.out.print("Enter Password: ");
		String loginPassword = putIntoHashCode(sc.nextLine());
		if (canLogIn(loginUsername, loginPassword, logins)) {
			System.out.println("Log in Success!");
		} else {
			System.out.println("Log in Failed!");
			System.out.print("Would you like to try again?");
			String choice = sc.nextLine();
			if (choice.toLowerCase().charAt(0) == 'y') {
				logIn(sc, logins);
			} else {
				System.out.println("Okay! Thank you!");
			}
		}
	}
	
	// private helper method to scan the entire file for the username + password
	// pre: none
	// post: returns true if the username + password was found, false otherwise
	private static boolean canLogIn(String username, String password, File file)
			throws FileNotFoundException {
		boolean found = false;
		Scanner fileScanner = new Scanner(file);
		while (fileScanner.hasNext()) {
			String lineToScan = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(lineToScan);
			if (lineScanner.next().equals(username)) {
				if (lineScanner.next().equals(password)) {
					lineScanner.close();
					return true;
				}
			}
			lineScanner.close();
		}
		fileScanner.close();
		return found;
	}
	
	// creates the file to be used for username/password management
	// pre: none
	// post: returns a File
	private static File createLogins() throws IOException {
		File allLogins = new File("allLogins.txt");
		if (allLogins.createNewFile()) {
			System.out.println("DEBUGGING: created new file: " + allLogins.getName());
		} else {
			System.out.println("DEBUGGING! file already exists.");
		}

		return allLogins;
	}

	
	// private helper method to help 'encrypt' a password so that it is not obvious
	// pre: none
	// post: returns a string of the original password in hexidecimal.
	private static String putIntoHashCode(String change) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < change.length(); i++) {
			sb.append(Integer.toHexString((char) (change.charAt(i))));
		}
		return sb.toString();
	}
}

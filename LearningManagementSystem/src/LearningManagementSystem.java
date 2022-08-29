import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class LearningManagementSystem {
	private static Instructor instructor;
	private static Student student;
	private static Admin admin;

	private static String userFirstName;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);

		Introduction(sc);
		allChoices(sc, instructor, student, admin);
	}

	// cycle through all choices for the user to use
	// pre: either teacher OR student is null
	// post: returns nothing
	public static void allChoices(Scanner sc, Instructor instructor, Student student, Admin admin)
			throws IOException {
		if (instructor == null && student == null && admin == null) {
			throw new IllegalStateException(
					"There is neither an instructor, student, nor admin active!");
		}
		System.out.println("\n\n\n");
		String choice = "";

		System.out.println("Welcome, please select the option by typing the number.");
		while (!choice.equals("log out")) {
			// teacher choices
			if (admin != null) {
				System.out.println("1. Add Course to Instructor");
				System.out.println("2. Add Course to Student");
				System.out.println("3. Send Message to Student");
				System.out.println("4. Create a Course");
				System.out.println("5. Log out");
				System.out.print("\n> ");
				choice = sc.nextLine();

				if (choice.equals("1")) {
					admin.addCourseToInstructor();
				}
				if (choice.equals("2")) {
					admin.addCourseToStudent();
				}
				if (choice.equals("3")) {

				}
				if (choice.equals("4")) {
					admin.createCourse();
				} else {
					choice = "log out";
				}
			}
			if (instructor != null) {
				System.out.println("1. Send Message to Student");
				System.out.println("2. Create an assignment");
				System.out.println("3. Quit");
				choice = sc.nextLine();
				if (choice.equals("1")) {
					instructor.sendMessage();
				}
				if (choice.equals("2")) {
					instructor.createAssignment();
				} else {
					choice = "log out";
				}
			}
			if (student != null) {

			}

		}

	}

	// introduction for the program
	// logs the user in as a teacher or a student/
	// pre: none
	// post: creates a new instance of the teacher or a student
	public static void Introduction(Scanner sc) throws IOException {
		System.out.println("Welcome to the LMS.");
		System.out.println("Please only use numbers to answer questions unless otherwise prompted");
		System.out.println("Please Select:\n1. Login\n2. Create Account\n");
		System.out.print("CHOICE: ");
		String choice = sc.nextLine();
		while (!choice.equals("1") && !choice.equals("2")) {
			System.out.println("That is the wrong ");
		}
		File logins = createLogins();
		if (choice.toLowerCase().equals("1")) {
			logIn(sc, logins);
		} else if (choice.toLowerCase().equals("2")) {
			createAccount(sc, logins);

		} else {
			// TODO: create loop for not valid entry
		}

	}

	private static void createAccount(Scanner sc, File logins) throws IOException {
		// get first name
		userFirstName = getUserFirstName(sc);

		// get instructor or teacher
		System.out.println("What is your occupation?\n");
		System.out.println("1. Instructor");
		System.out.println("2. Student");
		System.out.println("3. Admin");
		System.out.print("> ");
		String userOccupationChoice = sc.nextLine();
		// error correcting
		while (!userOccupationChoice.equals("1") && !userOccupationChoice.equals("2")
				&& !userOccupationChoice.equals("3")) {
			System.out.println("That is not a valid choice. Please use numbers.");
			System.out.println("What is your occupation?\n");
			System.out.println("1. Instructor");
			System.out.println("2. Student");
			System.out.println("3. Admin");
			System.out.print("> ");
			userOccupationChoice = sc.nextLine();
		}
		if (userOccupationChoice.equals("1")) {
			userOccupationChoice = "instructor";
		} else if (userOccupationChoice.equals("2")) {
			userOccupationChoice = "student";
		} else {
			userOccupationChoice = "admin";
		}

		String[] credentials = new String[2];
		credentials = createCredentials(credentials, sc, logins);

		// Write credentials to file
		FileWriter fileWriter = new FileWriter(logins, true);
		fileWriter.append(credentials[0] + "\t" + credentials[1] + "\n");
		fileWriter.close();

		// create the actual username.txt
		createFirstFile(credentials[0], userOccupationChoice);
	}

	private static void createFirstFile(String username, String occupation) throws IOException {
		File newFile = new File(username + ".txt");
		if (newFile.createNewFile()) {
			System.out.println("File successfully created, " + userFirstName + "!");
		} else {
			System.out.println("File already exists! Please contact admin.");
		}
		FileWriter newFileWriter = new FileWriter(newFile, true);
		newFileWriter.append(userFirstName + "\n" + occupation + "\n");
		newFileWriter.close();

	}

	// asks the user for their log in and password
	// pre: none, handled by parent
	// post: returns a String[] with 0 being username and 1 being password
	private static String[] createCredentials(String[] credentials, Scanner sc, File logins)
			throws FileNotFoundException {

		System.out.println("What is your username (case sensitive)?");
		System.out.print("> ");
		credentials[0] = sc.nextLine();
		while (credentials[0].equals("")) {
			System.out.println("That is not a valid username. Please enter a new username.");
			System.out.print("> ");
			credentials[0] = sc.nextLine();
		}
		while (!canUseUsername(logins, credentials[0])) {
			System.out.println("That username is already taken. Please enter a new username.");
			System.out.print("> ");
			credentials[0] = sc.nextLine();
		}

		System.out.println("What is your password (case sensitive)?");
		System.out.print("> ");
		credentials[1] = putIntoHashCode(sc.nextLine());

		while (credentials[1].equals("")) {
			System.out.println("That is not a valid password. Please enter a new password.");
			System.out.print("> ");
			credentials[1] = sc.nextLine();
		}
		return credentials;

	}

	// private method to get the user's first name and to confirm their name.
	// pre: sc != null
	private static String getUserFirstName(Scanner sc) {
		if (sc == null) {
			throw new IllegalArgumentException("Scanner is somehow null");
		}
		System.out.println("Welcome! What is your first name?");
		System.out.print("> ");
		String tempFirstName = sc.nextLine();

		System.out.println("Are you sure that your name is " + tempFirstName + "? (y/n)");
		System.out.print("> ");
		String choice = sc.nextLine();
		while (choice.equals("n")) {
			System.out.println("Welcome! What is your first name?");
			System.out.print("> ");
			tempFirstName = sc.nextLine();

			System.out.println("Are you sure that your name is " + tempFirstName + "? (y/n)");
			System.out.print("> ");
			choice = sc.nextLine();

		}

		System.out.println("Welcome to the LMS, " + tempFirstName + ".");
		return tempFirstName;
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
	private static void logIn(Scanner sc, File logins) throws IOException {
		System.out.println("Welcome back! Please enter your username: ");
		System.out.print("> ");
		String attemptedUsername = sc.nextLine();

		System.out.println("What is your password?");
		System.out.print("> ");
		String attemptedPassword = putIntoHashCode(sc.nextLine());

		boolean found = foundLogIn(attemptedUsername, attemptedPassword, logins);

		while (!found) {
			System.out.println(
					"We are sorry. That account is not found. Please reenter your information");
			logIn(sc, logins);
		}

		System.out.println("Welcome back!");
		String[] userInfo = getUserInfoFromLogin(new File(attemptedUsername + ".txt"));
		System.out.println("Log in Success! Welcome back " + userInfo[0]);

		// user is an instructor
		if (userInfo[1].equals("instructor")) {
			instructor = new Instructor(userInfo[0], attemptedUsername);

		} else if (userInfo[1].equals("student")) {
			student = new Student(userInfo[0], attemptedUsername);
		} else {
			admin = new Admin(userInfo[0], attemptedUsername);
		}

	}

	// scans the first two lines of the user file to get name and occupation
	// pre: none
	// post: returns a String[] with 0 being first name and 1 being occupation
	private static String[] getUserInfoFromLogin(File userFile) throws FileNotFoundException {
		String[] userInfo = new String[2];
		Scanner userFileScanner = new Scanner(userFile);
		userInfo[0] = userFileScanner.nextLine();
		userInfo[1] = userFileScanner.nextLine();
		userFileScanner.close();
		return userInfo;

	}

	// private method to check all known login info to log in
	// pre: none
	// post: returns true if the account was found, false otherwise.
	private static boolean foundLogIn(String username, String password, File file)
			throws FileNotFoundException {
		Scanner fileScanner = new Scanner(file);

		while (fileScanner.hasNextLine()) {
			String currentLine = fileScanner.nextLine();
			Scanner currentLineScanner = new Scanner(currentLine);
			if (currentLineScanner.next().equals(username)
					&& currentLineScanner.next().equals(password)) {
				currentLineScanner.close();
				fileScanner.close();
				return true;
			}
			currentLineScanner.close();

		}
		fileScanner.close();
		return false;
	}

	// creates the file to be used for username/password management
	// pre: none
	// post: returns a File
	private static File createLogins() throws IOException {
		File allLogins = new File("allLogin.txt");
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

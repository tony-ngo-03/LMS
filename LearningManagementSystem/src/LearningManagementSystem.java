import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class LearningManagementSystem {
	private static Teacher teacher;
	private static Student student;

	private static String userName;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);

		Introduction(sc);
		allChoices(sc, teacher, student);
	}

	// cycle through all choices for the user to use
	// pre: either teacher OR student is null
	// post: returns nothing
	public static void allChoices(Scanner sc, Teacher teacher, Student student) throws IOException {
		if ((teacher == null && student == null) || (teacher != null && student != null)) {
			throw new IllegalArgumentException("User is neither a teacher nor a student");
		}
		System.out.println("\n\n\n");
		String choice = "";

		System.out.println("Welcome, please select the option by typing the number.");
		while (!choice.equals("log out")) {
			// teacher choices
			if (teacher != null && student == null) {
				System.out.println("1. Create Assignment");
				System.out.println("2. log out");
				choice = sc.nextLine();
				if (choice.equals("1")) {
					teacher.createAssignment(sc);
				}
				if (choice.equals("2")) {
					choice = "log out";
				}

			}
			// student choices
			else {
				System.out.println("1. Take Assignments!");
				System.out.println("2. log out");

				choice = sc.nextLine();
				if (choice.equals("1")) {
					student.takeAssignment();
				}
				if (choice.equals("2")) {
					choice = "log out";
				}
			}
		}
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
			// get first name
			System.out.print("What is your first name: ");
			String createFirstName = sc.nextLine();
			userName = createFirstName;
			System.out.println();

			// choose either teacher or student
			System.out.print("Are you a teacher or a student: ");
			String createOccupation = sc.nextLine().toLowerCase();
			System.out.println();
			// validate choice for either teacher or student
			while (!createOccupation.toLowerCase().equals("teacher")
					&& !createOccupation.toLowerCase().equals("student")) {
				System.out.println("That is not a valid choice. ");
				System.out.print("Are you a teacher or a student: ");
				createOccupation = sc.nextLine().toLowerCase();
			}
			// create username
			System.out.print("Please create a username: ");
			String createUsername = sc.nextLine();
			if (createOccupation.equals("teacher")) {
				createUsername += "_teacher";
			} else {
				createUsername += "_student";
			}
			boolean canContinue = false;
			while (!canContinue) {
				if (canUseUsername(logins, createUsername)) {
					canContinue = true;
					System.out.println();
					// create password
					System.out.print("please create a password: ");
					String createPassword = putIntoHashCode(sc.nextLine());
					System.out.println();

					// write firstName, userName, password, and occupation into the file.
					FileWriter fileWriter = new FileWriter(logins.getName(), true);
					fileWriter.write(createUsername + "\t" + createPassword + "\t" + createFirstName
							+ "\t" + createOccupation + "\n");
					System.out.println("Successfully created an account!");

					// after creating an account, create new instance of either teacher or student
					if (createOccupation.equals("teacher")) {
						teacher = new Teacher(createFirstName, createUsername);
					} else {
						student = new Student(createFirstName, createUsername);
					}
					fileWriter.close();
				} else {
					System.out.println("This username is already taken");
					System.out.print("Please choose a new one: ");
					createUsername = sc.nextLine();
				}
			}

		} else {
			// TODO: create loop for not valid entry
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
	private static void logIn(Scanner sc, File logins) throws IOException {
		System.out.print("Are you a teacher or a student: ");
		String studentOrTeacher = sc.nextLine();
		System.out.println();
		System.out.print("Enter Username: ");
		String loginUsername = sc.nextLine();
		System.out.println();
		System.out.print("Enter Password: ");
		String loginPassword = putIntoHashCode(sc.nextLine());
		String[] nameAndOcc = foundLogIn(loginUsername, loginPassword, studentOrTeacher, logins);
		if (nameAndOcc == null) {
			System.out.println("Log in Failed! Your username or password is wrong!");
			System.out.print("Would you like to try again?");
			String choice = sc.nextLine();
			if (choice.toLowerCase().charAt(0) == 'y') {
				logIn(sc, logins);
			} else {
				System.out.println("Okay! Thank you!");
			}
		}
		System.out.println("Log in Success!");
		// after logging in, create new instance of teacher or student
		if (nameAndOcc[1].equals("teacher")) {
			teacher = new Teacher(nameAndOcc[0], loginUsername);
		} else {
			student = new Student(nameAndOcc[0], loginUsername);
		}

	}

	// private helper method to log the user in and also figure out if they are a student or a
	// teacher
	// pre: file != null
	// post: returns 'teacher', 'student', or 'not found!'
	private static String[] foundLogIn(String username, String password, String studentOrTeacher,
			File file) throws FileNotFoundException {
		Scanner fileScanner = new Scanner(file);
		while (fileScanner.hasNext()) {
			String currentLine = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(currentLine);
			String currentUsername = lineScanner.next() + "_" + studentOrTeacher;

			int index = currentUsername.indexOf("_");
			if (currentUsername.substring(0, index).equals(username)
					&& lineScanner.next().equals(password)) {
				// 0 is name, 1 is occupation
				String[] nameAndOcc = new String[2];
				nameAndOcc[0] = lineScanner.next();
				userName = nameAndOcc[0];
				fileScanner.close();
				nameAndOcc[1] = lineScanner.next();
				lineScanner.close();
				return nameAndOcc;
			}
			lineScanner.close();
		}
		fileScanner.close();
		return null;
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

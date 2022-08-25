import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {
	private ArrayList<Instructor> allInstructors;

	private String adminFirstName;
	private String adminUsername;

	private Scanner sc;

	// public Constructor
	public Admin(String firstName, String username) throws FileNotFoundException {

		this.adminFirstName = firstName;
		this.adminUsername = username;
		sc = new Scanner(System.in);
		allInstructors = getAllInstructors();
	}

	// getter method for Admin's First Name
	public String getFirstName() {
		return adminFirstName;
	}

	// getter method for Admin's username
	public String getUsername() {
		return adminUsername;
	}

	// gets all known instructors in the system
	// pre: none
	// post: returns an ArrayList of Instructors
	private ArrayList<Instructor> getAllInstructors() throws FileNotFoundException {
		ArrayList<Instructor> allInstructors = new ArrayList<Instructor>();
		File logInFile = new File("allLogin.txt");

		Scanner fileScanner = new Scanner(logInFile);
		while (fileScanner.hasNextLine()) {
			String currentLine = fileScanner.nextLine();
			Scanner currentLineScanner = new Scanner(currentLine);
			String currentUser = currentLineScanner.next();
			currentLineScanner.close();
			File currentUserFile = new File(currentUser + ".txt");
			Scanner currentUserScanner = new Scanner(currentUserFile);
			String currentUserFirstName = currentUserScanner.nextLine();
			if (currentUserScanner.nextLine().equals("instructor")) {
				allInstructors.add(new Instructor(currentUserFirstName, currentUser));
			}
			currentUserScanner.close();
		}

		fileScanner.close();

		return allInstructors;

	}

	public void addCourseToPerson() throws IOException {
		System.out.println("Which Instructor would you like to add a course to?");
		for (int person = 0; person < allInstructors.size(); person++) {
			System.out.println((person + 1) + ". " + allInstructors.get(person).getUsername());
		}

		System.out.print("\n> ");
		String choice = sc.nextLine();

		while (Integer.parseInt(choice) <= 0 || Integer.parseInt(choice) > allInstructors.size()) {
			System.out.println("That is not a valid choice.");
			System.out.print("> ");
			choice = sc.nextLine();
		}

		String instructUsername = allInstructors.get(Integer.parseInt(choice) - 1).getUsername();
		System.out.println("Found the Instructor. What is the course name?");
		System.out.print("\n> ");
		String courseName = sc.nextLine();
		addCourse(instructUsername, courseName);

	}

	private void addCourse(String username, String courseName) throws IOException {
		File instructFile = new File(username + ".txt");

		FileWriter instructFileWriter = new FileWriter(instructFile, true);
		instructFileWriter.append("COURSE:" + courseName + "\n");
		instructFileWriter.close();
	}
}

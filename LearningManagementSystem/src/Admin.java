import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {
	private ArrayList<Instructor> allInstructors;
	private ArrayList<Student> allStudents;

	private String adminFirstName;
	private String adminUsername;

	private Scanner sc;

	// public Constructor
	public Admin(String firstName, String username) throws IOException {

		this.adminFirstName = firstName;
		this.adminUsername = username;
		sc = new Scanner(System.in);
		allStudents = getAllStudents();
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

	// gets all known students in the system
	// pre: none
	// post: returns an ArrayList of Students

	private ArrayList<Student> getAllStudents() throws IOException {
		ArrayList<Student> allStudents = new ArrayList<Student>();
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
			if (currentUserScanner.nextLine().equals("student")) {
				allStudents.add(new Student(currentUserFirstName, currentUser));
			}
			currentUserScanner.close();
		}

		fileScanner.close();

		return allStudents;
	}

	public void createCourse() throws IOException {
		System.out.println("What is the course called? ");
		System.out.print("\n> ");
		String courseName = sc.nextLine();

		System.out.println("What instructor will be teaching the course?\n");
		for (int i = 0; i < allInstructors.size(); i++) {
			System.out.println((i + 1) + ": " + allInstructors.get(i).getUsername());
		}
		System.out.print("\n> ");
		int chosenInstructor = sc.nextInt() - 1;

		String chosenInstructorUsername = allInstructors.get(chosenInstructor).getUsername();

		FileWriter instructFileWriter = new FileWriter(new File(chosenInstructorUsername + ".txt"),
				true);
		instructFileWriter.append("COURSE: " + courseName + "\n");
		instructFileWriter.close();

		Course newCourse = new Course(courseName, chosenInstructorUsername);

		System.out.println("What students will be enrolled in the course?\n");

		for (int i = 0; i < allStudents.size(); i++) {
			System.out.println((i + 1) + ": " + allStudents.get(i).getUsername());
		}
		System.out.print("\n> ");
		int chosenStudent = sc.nextInt() - 1;

		String chosenStudentUsername = allStudents.get(chosenStudent).getUsername();

		FileWriter studentFileWriter = new FileWriter(new File(chosenStudentUsername + ".txt"),
				true);
		studentFileWriter.append("COURSE: " + courseName + "\n");
		studentFileWriter.close();

		File courseFile = newCourse.getCourseFile();

		FileWriter fileWriter = new FileWriter(courseFile, true);

		fileWriter.append("INSTRUCTOR:" + chosenInstructorUsername + "\n");
		fileWriter.append("STUDENT:" + chosenStudentUsername + "\n");
		fileWriter.close();

	}

	// gets all known instructors in the system
	// pre: none
	// post: returns an ArrayList of Instructors
	private ArrayList<Instructor> getAllInstructors() throws IOException {
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

	// public method to add course to an instructor or student
	// pre: none
	// post: adds the course to the person's file
	public void addCourseToInstructor() throws IOException {
		System.out.println("Which Instructor would you like to add a course to?");
		for (int person = 0; person < allInstructors.size(); person++) {
			System.out.println((person + 1) + ". " + allInstructors.get(person).getUsername());
		}

		System.out.print("\n> ");
		String choice = sc.nextLine();

		while (Integer.parseInt(choice) <= 0 || Integer.parseInt(choice) > allInstructors.size()) {
			System.out.println("That is not a valid choice.");
			System.out.print("\n> ");
			choice = sc.nextLine();
		}

		String instructUsername = allInstructors.get(Integer.parseInt(choice) - 1).getUsername();
		System.out.println("Found the Instructor. What is the course name?");
		System.out.print("\n> ");
		String courseName = sc.nextLine();
		addCourse(instructUsername, courseName, "INSTRUCTOR");

	}

	public void addCourseToStudent() throws IOException {
		System.out.println("Which Student would you like to add a course to?");
		for (int person = 0; person < allStudents.size(); person++) {
			System.out.println((person + 1) + ". " + allStudents.get(person).getUsername());
		}

		System.out.print("\n> ");
		String choice = sc.nextLine();

		int temp = Integer.parseInt(choice);

		while (temp <= 0 || temp > allStudents.size()) {
			System.out.println("That is not a valid choice.");
			System.out.print("\n> ");
			temp = Integer.parseInt(sc.nextLine());
		}

		String studentUsername = allStudents.get(temp - 1).getUsername();
		System.out.println("Found the student. What is the course name?");
		String courseName = sc.nextLine();
		addCourse(studentUsername, courseName, "STUDENT");

	}

	// private helper method to actually access the requested usernames file and add the course
	// pre: none
	// post: edits the file to add the course inside.
	private void addCourse(String username, String courseName, String occupation)
			throws IOException {
		File instructFile = new File(username + ".txt");

		FileWriter instructFileWriter = new FileWriter(instructFile, true);
		instructFileWriter.append("COURSE:" + courseName + "\n");
		instructFileWriter.close();

		File courseFile = new File(courseName + ".txt");

		FileWriter courseFileWriter = new FileWriter(courseFile, true);
		courseFileWriter.append(occupation + ":" + username);
		courseFileWriter.close();
	}
}

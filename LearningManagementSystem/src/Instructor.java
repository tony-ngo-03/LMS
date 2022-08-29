import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Instructor {
	private ArrayList<Course> allCourses;
	private ArrayList<Message> allMessages;

	private String firstName;
	private String username;

	private File instructorFile;

	private Scanner sc;

	public Instructor(String firstName, String username) throws IOException {
		this.firstName = firstName;
		this.username = username;
		System.out.println("Welcome back, " + this.firstName + "!");
		instructorFile = getInstructorFile();
		this.allCourses = getCourses();
		sc = new Scanner(System.in);
	}

	// getter method to get username of the instructor
	// pre: none
	// post: a String
	public String getUsername() {
		return username;
	}

	// lets the instructor view all of their courses that they have been assigned
	// pre: none
	// post: returns a string with the course one line at a time.
	public String viewCourses() {
		System.out.println("Here are all of your avaliable courses!");
		StringBuilder sb = new StringBuilder();
		for (Course course : allCourses) {
			sb.append(course.getCourseName() + "\n");
		}

		return sb.toString();
	}

	// gets the instructor file from the directory
	// pre: none
	// post: returns a File if the file was found.
	private File getInstructorFile() {
		File instructorFile = new File(username + ".txt");
		return instructorFile;
	}

	// scans the instructor's files to get all of their courses
	// pre: instructorFile != null
	// post: returns an ArrayList<Course> of all Courses
	private ArrayList<Course> getCourses() throws IOException {
		if (instructorFile == null) {
			throw new IllegalStateException("Instructor File is not Found!");
		}
		ArrayList<Course> allCourses = new ArrayList<Course>();
		Scanner fileScanner = new Scanner(instructorFile);

		while (fileScanner.hasNextLine()) {
			String curr = fileScanner.nextLine();

			if (curr.length() > 7 && curr.substring(0, 7).equals("COURSE:")) {
				Course tempCourse = new Course(curr.substring(7), this.username);
				allCourses.add(tempCourse);
			}
		}

		fileScanner.close();
		return allCourses;
	}

	public void sendMessage() throws IOException {
		System.out
				.println("Please enter the username of the person you wish to send a message to: ");
		System.out.print("\n> ");
		String sendUsername = sc.nextLine();

		File usernameFile = new File(sendUsername + ".txt");

		while (!usernameFile.exists()) {
			System.out
					.println("Sorry. That user cannot be found. Please enter the username again.");
			System.out.print("\n> ");
			sendUsername = sc.nextLine();
			usernameFile = new File(sendUsername + ".txt");
		}

		System.out.println("What is the subject of the message? This cannot be left blank.");
		System.out.print("\n> ");
		String subject = sc.nextLine();

		Message newMessage = new Message(this.username, sendUsername, subject);

		System.out.println("What would you like to write? ");
		System.out.print("\n> ");
		String message = sc.nextLine();

		newMessage.sendMessage(this.firstName, message);
		writeMessageInFiles(newMessage.getMessageName(), sendUsername);

	}

	private void writeMessageInFiles(String messageName, String receiverName) throws IOException {
		FileWriter fileWriter = new FileWriter(instructorFile, true);
		fileWriter.append("MESSAGE: " + messageName + "\n");
		fileWriter.close();

		FileWriter otherWriter = new FileWriter(new File(receiverName + ".txt"), true);
		otherWriter.append("MESSAGE: " + messageName + "\n");
		otherWriter.close();
	}

	public void createAssignment() throws IOException {
		System.out.println("What course would you like to create an assignment for? ");

		for (int i = 0; i < allCourses.size(); i++) {
			System.out.println((i + 1) + ": " + allCourses.get(i).getCourseName());
		}

		System.out.print("\n> ");
		int courseChoice = sc.nextInt();

		Course chosenCourse = allCourses.get(courseChoice - 1);

		chosenCourse.createAssignment();
	}

}

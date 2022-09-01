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
		this.allMessages = getAllMessages();
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
				System.out.println("INSIDE INSTRUCTOR");
				Course tempCourse = new Course(curr.substring(8), this.username);
				System.out.println(tempCourse.getCourseName());
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
		fileWriter.append("\nMESSAGE: " + messageName + "\n");
		fileWriter.close();

		FileWriter otherWriter = new FileWriter(new File(receiverName + ".txt"), true);
		otherWriter.append("\nMESSAGE: " + messageName + "\n");
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

	private ArrayList<Message> getAllMessages() {
		ArrayList<Message> allMessages = new ArrayList<Message>();
		File dir = new File(System.getProperty("user.dir"));

		File[] allFiles = dir.listFiles();

		for (int i = 0; i < allFiles.length; i++) {
			File checkFile = allFiles[i];
			if (checkFile.getName().substring(0, 2).equals("m_")) {
				allMessages.add(new Message(checkFile));
			}
		}
		return allMessages;
	}

	public void viewAllMessages() throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < allMessages.size(); i++) {
			sb.append((i + 1) + ". " + allMessages.get(i).getInfo() + "\n");
		}
		sb.append((allMessages.size() + 1) + ". Exit");
		System.out.println(sb.toString());

		System.out.println("Please pick a number.");

		int choice = Integer.parseInt(sc.nextLine());

		if (choice != allMessages.size() + 1) {
			Message chosen = allMessages.get(choice - 1);
			System.out.println("Last message: " + chosen.getLastMessage());

			System.out.println("Would you like to write something? (y/n)");
			System.out.print("\n> ");
			String willRespond = sc.nextLine();

			if (willRespond.toLowerCase().equals("y")) {
				System.out.println("What would you like to write? ");
				System.out.print("\n> ");
				String message = sc.nextLine();

				chosen.sendMessage(this.firstName, message);
			}

		}

	}

}

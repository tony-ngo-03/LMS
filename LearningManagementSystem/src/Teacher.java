import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Teacher {
	private Assignment[] allAssignments;
	private ArrayList<String> allStudentsUsernames;

	private String teacherName;
	private String teacherUsername;

	private File teacherFile;

	private ArrayList<Message> allMessages;

	// constructor for the Teacher class
	// pre: name and username != null
	public Teacher(String name, String username) throws IOException {
		if (name == null || username == null) {
			throw new IllegalArgumentException("either name nor username can be null");
		}
		this.teacherName = name;
		this.teacherUsername = username;
		this.teacherFile = createTeacherFile();
		this.allStudentsUsernames = getAllStudents();
		this.allMessages = getAllMessages();
		System.out.println("Welcome!" + this.teacherName);
		System.out.println("These are all of the students: " + showAllStudents());
		
		
		
	}

	// shows all of the found files that contain _student
	// pre: allStudentsUsernames != null
	// post: returns a string of all of the students usernames
	public String showAllStudents() {
		if (allStudentsUsernames == null) {
			return "no students found!";
		}
		return allStudentsUsernames.toString();
	}

	// scans the repo and stores all student's files names
	// pre: none
	// post: returns an ArrayList<String> of all students
	private ArrayList<String> getAllStudents() {
		ArrayList<String> temp = new ArrayList<String>();
		File directory = new File(System.getProperty("user.dir"));

		for (File file : directory.listFiles()) {
			if (file.getName().length() >= 12) {
				if (file.getName().substring(file.getName().length() - 11).equals("student.txt")) {
					temp.add(file.getName());
				}
			}
		}
		return temp;
	}

	// public getter for this teacher's file
	public File getTeacherFile() {
		return teacherFile;
	}

	// creates a new assignment and assigns them to students
	// pre: none
	// post: returns an Assignment (new file), assigned to student, written to teacher file
	public Assignment createAssignment(Scanner sc) throws IOException {
		// create name of the assignment
		System.out.print("What is the name of the assignment: ");
		String assignmentName = sc.nextLine();
		System.out.println();
		// create assignment worth
		System.out.print("How much percent is this assignment worth: ");
		double gradePercent = Double.parseDouble(sc.nextLine());
		System.out.println();
		// create # of questions
		System.out.print("How many questions are on this assignment: ");
		int numQuestions = Integer.parseInt(sc.nextLine());
		System.out.println();
		Assignment newAssignment = new Assignment(assignmentName, gradePercent, numQuestions,
				teacherUsername);
		newAssignment.createQuestions(gradePercent, numQuestions);
		// write to teacher

		System.out.println(newAssignment.toString());
		System.out.println(teacherFile.getName());
		FileWriter fileWriter = new FileWriter(teacherFile.getName(), true);
		fileWriter.append(
				"Assignment: " + assignmentName + "\t" + gradePercent + "\t" + numQuestions + "\n");
		fileWriter.close();

		// assign to student
		System.out.println("Would you like to assign this to a specifc student or all students?");
		System.out.println();
		System.out.print("1. All Students\n");
		System.out.print("2. Specifc");
		System.out.println();
		System.out.print("CHOICE: ");
		String assignChoice = sc.nextLine();
		if (assignChoice.equals("1")) {
			for (String name : allStudentsUsernames) {
				assignToStudent(newAssignment, name);
			}
		} else {
			System.out.print("\nWhat is the student's username: ");
			String specificStudent = sc.nextLine() + "_student.txt";
			assignToStudent(newAssignment, specificStudent);
		}
		System.out.println("Assigned to student(s)!");
		return newAssignment;
	}

	// assigns an assignmnet to a student
	// pre: none
	// post: writes to respective files!
	private void assignToStudent(Assignment assignment, String studentName) throws IOException {
		File file = new File(studentName);
		if (file.exists()) {
			FileWriter fileWriter = new FileWriter(studentName, true);
			fileWriter.append(
					"Assignment: " + teacherUsername + "_" + assignment.getAssignmentName() + "\n");
			fileWriter.close();
		} else {
			System.out.println("Error. That Student does not have a file!");
		}

	}

	// private method to create the .txt for the file
	// pre: none
	// post: creates a new file if it does not exist and returns the file
	private File createTeacherFile() throws IOException {
		File teacherInfo = new File(teacherUsername + "_teacher.txt");
		if (teacherInfo.createNewFile()) {
			System.out.println("Sucessfully created a new file! Welcome to the LMS.");
		} else {
			System.out.println("File already created. Welcome back!");
		}
		return teacherInfo;
	}

	public Message sendMessage(Scanner sc) throws IOException {

		// get message info and then create a message object
		String subject = getMessageSubject(sc);
		System.out.println("SUBJECT: " + subject);
		String studentUsername = getMessageName(sc);
		System.out.println("Student Username: " + studentUsername);
		Message newMessage = new Message(this.teacherUsername, studentUsername, subject, true);

		// actually writing to the message
		System.out.println("What would you like to write?");
		String message = sc.nextLine();
		newMessage.write(message);

		// write this to the teacher file
		FileWriter teacherFileWriter = new FileWriter(teacherFile, true);
		teacherFileWriter.append("\nMESSAGE:" + newMessage.getMessageName());
		teacherFileWriter.close();

		// write this to the student file
		FileWriter studentFileWriter = new FileWriter(new File(studentUsername), true);
		studentFileWriter.append("\nMESSAGE:" + newMessage.getMessageName());
		studentFileWriter.close();

		return newMessage;
	}

	public void viewAllMessages() throws IOException {
		System.out.println("Here are all of your active messages!");

		for (int i = 0; i < this.allMessages.size(); i++) {
			System.out.println("" + (i + 1) + ": Student: " + allMessages.get(i).receiverGetter()
					+ "\tSubject: " + allMessages.get(i).subjectGetter());
		}
	}

	private ArrayList<Message> getAllMessages() throws IOException {
		ArrayList<Message> allMessages = new ArrayList<Message>();
		// get a reference to the user directory
		File directory = new File(System.getProperty("user.dir"));

		for (File file : directory.listFiles()) {
			String fileName = file.getName();
			if (fileName.charAt(0) == 'm') {
				Message tempMessage = new Message(file, true);
				if (tempMessage.senderGetter().equals(teacherUsername)) {
					allMessages.add(tempMessage);
				}
			}
		}
		return allMessages;

	}

	private String getMessageSubject(Scanner sc) {
		System.out.println("What is the subject of this message?");
		System.out.print("Subject: ");
		String subject = sc.nextLine();
		return subject;
	}

	private String getMessageName(Scanner sc) {
		System.out.println("Who would you like to send a message to?");
		for (int studentNum = 0; studentNum < allStudentsUsernames.size(); studentNum++) {
			System.out.println("" + (studentNum + 1) + allStudentsUsernames.get(studentNum));
		}
		System.out.println();
		System.out.print("CHOICE: ");
		String choice = sc.nextLine();
		boolean canContinue = false;
		int studentChoice = -1;
		while (!canContinue) {
			try {
				studentChoice = Integer.parseInt(choice) - 1;
				canContinue = true;
			} catch (Exception e) {
				System.out.println("Sorry. We cannot find that number.");
				System.out.print("CHOICE: ");
				choice = sc.nextLine();
			}
		}
		while (studentChoice < 0 || studentChoice > allStudentsUsernames.size() - 1) {
			System.out.println("That is not a valid choice!");
			System.out.print("CHOICE: ");
			studentChoice = Integer.parseInt(sc.nextLine());
		}
		String tempName = allStudentsUsernames.get(studentChoice);
		// 12 is _student.txt
		tempName = tempName.substring(0, tempName.length() - 12);
		System.out.println("TEMPNAME :" + tempName);
		return tempName;
	}
}

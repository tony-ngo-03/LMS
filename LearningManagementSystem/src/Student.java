import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Student {
	private String firstName;
	private String username;
	private Grade[] allGrades;

	private Scanner sc;
	private Assignment[] allAssignments;
	private int numAssignments;
	private File studentFile;

	private ArrayList<Message> allMessages;

	// public constructor for the student
	// pre: studentName != null, username != null
	public Student(String firstName, String username) throws IOException {
		if (firstName == null || username == null) {
			throw new IllegalArgumentException();
		}
		sc = new Scanner(System.in);
		this.firstName = firstName;
		this.username = username;
		this.studentFile = studentFile();
		// this.allAssignments = getAllAssignments(this.studentFile);
		System.out.println("Welcome! " + this.firstName);
	}

	// constructor to recreate the student
	public Student(File file) throws FileNotFoundException {
		sc = new Scanner(System.in);
		this.studentFile = file;
		Scanner fileScanner = new Scanner(this.studentFile);
		this.firstName = fileScanner.nextLine();
		this.username = this.studentFile.getName()
				.substring(this.studentFile.getName().length() - 4);
		

	}
	
	
	public String getUsername() {
		return this.username;
	}

	// creates a .txt file for the student
	// pre: none
	// post: if a .txt file does not exist, then it is created
	private File studentFile() throws IOException {
		return new File(username + ".txt");
	}

	// gets all assignments that are avaliable and that the student has not taken
	// pre: assignmentFile != null
	// post: returns an array of Assignments that the student can take
	private Assignment[] getAllAssignments(File assignmentFile) throws FileNotFoundException {
		if (assignmentFile == null) {
			throw new IllegalArgumentException("Student file is null");
		}
		Assignment[] temp = new Assignment[5];
		int counter = 0;
		Scanner fileScanner = new Scanner(assignmentFile);
		while (fileScanner.hasNextLine()) {
			String assignmentName = fileScanner.nextLine().substring(12);
			File foundAssignment = new File(assignmentName + ".txt");
			Assignment tempAssignment = new Assignment(foundAssignment);
			if (canUseAssignment(tempAssignment)) {
				temp[counter] = new Assignment(foundAssignment);
				this.numAssignments++;
				counter++;
				if (counter >= temp.length) {
					temp = resize(temp);
				}
			}
		}
		fileScanner.close();
		return temp;
	}

	// checks the assignment to make sure that the student has not used it before
	// pre: none
	// post: returns true if there is no grade for the student, false otherwise
	private boolean canUseAssignment(Assignment assignment) throws FileNotFoundException {
		Scanner sc = new Scanner(assignment.getAssignmentFile());
		sc.nextLine();
		int numQuestions = Integer.parseInt(sc.nextLine());
		for (int i = 0; i < numQuestions; i++) {
			for (int j = 0; j < 7; j++) {
				sc.nextLine();
			}
		}
		while (sc.hasNextLine()) {
			String taken = sc.nextLine();
			int colonIndex = taken.indexOf(':');
			if (taken.substring(0, colonIndex).contains(username)) {
				return false;
			}
		}
		return true;
	}

	// private method to resize if needed
	// pre: orig != null
	// post: returns a copy of the |orig| with more space
	private Assignment[] resize(Assignment[] orig) {
		Assignment[] temp = new Assignment[orig.length * 2];
		for (int i = 0; i < orig.length; i++) {
			temp[i] = orig[i];
		}
		return temp;
	}

	// lets the student choose and take an assignment
	// pre: none
	// post: assignment gets updated with grade!
	public void takeAssignment() throws IOException {
		if (this.numAssignments == 0) {
			System.out.println("There are no more assignments for you to take!!");
		} else {
			System.out.println("Here is your list of assignments that you can take!");
			boolean allAssignmentsFound = false;
			int index = 0;
			while (!allAssignmentsFound) {
				if (this.allAssignments[index] != null) {
					System.out.println("" + (index + 1) + ". "
							+ this.allAssignments[index].getAssignmentName());
					index++;
				} else {
					allAssignmentsFound = true;
				}
			}
			System.out.print("Please Select an Assignment: ");
			String studentChoice = sc.nextLine();
			int chosen = Integer.parseInt(studentChoice) - 1;
			while (chosen < 0 || chosen >= index + 1) {
				System.out.println("That is not a valid assignment!");
				System.out.print("Please pick another: ");
				chosen = Integer.parseInt(sc.nextLine());
			}
			Assignment chosenAssignment = allAssignments[chosen];
			System.out.println("You have chosen to take " + chosenAssignment.getAssignmentName());
			System.out.println("\n");
			chosenAssignment.takeAssignment();
			System.out.println(chosenAssignment.viewGrade());
			chosenAssignment.appendToAssignment(username + ":" + chosenAssignment.viewGrade());
			this.allAssignments = removeFromList(chosen);
		}
	}

	// public getter method for the student file for use of editing
	// pre: studentFile != null
	// post: returns the studentFile
	public File getStudentFile() {
		if (this.studentFile == null) {
			throw new IllegalArgumentException("Student file does not exist!");
		}
		return this.studentFile;
	}

	// private method to copy all of the assignments for use of editng
	// pre: none
	// post: returns a copy of the |allAssignments|
	private Assignment[] copyAssignments() {
		Assignment[] copy = new Assignment[this.allAssignments.length];
		for (int i = 0; i < this.allAssignments.length; i++) {
			copy[i] = this.allAssignments[i];
		}
		return copy;
	}

	// private method to remove a taken assignment from the list
	// pre: none
	// post: returns an array with one element removed;
	private Assignment[] removeFromList(int index) {
		Assignment[] temp = copyAssignments();
		for (int i = index; i < temp.length - 1; i++) {
			temp[i] = temp[i + 1];
		}
		this.numAssignments--;
		return temp;
	}

}

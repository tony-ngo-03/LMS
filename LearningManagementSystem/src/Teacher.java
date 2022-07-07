import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Teacher {
	private Assignment[] allAssignments;
	private ArrayList<String> allStudentsUserNames;

	private String teacherName;
	private String teacherUsername;

	private File teacherFile;

	public Teacher(String name, String username) throws IOException {
		this.teacherName = name;
		this.teacherUsername = username;
		this.teacherFile = createTeacherFile();
		this.allStudentsUserNames = getAllStudents();
		System.out.println(showAllStudents());
	}

	public String showAllStudents() {
		return allStudentsUserNames.toString();
	}

	private ArrayList<String> getAllStudents() {
		ArrayList<String> temp = new ArrayList<String>();
		File directory = new File(System.getProperty("user.dir"));

		for (File file : directory.listFiles()) {
			if (file.getName().length() >= 6) {
				if (file.getName().substring(file.getName().length() - 5).equals("s.txt")) {
					temp.add(file.getName());
				}
			}
		}
		return temp;
	}

	public File getTeacherFile() {
		return teacherFile;
	}

	// could possibly move this to teacher?
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
		newAssignment.createQuestions();
		System.out.println(newAssignment.toString());
		System.out.println(teacherFile.getName());
		FileWriter fileWriter = new FileWriter(teacherFile.getName(), true);
		fileWriter.append("Assignment: " + assignmentName);
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
			for (String name : allStudentsUserNames) {
				assignToStudent(newAssignment, name);
			}
		} else {
			System.out.print("\nWhat is the student's username: ");
			String specificStudent = sc.nextLine();
			assignToStudent(newAssignment, specificStudent);
		}
		System.out.println("Assigned to student(s)!");
		return newAssignment;
	}

	private void assignToStudent(Assignment assignment, String studentName) throws IOException {
		FileWriter fileWriter = new FileWriter(studentName, true);
		fileWriter.append("Assignment: " + assignment.getAssignmentName());
		fileWriter.close();
	}

	private File createTeacherFile() throws IOException {
		File teacherInfo = new File(teacherUsername + ".txt");
		if (teacherInfo.createNewFile()) {
			System.out.println("Sucessfully created a new file! Welcome to the LMS.");
		} else {
			System.out.println("File already created. Welcome back!");
		}
		return teacherInfo;
	}

}

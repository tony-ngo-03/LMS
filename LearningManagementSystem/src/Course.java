import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Course {
	private Scanner sc;
	private ArrayList<Student> students;
	private ArrayList<Assignment> allAssignments;

	private Grade courseGrade;

	private String courseName;
	private String courseInstructor;

	private File courseFile;

	public Course(String courseName, String courseInstructor) throws IOException {
		this.courseName = courseName;
		this.courseInstructor = courseInstructor;
		this.students = new ArrayList<Student>();
		this.allAssignments = new ArrayList<Assignment>();
		this.courseFile = createCourseFile();

		sc = new Scanner(System.in);
	}

	private File createCourseFile() throws IOException {
		System.out.println("INSIDE COURSE.JAVA");
		File courseFile = new File(this.courseName + ".txt");
		if (courseFile.exists()) {
			System.out.println("Course file already exists!");
		} else {
			courseFile.createNewFile();
		}
		return courseFile;
	}

	public File getCourseFile() {
		if (this.courseFile == null) {
			throw new IllegalStateException("Course file does not exist!");
		}
		return this.courseFile;
	}

	public String getCourseName() {
		return this.courseName;
	}

	// creates an assignment
	public Assignment createAssignment() throws IOException {
		System.out.println("What do you want to call the assignment? ");
		System.out.print("\n> ");
		String assignmentName = sc.nextLine();

		System.out.println("What grade percentage should this be?");
		System.out.print("\n> ");
		double assignmentPercentage = sc.nextDouble();

		System.out.println("How many questions are on this assignment? ");
		System.out.print("\n> ");
		int numAssignmentQuestions = sc.nextInt();

		Assignment newAssignment = new Assignment(assignmentName, assignmentPercentage,
				numAssignmentQuestions, this.courseName);

		newAssignment.createQuestions(assignmentPercentage, numAssignmentQuestions);

		FileWriter fileWriter = new FileWriter(this.courseFile, true);
		fileWriter.append("\nASSIGNMENT:" + assignmentName + "\n");
		fileWriter.close();
		this.allAssignments.add(newAssignment);
		assignToStudents(newAssignment);
		return newAssignment;

	}

	private void assignToStudents(Assignment assignment) throws IOException {
		for (Student student : students) {
			File studentFile = student.getStudentFile();
			FileWriter studentFileWriter = new FileWriter(studentFile, true);

			studentFileWriter.append("\n" + assignment.getAssignmentName() + "\n");
			studentFileWriter.close();
		}
	}

	private ArrayList<Student> getAllStudents() throws IOException {
		ArrayList<Student> allStudents = new ArrayList<Student>();

		Scanner fileScanner = new Scanner(this.courseFile);

		while (fileScanner.hasNextLine()) {
			String currentLine = fileScanner.nextLine();
			if (currentLine.contains("STUDENT:")) {
				String studentUsername = currentLine.substring(currentLine.indexOf(":"));
				File studentFile = new File(studentUsername + ".txt");
				Scanner studentFileScanner = new Scanner(studentFile);
				String firstName = studentFileScanner.nextLine();
				studentFileScanner.close();
				Student newStudent = new Student(firstName, studentUsername);
				allStudents.add(newStudent);
			}
		}
		fileScanner.close();
		return allStudents;
	}

	private String getCourseInstructor() throws FileNotFoundException {
		Scanner fileScanner = new Scanner(this.courseFile);
		while (fileScanner.hasNextLine()) {
			String curLine = fileScanner.nextLine();
			if (curLine.contains("INSTRUCTOR:")) {
				return curLine.substring(curLine.indexOf(":"));
			}
		}

		return null;

	}
}

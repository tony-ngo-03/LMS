import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Student {
	private String studentName;
	private String studentUsername;
	private Grade[] allGrades;

	private Assignment[] allAssignments;

	private File studentFile;

	public Student(String studentName, String username) throws IOException {
		this.studentName = studentName;
		this.studentUsername = username;
		this.studentFile = createStudentFile();
		this.allAssignments = getAllAssignments(this.studentFile);
	}

	public File getStudentFile() {
		return studentFile;
	}

	private File createStudentFile() throws IOException {
		File studentFile = new File(studentUsername + "_student.txt");
		if (studentFile.createNewFile()) {
			System.out.println("Student file sucessfully created! Welcome to the LMS");
		} else {
			System.out.println("Student file found! Welcome Back.");
		}
		return studentFile;
	}

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
			temp[counter] = new Assignment(foundAssignment);
			counter++;
			if (counter >= temp.length) {
				temp = resize(temp);
			}
		}
		fileScanner.close();
		return temp;
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

	public void takeAssignment() {
		System.out.println("Here is your list of assignments that you can take!");
		for (int assignmentNum = 0; assignmentNum < this.allAssignments.length
				- 1; assignmentNum++) {
			System.out.println("" + (assignmentNum + 1) + ". "
					+ this.allAssignments[assignmentNum].getAssignmentName());
		}
	}
}

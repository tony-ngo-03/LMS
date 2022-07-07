import java.io.File;
import java.io.IOException;

public class Student {
	private String studentName;
	private String studentUsername;
	private Grade[] allGrades;

	private File studentFile;

	public Student(String studentName, String username) throws IOException {
		this.studentName = studentName;
		this.studentUsername = username;
		this.studentFile = createStudentFile();
	}

	public File getStudentFile() {
		return studentFile;
	}

	private File createStudentFile() throws IOException {
		File studentFile = new File(studentUsername + ".txt");
		if (studentFile.createNewFile()) {
			System.out.println("Student file sucessfully created! Welcome to the LMS");
		} else {
			System.out.println("Student file found! Welcome Back.");
		}
		return studentFile;
	}
}

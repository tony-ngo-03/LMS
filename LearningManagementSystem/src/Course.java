import java.util.ArrayList;
import java.util.Scanner;

public class Course {
	private ArrayList<Student> students;
	private ArrayList<Homework> allHomework;

	private String courseName;
	private String courseInstructor;

	public Course(String courseName, String courseInstructor) {
		this.courseName = courseName;
		this.courseInstructor = courseInstructor;
		students = new ArrayList<Student>();
		allHomework = new ArrayList<Homework>();
	}
}

import java.util.ArrayList;

public class Instructor {
	private ArrayList<Course> allCourses;
	private ArrayList<Message> allMessages;

	private String firstName;
	private String userName;

	public Instructor(String firstName, String userName) {
		this.firstName = firstName;
		this.userName = userName;
	}

	public String getUsername() {
		return userName;
	}
}

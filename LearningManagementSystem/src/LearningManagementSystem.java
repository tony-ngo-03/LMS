import java.util.Scanner;

public class LearningManagementSystem {
	private Teacher teacher;
	private Student student;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		Introduction(sc);
		Assignment assignment = new Assignment("Homework 1", 10.0, 2);
		assignment.createQuestions();
		System.out.println(assignment.toString());
	}
	
	public static void options(Scanner sc) {
		System.out.println("So, what would you like to do now?");
		System.out.println("OPTIONS:");
		System.out.println("1. Create Assignment");
	}
	
	public static void Introduction(Scanner sc) {
		System.out.println("Welcome to the LMS.");
		System.out.print("Please Select: ");
		
		String choice = sc.nextLine();
		
		if(choice.toLowerCase().equals("teacher")) {
			
			
		}
		else if(choice.toLowerCase().equals("student")) {
			
		}
		else {
			
		}
		
	}
	
	
	
	
	
	
	
}

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Assignment {

	private Grade grade;
	private String assignmentName;
	private double gradePercent;

	// format: Question: {A, B, C, D, Correct}
	private HashMap<String, String[]> questionsAndAnswers;
	private String[] answers;
	private String[] allCorrectAnswers;

	private int numQuestions;

	// constructor for the assignment
	// basic information
	// pre: numQuestions 
	public Assignment(String name, double gradePercentage,int numQuestions) {
		this.assignmentName = name;
		this.gradePercent = gradePercentage;
		this.numQuestions = numQuestions;
		answers = new String[numQuestions];
		allCorrectAnswers = new String[numQuestions];
		this.grade = new Grade(-1);
		this.questionsAndAnswers = new HashMap<String, String[]>();
	}
	
	
	// getter method for the grade of this assignment;
	// pre: Grade must be not -1
	// post: returns a grade;
	public String viewGrade() {
		if(this.grade.equals(new Grade(-1))){
			return this.assignmentName + " has not been graded yet!";
		}
		return this.grade.toString();
	}
	
	
	// setter method to change the grade of this assignment
	// pre: newGrade != this.grradel
	// post: returns true if the grade was changed;
	public boolean setGrade(Grade newGrade) {
		if(this.grade.equals(newGrade)) {
			return false;
		}
		this.grade = newGrade;
		return true;
	}
	// fills the HashMap of Q and A for the assignment
	// pre: none
	// post: updates 
	public void createQuestions() {
		
		System.out.println(createQuestionIntro());
		Scanner sc = new Scanner(System.in);
		System.out.println("SIZE: " + numQuestions);
		for (int qNum = 0; qNum < numQuestions; qNum++) {
			System.out.print("Enter Question " + (qNum + 1) + ": ");
			String[] answers = new String[5];
			String question = sc.nextLine();
			System.out.println();
			for (int answerNum = 0; answerNum < 4; answerNum++) {
				System.out.print("Enter Letter " + (char) (65 + answerNum) + ": ");
				answers[answerNum] = sc.nextLine();
				System.out.println();
			}

			System.out.print("What is the correct answer: ");
			answers[4] = sc.nextLine();

			questionsAndAnswers.put(question, answers);
		}
		sc.close();
	}

	// helper method to print out the introduction
	// pre: none
	// post: returns a string
	private String createQuestionIntro() {
		StringBuilder sb = new StringBuilder();	
		sb.append(
				"Welcome, Instructor: This assignment has " + numQuestions + " questions.");
		sb.append("For the sake of integrity, the questions will be randomized.");
		sb.append("The format goes like this:");
		sb.append("QUESTION: *type question here*");
		sb.append("ANSWER:");
		sb.append("A. *typed*");
		sb.append("B. *typed*");
		sb.append("C. *typed*");
		sb.append("D. *typed*");
		sb.append("\n\n");	
		return sb.toString();
	}

	// the format of the assignment.
	// Question:
	// A.
	// B.
	// C.
	// D.
	// pre: none
	// post: returns a string
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<String> allQuestions = questionsAndAnswers.keySet();
		Iterator<String> tempIt = allQuestions.iterator();
		for (int i = 0; i < allQuestions.size(); i++) {
			String question = tempIt.next();
			sb.append("Question " + i + ": " + question);
			sb.append("\nAnswers:\n");
			String[] temp = questionsAndAnswers.get(question);
			sb.append(showAnswers(temp));
		}
		return sb.toString();

	}

	// private helper method for toString() that shows the answers to the question
	// pre: answerBox != null
	// post: returns a string with the answers
	private String showAnswers(String[] answerBox) {
		final int A_CHAR = 65;
		final int D_CHAR = 69;
		if (answerBox == null) {
			throw new IllegalArgumentException("Answers cannot be null! Attempted to call showAnswers");
		}
		String temp = "";
		for (int i = A_CHAR; i < D_CHAR; i++) {
			temp += "" + (char) (i) + ": " + answerBox[i - A_CHAR];
			temp += "\n";
		}
		return temp;
	}

}

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Assignment {

	private Grade grade;
	private String assignmentName;
	private double gradePercent;

	// format: Question: {A, B, C, D, Correct}
	private TreeMap<String, String[]> questionsAndAnswers;
	private String[] studentAnswers;
	
	private Scanner sc;

	private int numQuestions;

	private final static int A_CHAR = 65;
	private final static int D_CHAR = 69;

	// constructor for the assignment
	// basic information
	// pre: numQuestions
	public Assignment(String name, double gradePercentage, int numQuestions) {
		sc = new Scanner(System.in);
		this.assignmentName = name;
		this.gradePercent = gradePercentage;
		this.numQuestions = numQuestions;
		studentAnswers = new String[numQuestions];
		this.grade = new Grade(-1);
		this.questionsAndAnswers = new TreeMap<String, String[]>();
	}
	
	
	// getter method for the 
	public double viewGradePercent() {
		return this.gradePercent;
	}

	// getter method for the grade of this assignment;
	// pre: Grade must be not -1
	// post: returns a grade;
	public String viewGrade() {
		if (this.grade.equals(new Grade(-1))) {
			return this.assignmentName + " has not been graded yet!";
		}
		return this.grade.toString();
	}

	// setter method to change the grade of this assignment
	// pre: newGrade != this.grradel
	// post: returns true if the grade was changed;
	public boolean setGrade(Grade newGrade) {
		if (this.grade.equals(newGrade)) {
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
			answers[answers.length - 1] = sc.nextLine();

			questionsAndAnswers.put(question, answers);
		}
	}

	// helper method to print out the introduction
	// pre: none
	// post: returns a string
	private String createQuestionIntro() {
		StringBuilder sb = new StringBuilder();
		sb.append("Welcome, Instructor: This assignment has " + numQuestions + " questions.\n");
		sb.append("For the sake of integrity, the questions will be randomized.\n");
		sb.append("The format goes like this:\n");
		sb.append("QUESTION: *type question here*\n");
		sb.append("ANSWER:\n");
		sb.append("A. *typed*\n");
		sb.append("B. *typed*\n");
		sb.append("C. *typed*\n");
		sb.append("D. *typed*\n");
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
			sb.append("Question " + (i + 1) + ": " + question);
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
		if (answerBox == null) {
			throw new IllegalArgumentException(
					"Answers cannot be null! Attempted to call showAnswers");
		}
		String temp = "";
		for (int i = A_CHAR; i < D_CHAR; i++) {
			temp += "" + (char) (i) + ": " + answerBox[i - A_CHAR];
			temp += "\n";
		}
		return temp;
	}

	// prints out the correct answers versus the students answer side by side
	// pre: none
	// post: none
	public void correctVsStudent() {
		Set<String> questionSet = questionsAndAnswers.keySet();
		Iterator<String> questionIterator = questionSet.iterator();
		for (int question = 0; question < questionSet.size(); question++) {
			String currentQuestion = questionIterator.next();
			String currentCorrectAnswer = questionsAndAnswers
					.get(currentQuestion)[questionsAndAnswers.get(currentQuestion).length - 1];
			String currentStudentAnswer = studentAnswers[question];
			System.out.print("Question " + (question + 1) + ": " + currentCorrectAnswer + "\t"
					+ currentStudentAnswer);
			if(!currentCorrectAnswer.equals(currentStudentAnswer)) {
				System.out.print(" DIFFERENT!");
			}
			System.out.println();
		}
	}

	// lets the user take the assignment.
	// pre: none
	// post: updates the studentAnswers array and calculates the grade that the
	// student received.
	public void takeAssignment() {
		Set<String> questionSet = questionsAndAnswers.keySet();
		Iterator<String> questionIterator = questionSet.iterator();
		for (int question = 0; question < questionSet.size(); question++) {
			String currentQuestion = questionIterator.next();
			System.out.println("Question " + (question + 1) + ": " + currentQuestion);
			System.out.println("\nANSWERS: \n");
			String[] tempAnswers = questionsAndAnswers.get(currentQuestion);
			for (int answer = A_CHAR; answer < D_CHAR; answer++) {
				System.out.println("" + (char) (answer) + ": " + tempAnswers[answer - A_CHAR]);
			}
			System.out.print("\nCHOSEN ANSWER: ");
			String tempAnswer = sc.nextLine();
			studentAnswers[question] = "" + tempAnswer.toUpperCase().charAt(0);
		}
		System.out.println("CONGRATULATIONS FOR FINISHING: " + assignmentName);	
		this.grade = calculateGrade();
	}
	
	
	private Grade calculateGrade() {
		double numMissed = 0;
		Set<String> questionSet = questionsAndAnswers.keySet();
		Iterator<String> questionIterator = questionSet.iterator();
		for (int question = 0; question < questionSet.size(); question++) {
			String currentQuestion = questionIterator.next();
			String currentCorrectAnswer = questionsAndAnswers
					.get(currentQuestion)[questionsAndAnswers.get(currentQuestion).length - 1];
			String currentStudentAnswer = studentAnswers[question];
			if(!currentCorrectAnswer.equals(currentStudentAnswer)) {
				numMissed++;
			}
		}		
		return new Grade(numMissed / numQuestions);
	}

}

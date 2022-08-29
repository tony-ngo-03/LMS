import java.util.TreeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Assignment {

	private Grade grade;
	private String assignmentName;
	private double gradePercent;
	private String courseName;

	private File assignmentFile;
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
	public Assignment(String name, double gradePercentage, int numQuestions, String courseName)
			throws IOException {
		sc = new Scanner(System.in);
		this.assignmentName = name;
		this.courseName = courseName;
		this.gradePercent = gradePercentage;
		this.numQuestions = numQuestions;
		studentAnswers = new String[numQuestions];

		this.grade = new Grade(-1);
		this.questionsAndAnswers = new TreeMap<String, String[]>();
		assignmentFile = createFileAssignment();
	}

	// public getter method
	// pre: assignemntFile != null
	// post: returns a File of the assignment
	public File getAssignmentFile() {
		if (this.assignmentFile == null) {
			throw new IllegalArgumentException();
		}
		return this.assignmentFile;
	}

	// default constructor for an assignment
	public Assignment() {
		sc = new Scanner(System.in);
	}

	// constructor of an assignment from a file
	// pre: assignemntFile must exist and the assignmentFile != null
	// post: creates a new Assignment
	public Assignment(File assignmentFile) throws FileNotFoundException {
		this.assignmentFile = assignmentFile;
		if (assignmentFile == null || !assignmentFile.exists()) {
			throw new IllegalArgumentException("File is null or does not exist!");
		}
		this.questionsAndAnswers = new TreeMap<String, String[]>();
		this.grade = new Grade(-1);
		sc = new Scanner(System.in);
		// set up initial info
		String tempName = assignmentFile.getName().substring(0,
				assignmentFile.getName().length() - 3);
		int underscoreIndex = tempName.indexOf("_");
		this.courseName = tempName.substring(0, underscoreIndex);
		this.assignmentName = tempName.substring(underscoreIndex);

		Scanner fileScanner = new Scanner(assignmentFile);
		String gradePercentage = fileScanner.nextLine();
		String numQuestionString = fileScanner.nextLine();
		this.gradePercent = Double.parseDouble(gradePercentage);

		this.numQuestions = Integer.parseInt(numQuestionString);
		this.studentAnswers = new String[this.numQuestions];
		for (int questionNum = 0; questionNum < numQuestions; questionNum++) {
			// get the question
			String question = fileScanner.nextLine();
			int colonIndex = question.indexOf(":");
			question = question.substring(colonIndex + 1);
			// skip the "ANSWERS"
			fileScanner.nextLine();
			// get the 4 answers
			String[] answerBox = new String[5];
			for (int answerNum = 0; answerNum < 4; answerNum++) {
				String answer = fileScanner.nextLine().substring(3);
				answerBox[answerNum] = answer;
			}

			// get the correct answer
			String correctAnswer = answerDecoder(fileScanner.nextLine().substring(9));
			answerBox[answerBox.length - 1] = correctAnswer;

			// insert into the questionsAndAnswers
			this.questionsAndAnswers.put(question, answerBox);

		}
		fileScanner.close();
	}

	// private helper method to help decode the correct answer to rebuild the assignment
	// pre: toDecode != null
	// post: returns a string that is either "A" - "D"
	private String answerDecoder(String toDecode) {
		if (toDecode == null) {
			throw new IllegalArgumentException("cannot decode null");
		}
		toDecode = toDecode.substring(0, 2);
		int decoded = Integer.parseInt(toDecode, 16);
		return "" + (char) decoded;
	}

	// getter method for the assignment name
	// pre: none
	// post: returns a string of the assignment name
	public String getAssignmentName() {
		return this.assignmentName;
	}

	// creates the .txt file of the assignment
	// pre: none
	// post: returns the file that the assignment is linked to
	private File createFileAssignment() throws IOException {
		File assignmentFile = new File(courseName + "_" + assignmentName + ".txt");
		if (assignmentFile.createNewFile()) {
			System.out.println("Sucessfully created Assignment!");
		} else {
			System.out.println("File already exists");
		}

		return assignmentFile;
	}

	// getter method for how much this assignment is worth
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

	// fills the HashMap of Q and A for the assignment and writes it to the file
	// pre: none
	// post: updates
	public void createQuestions(double gradePercent, int numQuestions) throws IOException {
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
			questionsAndAnswers.put("Question " + (qNum + 1) + ":" + question, answers);
		}
		FileWriter fileWriter = new FileWriter(assignmentFile.getName(), true);
		fileWriter.write(gradePercent + "\n" + numQuestions + "\n");
		fileWriter.write(toString());
		fileWriter.close();
		System.out.println("The assignment File has been created!");

	}

	// helper method to print out the introduction
	// pre: none
	// post: returns a string
	private String createQuestionIntro() {
		StringBuilder sb = new StringBuilder();
		sb.append("Welcome, Instructor: This assignment has " + numQuestions + " question(s).\n");
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
			sb.append(getAllAnswers(temp));
		}
		return sb.toString();
	}

	// private helper method for toString() that shows the answers to the question
	// pre: answerBox != null
	// post: returns a string with the answers
	private String getAllAnswers(String[] answerBox) {
		if (answerBox == null) {
			throw new IllegalArgumentException(
					"Answers cannot be null! Attempted to call showAnswers");
		}
		String temp = "";
		for (int i = A_CHAR; i < D_CHAR; i++) {
			temp += "" + (char) (i) + ": " + answerBox[i - A_CHAR];
			temp += "\n";
		}
		temp += "CORRECT: " + encryptAnswer(answerBox[answerBox.length - 1]);
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
			if (!currentCorrectAnswer.equals(currentStudentAnswer)) {
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

	// calculates the grade by comparing the correct answer to the wrong answer
	// pre: none
	// post: returns a grade
	private Grade calculateGrade() {
		double numMissed = 0;
		Set<String> questionSet = questionsAndAnswers.keySet();
		Iterator<String> questionIterator = questionSet.iterator();
		for (int question = 0; question < questionSet.size(); question++) {
			String currentQuestion = questionIterator.next();
			String currentCorrectAnswer = questionsAndAnswers
					.get(currentQuestion)[questionsAndAnswers.get(currentQuestion).length - 1];
			System.out.println("CURRENT CORRECT ANSWER: " + currentCorrectAnswer);
			String currentStudentAnswer = studentAnswers[question];
			System.out.println("CURRENT STUDENT ANSWER: " + currentStudentAnswer);
			System.out.println(currentCorrectAnswer.equals(currentStudentAnswer));
			if (!currentCorrectAnswer.equals(currentStudentAnswer)) {
				numMissed++;
			}
		}

		return new Grade(100 * (numQuestions - numMissed) / numQuestions);
	}

	// encrypts the answer by converting the ascii value to hexidecimal and adding the assignment
	// name in ascii to hexidecial
	// pre: toEncrypt != null
	// post: returns a string of the encrypted answer
	private String encryptAnswer(String toEncrypt) {
		if (toEncrypt == null) {
			throw new IllegalArgumentException("Cannot encrypt null!");
		}
		String newAnswer = "";
		newAnswer += Integer.toHexString((char) toEncrypt.charAt(0));
		for (int i = 0; i < assignmentName.length(); i++) {
			newAnswer += Integer.toHexString((char) assignmentName.charAt(i));
		}
		return newAnswer;
	}

	public void appendToAssignment(String toAppend) throws IOException {
		FileWriter fileWriter = new FileWriter(this.assignmentFile, true);
		fileWriter.append("\n" + toAppend);
		fileWriter.close();
	}
}

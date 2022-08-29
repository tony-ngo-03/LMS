import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Message {

	private String instructor;
	private String student;
	private String subject;

	private File messageFile;

	private Scanner sc;

	//
	public Message(String instructor, String student, String subject) throws IOException {
		this.instructor = instructor;
		this.student = student;
		this.subject = subject;
		this.messageFile = getMessageFile();
		sc = new Scanner(System.in);
	}

	// getter method for the entire message name
	// pre: none
	// post:
	public String getMessageName() {
		return this.messageFile.getName();
	}

	// creates a message file if it already does not exist
	// pre: none
	// post: creates or obtains the message file to be used
	private File getMessageFile() throws IOException {
		String test = "m_" + this.instructor + "_" + this.student + "-" + subject + ".txt";
		System.out.println(test);
		File messageFile = new File(test);
		if (messageFile.createNewFile()) {
			System.out.println("Message File created!");
		} else {
			System.out.println("Message File found!");
		}

		return messageFile;
	}

	// method to actually write to the message file!
	// pre: this.messageFile != null
	// post: writes to the message file
	public void sendMessage(String firstName, String message) throws IOException {
		if (this.messageFile == null) {
			throw new IllegalStateException("The message file is not found!");
		}

		FileWriter fileWriter = new FileWriter(this.messageFile, true);
		fileWriter.append("\n" + firstName + ": " + message + "\n");
		fileWriter.close();
	}

	// scans the entire file to get the last message sent
	// pre: messageFile != null
	public String getLastMessage() throws FileNotFoundException {
		if (this.messageFile == null) {
			throw new IllegalStateException("The message file is not found!");
		}
		Scanner fileScanner = new Scanner(this.messageFile);
		String lastMessage = "";
		while (fileScanner.hasNextLine()) {
			lastMessage = fileScanner.nextLine();
		}
		fileScanner.close();
		return lastMessage;
	}

}

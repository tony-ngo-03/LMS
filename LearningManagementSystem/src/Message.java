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

	public Message(String instructor, String student, String subject) throws IOException {
		this.instructor = instructor;
		this.student = student;
		this.subject = subject;
		this.messageFile = getMessageFile();
	}

	public Message(File file) {
		this.messageFile = file;
		String messageName = this.messageFile.getName();
		messageName = messageName.substring(2);
		this.instructor = messageName.substring(0, messageName.indexOf("_"));

		this.student = messageName.substring(messageName.indexOf("_") + 1,
				messageName.indexOf("-"));

		this.subject = messageName.substring(messageName.indexOf("-") + 1,
				messageName.indexOf("."));

		System.out.println(getInfo());

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
		fileWriter.append(firstName + ": " + message + "\n");
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
			String temp = fileScanner.nextLine();
			if (!temp.equals("")) {
				lastMessage = temp;
			}

		}
		fileScanner.close();
		return lastMessage;
	}

	// gets the vital information of any message
	// pre: none
	// post: returns the instructor, student, and subject of the message
	public String getInfo() {
		return "Instructor: " + this.instructor + "\tStudent: " + this.student + "\tSubject: "
				+ this.subject;
	}

}

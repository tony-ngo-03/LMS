import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Message {
	private File messageFile;

	private String sender;
	private String receiver;

	private String subject;
	private String messageName;

	// constructor for message class
	// teacherOrStudent is true if teacher, false if student
	// for naming conventions
	public Message(String sender, String receiver, String subject, boolean teacherOrStudent)
			throws IOException {
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.messageName = "message_";
		if (teacherOrStudent) {
			this.messageName += this.sender + "_" + this.receiver;
		} else {
			this.messageName += this.receiver + "_" + this.sender;
		}
		this.messageName += "_" + this.subject;

		this.messageFile = getMessageFile();
	}

	// construct the message from a file!
	// pre: file != null && file.exists()
	public Message(File file, boolean teacherOrStudent) {
		if (file == null || !file.exists()) {
			throw new IllegalArgumentException("file is null or the file does not exist");
		}
		this.messageFile = file;
		this.messageName = this.messageFile.getName();
		this.sender = getSender(teacherOrStudent);
		this.receiver = getReceiver(teacherOrStudent);
		this.subject = getSubject();

	}

	// public getter method for this message file
	// pre: messageFile != null
	// post: returns the file requested
	public File getMessage() {
		if (this.messageFile == null) {
			throw new IllegalStateException("File is not yet created for this message!");
		}
		return this.messageFile;
	}

	public String getMessageName() {
		return this.messageFile.getName();
	}

	// private helper method used for getting the file for initial use
	// pre: none
	// post: returns a file of the message
	private File getMessageFile() throws IOException {
		File file = new File(this.messageName + ".txt");

		if (!file.createNewFile()) {
			System.out.println("DEBUGGING: Message Found!");
		} else {
			System.out.println("DEBUGGING: ");
		}
		return file;
	}

	// appends to the file as a new message!
	// pre: message != null
	// post: the file is changed!
	public void write(String message) throws IOException {
		if (message == null) {
			throw new IllegalArgumentException("Message cannot be null");
		}
		FileWriter fileWriter = new FileWriter(this.messageFile, true);
		fileWriter.append("\n" + sender + ": " + message);
		fileWriter.close();
	}

	// public method to get the last message sent in the file
	// pre: none
	// post: returns a string of the last message in that conversation
	public String getLastMessage() throws FileNotFoundException {
		Scanner fileScanner = new Scanner(messageFile);
		String last = "";
		while (fileScanner.hasNextLine()) {
			last = fileScanner.nextLine();
		}
		fileScanner.close();
		return last;
	}

	// gets the reciever of the message for easier viewing!
	// pre: teacherOrStudent is true for teacher, false for student
	// post: returns a string of the receiver of the message
	private String getReceiver(boolean teacherOrStudent) {
		int counter = teacherOrStudent ? 2 : 1;
		String name = "";
		for (int i = 0; i < this.getMessageName().length(); i++) {
			if (this.getMessageName().charAt(i) == '_') {
				counter--;
			}
			if (counter == 0) {
				name += "" + this.getMessageName().charAt(i);
			}
		}
		return name.substring(1);
	}

	private String getSender(boolean teacherOrStudent) {
		int counter = teacherOrStudent ? 1 : 2;
		String name = "";
		for (int i = 0; i < this.getMessageName().length(); i++) {
			if (this.getMessageName().charAt(i) == '_') {
				counter--;
			}
			if (counter == 0) {
				name += "" + this.getMessageName().charAt(i);
			}
		}
		return name.substring(1);
	}

	private String getSubject() {
		int counter = 3;
		String subject = "";
		for (int i = 0; i < this.getMessageName().length(); i++) {
			if (this.getMessageName().charAt(i) == '_') {
				counter--;
			}
			if (counter == 0) {
				subject += "" + this.getMessageName().charAt(i);
			}
		}

		return subject.substring(1, subject.length() - 4);

	}

	public String senderGetter() {
		return this.sender;
	}

	public String receiverGetter() {
		return this.receiver;
	}

	public String subjectGetter() {
		return this.subject;
	}

	// the entire message in 1 string
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			Scanner fileScanner = new Scanner(messageFile);
			while (fileScanner.hasNextLine()) {
				sb.append(fileScanner.nextLine() + "\n");
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

}

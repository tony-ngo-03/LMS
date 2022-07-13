
public class Grade {
	private double numGrade;

	// constructor
	// pre: num >= -1 (-1 means no grade yet)
	// post: creates a new object Grade
	public Grade(double num) {
		if (num < -1) {
			throw new IllegalArgumentException("The grade must be greater than -1");
		}
		this.numGrade = num;
	}

	// determines if two grades are equal to eachother
	// pre: otherGrade != null
	// post: returns true if equal, false otherwise
	public boolean equals(Object other) {
		if (other == null) {
			throw new IllegalArgumentException("cannot compare to null!");
		}
		if (other instanceof Grade) {
			return this.numGrade == ((Grade) other).numGrade;
		}
		return false;

	}

	// overrides Object's toString
	// pre: none
	// post: returns the grade in a String
	public String toString() {
		return "" + numGrade;
	}

	// gives the user the letter grade equivelant (no +-)
	// pre: none
	// post: returns either A, B, C, D, or F
	public String getLetterGrade() {
		if (numGrade >= 90) {
			return "A";
		} else if (numGrade >= 80) {
			return "B";
		} else if (numGrade >= 70) {
			return "C";
		} else if (numGrade >= 60) {
			return "D";
		} else {
			return "F";
		}
	}
}

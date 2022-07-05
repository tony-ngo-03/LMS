
public class Grade {
	private double numGrade;
	
	
	public Grade(double num) {
		this.numGrade = num;
	}
	
	
	public boolean equals(Grade otherGrade) {
		return this.numGrade == otherGrade.numGrade;
	}
	
	public String toString() {
		return "" + numGrade;
	}
	
	
	// 
	public String getLetterGrade() {
		if(numGrade >= 90) {
			return "A";
		}
		else if(numGrade >= 80) {
			return "B";
		}
		else if(numGrade >= 70) {
			return "C";
		}
		else if(numGrade >= 60) {
			return "D";
		}
		else {
			return "F";
		}
	}
}

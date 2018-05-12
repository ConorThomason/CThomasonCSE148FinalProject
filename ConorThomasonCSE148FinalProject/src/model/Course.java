package model;
/*
 * The creation of a Course, Textbook, or Person requires the immediate addition to a bag. This is to check the uniqueness. 
 */
public class Course implements java.io.Serializable {
	private String courseTitle;
	private String courseNumber;
	private Textbook textbookAssigned;
	private String textbookIsbn;
	private int numberOfCredits;
	
	public Course(Course course) {
		try {
		this.setCourseTitle(course.getCourseTitle());
		this.setCourseNumber(course.getCourseNumber());
		this.setTextbookAssigned(course.getTextbookAssigned());
		this.setNumberOfCredits(course.getNumberOfCredits());
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public Course(String courseTitle, String courseNumber, Textbook textbookAssigned, int numberOfCredits) {
		super();
		try {
		this.setCourseTitle(courseTitle);
		this.setCourseNumber(courseNumber);
		this.setTextbookAssigned(textbookAssigned);
		this.setNumberOfCredits(numberOfCredits);
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		try {
		this.courseTitle = courseTitle;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Course Title entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		try {
		this.courseNumber = courseNumber;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Course Number entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public Textbook getTextbookAssigned() {
		return textbookAssigned;
	}
	public void setTextbookAssigned(Textbook textbookAssigned) {
		try {
		this.textbookAssigned = textbookAssigned;
		if (textbookAssigned != null)
			this.setTextbookIsbn(textbookAssigned.getIsbn());
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Textbook Asigned entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public int getNumberOfCredits() {
		return numberOfCredits;
	}
	public void setNumberOfCredits(int numberOfCredits) {
		try {
		this.numberOfCredits = numberOfCredits;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Number of Credits entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	@Override
	public String toString() {
		String returnedString = "Course Title: " + courseTitle + "\t Course Number: " + courseNumber + "\nTextbook Assigned: \n";
		if (textbookAssigned == null) {
			returnedString += "No Textbook Assigned \nNumber Of Credits: " + numberOfCredits + "\n";
		}
		else if (textbookAssigned.getIsbn().equals("")) {
			returnedString += "No Textbook Assigned \nNumber Of Credits: " + numberOfCredits + "\n";
		}
		else {
			returnedString += textbookAssigned + "\nNumber Of Credits: " + numberOfCredits + "\n";
		}
		return returnedString;
	}
	public String getTextbookIsbn() {
		return textbookIsbn;
	}
	public void setTextbookIsbn(String textbookIsbn) {
		this.textbookIsbn = textbookIsbn;
	}
	
}

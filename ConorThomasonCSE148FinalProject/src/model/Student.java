package model;

public class Student extends Person implements java.io.Serializable {
	private double gpa;
	private String major;
	private CourseBag courseBag;

	public Student(String firstName, String lastName, String phoneNumber,String major) {
		super(firstName, lastName, phoneNumber);
		try {
			this.setMajor(major.toUpperCase());
			CourseBag majorBag = new CourseBag("DEFAULT", this.getMajor());
			this.setCourseBag(majorBag);
			this.setGpa(calculateGpa(this.courseBag));
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public Student(String firstName, String lastName, String id, String phoneNumber, double gpa, String major, CourseBag courseBag) {
		super(firstName, lastName, id, phoneNumber);
		try {
			this.setGpa(gpa);
			this.setMajor(major.toUpperCase());
			this.setCourseBag(courseBag);
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}

	public Student(Student student) {
		super(student.getFirstName(),student.getLastName(), student.getPhoneNumber());
		try { 
			this.setGpa(student.getGpa());
			this.setMajor(student.getMajor());
			this.setCourseBag(student.getCourseBagArray());
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public boolean checkDataValidity() {
		if ((this.getFirstName() != null) && (this.getLastName() != null) && (this.getPhoneNumber() != null) &&
			(this.getMajor() != null)) {
			return true;
		}
		else {
			return false;
		}
	}
	public double getGpa() {
		return gpa;
	}
	public void setGpa(double gpa) {
		if (gpa > 4.0) {
			try {
				throw new GpaTooLargeException(); 
			} catch (GpaTooLargeException e) {
				System.out.println(e.getMessage());
				this.gpa = 4.0;
			}
		}
		if (gpa < 0.0) {
			try {
				throw new GpaTooSmallException();
			} catch (GpaTooSmallException e) {
				System.out.println(e.getMessage());
				this.gpa = 0.0;
			}
		}
		try {
			this.gpa = gpa;
		} catch(IllegalArgumentException e) {
			System.out.println("The GPA entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public double calculateGpa(CourseBag coursebag) {
		double totalPoints = 0;
		int totalCredits = 0;
		MasterCourseBag masterCourseBag = new MasterCourseBag(50);
		masterCourseBag.importData("DEFAULT");
		for (int i = 0; i < courseBag.getCourseCount(); i++) {
			String[] courseInfo = courseBag.getCourseInfo(i);
			if (courseInfo[1].equals("IP") || courseInfo[1].equals("N/A")) {
				break;
			}
			else {
				Course courseForCredit =  masterCourseBag.getCourse((masterCourseBag.find(courseInfo[0])));
				totalPoints += this.gradeToGpa(courseInfo[1]) * courseForCredit.getNumberOfCredits();
				totalCredits += courseForCredit.getNumberOfCredits();
			}
		}
		return totalPoints/totalCredits;
	}
	public double gradeToGpa(String grade) {
		switch (grade.toUpperCase()) {
		case "A": return 4.0;
		case "B+": return 3.5;
		case "B": return 3.0;
		case "C+": return 2.5;
		case "C": return 2.0;
		case "D+": return 1.5;
		case "D": return 1.0;
		case "F": return 0.0;
		default: return 0.0;
		}
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		try {
			this.major = major;
			CourseBag majorBag = new CourseBag("DEFAULT", this.getMajor());
			this.setCourseBag(majorBag);
		} catch(IllegalArgumentException e) {
			System.out.println("The Major entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public CourseBag getCourseBagArray() {
		return this.courseBag;
	}
	public String getCourseBagExportable() {
		String returnedString = Integer.toString(courseBag.getCourseCount()) + "&&& ";
		String[] course;
		for (int i = 0; i < courseBag.getCourseCount(); i++)
		{
			course = courseBag.getCourseInfo(i);
			returnedString += course[0] + "&&& " + course[1] + "&&& " + course[2] + "&&& ";
		}
		return returnedString;
	}
	public String getCourseBag() {
		String returnedString = Integer.toString(courseBag.getCourseCount()) + " ";
		String[] course;
		for (int i = 0; i < courseBag.getCourseCount(); i++)
		{
			course = courseBag.getCourseInfo(i);
			returnedString += course[0] + " " + course[1] + " " + course[2] + " ";
		}
		return returnedString;
	}
	public void setCourseBag(CourseBag givenCourseBag) {
		try {
			courseBag = new CourseBag(givenCourseBag.getCourseCount());
			for (int i = 0; i < givenCourseBag.getCourseCount(); i++){
				courseBag.add(givenCourseBag.getCourseInfo(i));
			}
		} catch(IllegalArgumentException e) {
			System.out.println("The CourseBag entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String toString() {
		String returnedString = super.toString() + "\t GPA: " + this.getGpa() + "\t Major: " + this.getMajor() + "\t \nCourses:\n";
		for (int i = 0; i < courseBag.getCourseCount(); i++) {
			String[] parsedInfo = courseBag.getCourseInfo(i);
			returnedString += parsedInfo[0] + " " + parsedInfo[1] + " " + parsedInfo[2] + " \n";
		}
		return returnedString;
	}
}

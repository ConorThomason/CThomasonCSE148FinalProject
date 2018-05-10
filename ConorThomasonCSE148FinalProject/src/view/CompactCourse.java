package view;

public class CompactCourse {
	private String courseNumber;
	private String courseGrade;
	private String courseType;
	
	public CompactCourse() {
		
	}
	public CompactCourse(String courseNumber, String courseGrade, String courseType) {
		this.courseNumber = courseNumber;
		this.courseGrade = courseGrade;
		this.courseType = courseType;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getCourseGrade() {
		return courseGrade;
	}
	public void setCourseGrade(String courseGrade) {
		this.courseGrade = courseGrade;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String[] getCourseBagStyle() {
		String[] returnedString = new String[3];
		returnedString[0] = courseNumber;
		returnedString[1] = courseGrade;
		returnedString[2] = courseType;
		return returnedString;
	}
}

package model;

public class Faculty extends Person implements java.io.Serializable {
	private String title;
	private String salary;
	public Faculty(String firstName, String lastName, String phoneNumber, String title, String salary) {
		super(firstName, lastName, phoneNumber);
		try {
		this.setTitle(title);
		this.setSalary(salary);
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public Faculty(String firstName, String lastName, String id, String phoneNumber, String title, String salary) {
		super(firstName, lastName, id, phoneNumber);
		try {
		this.setTitle(title);
		this.setSalary(salary);
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public Faculty(Faculty faculty) {
		super(faculty.getFirstName(), faculty.getLastName(), faculty.getPhoneNumber());
		try {
		this.setTitle(faculty.getTitle());
		this.setSalary(faculty.getSalary());
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		try {
		this.title = title;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Title entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		try {
		this.salary = salary;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Salary entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
		String salaryToConvert = "";
		if (salary.charAt(0) != '$') //Added a check for $ so that it would be uniform throughout
		{
			salaryToConvert += '$';
		}
		salaryToConvert += salary;
		this.salary = salaryToConvert;
	}
	public String toString() {
		return super.toString() + "\t Title: " + this.getTitle() + "\t Salary: " + this.getSalary();
	}
	public boolean checkDataValidity() {
		if ((this.getFirstName() != null) && (this.getLastName() != null) && (this.getPhoneNumber() != null) && (this.getTitle() != null)
				&& (this.getSalary() != null)) {
			return true;
		}
		else {
			return false;
		}
	}
}

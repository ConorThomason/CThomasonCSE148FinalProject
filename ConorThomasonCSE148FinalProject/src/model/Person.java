package model;

public abstract class Person implements java.io.Serializable {
	private String firstName;
	private String lastName;
	private static int generatedId = 1;
	private String id;
	private String phoneNumber;
	
	public Person (Person person) {
		try {
		this.setFirstName(person.getFirstName());
		this.setLastName(person.getLastName());
		this.id = Integer.toString(generatedId); //Even if the person is a carbon copy of somebody else, they still must have a new ID. IDs cannot be changed/reused.
		generatedId++; //Piazza notes said that all IDs must be unique, no matter what - hence this implementation.
		this.phoneNumber = person.phoneNumber;
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
		
	}
	public Person(String firstName, String lastName, String phoneNumber) {
		super();
		try {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.id = Integer.toString(generatedId); //Integer class handles primitives just fine, so this method doesn't cause any issues.
		generatedId++; //Increment id
		this.setPhoneNumber(phoneNumber);
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public Person(String firstName, String lastName, String id, String phoneNumber) {
		super();
		try {
			this.setFirstName(firstName);
			this.setLastName(lastName);
			this.setId(id); //This is a special case for the sake of the importing/exporting in peopleBag - the only time the setId method comes into use is if ID = null.
			this.setPhoneNumber(phoneNumber);
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		try {
		this.firstName = firstName;
		} catch(IllegalArgumentException e) {
			System.out.println("The First Name entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		try {
		this.lastName = lastName;
		} catch(IllegalArgumentException e) {
			System.out.println("The Last Name entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if (this.id == null)
			this.id = id;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		try {
		this.phoneNumber = phoneNumber;
		} catch(IllegalArgumentException e) {
			System.out.println("The Phone Number entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String toString() {
		return "First Name: " + firstName + "\t Last Name: " + lastName + "\t ID: " + id + "\t Phone Number: "
				+ phoneNumber;
	}
	public boolean checkDataValidity;
	public static int getCurrentId() {
		return generatedId;
	}
	public static void setCurrentId(int newIdMax) {
		generatedId = newIdMax;
	}
}

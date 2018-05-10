package model;

/*
 * add DONE
 * display DONE
 * find DONE
 * delete DONE
 * load DONE
 * save DONE
 * exportData DONE
 * importData
 */

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PeopleBag implements java.io.Serializable {
	private Person people[];
	private int itemCount = 0;

	public PeopleBag(int maxSize) { //In the case that only 1 size parameter is given.
		people = new Person[maxSize];
	}
	public int getPeopleCount() {
		return itemCount;
	}
	public void add(Person person) {
		boolean validityState = false;
		if (person instanceof Student)
		{
			person = (Student)person;
			validityState = ((Student) person).checkDataValidity();
		}
		else if (person instanceof Faculty) {
			person = (Faculty)person;
			validityState = ((Faculty) person).checkDataValidity();
		}
		if (validityState) {
			people[itemCount++] = person;
		}
	}
	public boolean isDuplicate(String id) {
		if (this.find(id) == -1)
			return false;
		else
			return true;
	}
	public int find(String id) {
		for (int i = 0; i < itemCount; i++)
		{
			if (people[i].getId().equals(id)) 
				return i;
		}
		return -1;
	}
	public void display() {
		System.out.println("\nStudents:");
		for(int i = 0; i < itemCount; i++)
		{
			if (people[i] instanceof Student) {
				System.out.println(people[i]);
			}
		}
		System.out.println("\nFaculty:");
		for(int i = 0; i < itemCount; i++)
		{
			if (people[i] instanceof Faculty) {
				System.out.println(people[i]);
			}
		}
		System.out.println();
	}
	public Person getPerson(int index) {
		return people[index];
	}
	public Person delete(String id) {
		int index = this.find(id);

		if (people[index].getId().equals(id)) {
			Person deletedPerson = people[index];
			for (int i = 0; i < itemCount-1; i++)
			{
				people[i] = people[i+1];
			}
			itemCount--;
			return deletedPerson;
		}
		else {
			System.out.println("This person does not exist.");
			return null;
		}
	}
	public void save() {
		ObjectOutputStream objectOutput = null;
		try {
			FileOutputStream fileOutput = new FileOutputStream("people.dat");
			objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(people);
			objectOutput.writeObject(itemCount);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				objectOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void load() { //Unlike the import/export methods, the load method will overwrite any data that is already in the arrays
		ObjectInputStream objectInput = null;
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream("people.dat");
			objectInput = new ObjectInputStream(fileInput);
			people = (Person[])objectInput.readObject();
			itemCount = (Integer)objectInput.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				objectInput.close();
				fileInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void exportData() {
		File file = null;
		Writer output = null;
		try {
			file = new File("peopleExport.txt");
			output = new FileWriter(file);
			for (int i = 0; i < itemCount; i++) {
				if (people[i] instanceof Student) {
					output.write("Student: &&& ");
					output.write(people[i].getFirstName() + "&&& ");
					output.write(people[i].getLastName() + "&&& ");
					output.write(people[i].getId() + "&&& ");
					output.write(people[i].getPhoneNumber() + "&&& ");
					output.write(((Student) people[i]).getGpa() + "&&& ");
					output.write(((Student) people[i]).getMajor() + "&&& \n");
					output.write(((Student) people[i]).getCourseBagExportable() + "&&& \n");
				}
				else {
					output.write("Faculty: &&& ");
					output.write(people[i].getFirstName() + "&&& ");
					output.write(people[i].getLastName() + "&&& ");
					output.write(people[i].getId() + "&&& ");
					output.write(people[i].getPhoneNumber() + "&&& ");
					output.write(((Faculty) people[i]).getTitle() + "&&& ");
					output.write(((Faculty) people[i]).getSalary() + "&&& \n");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				output.flush();
				output.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void importData(String fileName) { //If you attempt to add a course with the same course number as one already in the array, it will output a message complaining, and reject the input.
		if (fileName.equals("DEFAULT"))
			fileName = "peopleExport.txt";
		File file = null;
		Scanner input = null;
		try {
			file = new File(fileName);
			input = new Scanner(file).useDelimiter("&&& ");
			while (input.hasNext()) {
				if (input.next().equals("Student: ")) {
					CourseBag inputCourseBag = new CourseBag(1);
					Student inputStudent = new Student("","",null,"",0.0,"", inputCourseBag);
					inputStudent.setFirstName(input.next());
					inputStudent.setLastName(input.next());
					inputStudent.setId(input.next());
					inputStudent.setPhoneNumber(input.next());
					inputStudent.setGpa(input.nextDouble());
					inputStudent.setMajor(input.next());
					input.nextLine();
					String[][] coursesToReturn = new String[input.nextInt()][3]; //3 is for each of the columns following the layout created for CourseBag
					for (int i = 0; i < coursesToReturn.length; i++)
					{
						coursesToReturn[i][0] = input.next();
						coursesToReturn[i][1] = input.next();
						coursesToReturn[i][2] = input.next() + " ";
					}
					CourseBag courses = new CourseBag(coursesToReturn);
					inputStudent.setCourseBag(courses);
					this.add(inputStudent);
					input.nextLine();
				}
				else {
					Faculty inputFaculty = new Faculty("","",null,"","","");
					inputFaculty.setFirstName(input.next());
					inputFaculty.setLastName(input.next());
					inputFaculty.setId(input.next());
					inputFaculty.setPhoneNumber(input.next());
					inputFaculty.setTitle(input.next());
					inputFaculty.setSalary(input.next());
					this.add(inputFaculty);
					input.nextLine();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		finally {
			input.close();
		}
	}
}

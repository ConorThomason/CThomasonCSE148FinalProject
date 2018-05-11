package model;

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

import view.Util;

/*
 * add DONE
 * display DONE
 * find DONE
 * delete DONE
 * load DONE
 * save DONE
 * exportData DONE
 * importData DONE
 */
public class CourseBag implements java.io.Serializable {
	private String[][] courses;
	private final int CNUMBER = 0;
	private final int CGRADE = 1;
	private final int CTYPE = 2;
	private final int DEFAULTMAX = 100;
	private int itemCount;

	/*					 Course   Grade   Type, Type = "NEED", "HAVE", "TAKING", "NOT REQUIRED"
	 * 		1			|		|		|		|
	 * 		2			|		|		|		|
	 * 		3			|		|		|		|
	 * 		...			|		|		|		|
	 * 		n			|		|		|		|
	 * 
	 * 
	 */

	public CourseBag(String[][] courses) {
		super();
		this.courses = courses;
		itemCount = courses.length;
	}
	public CourseBag(int maxSize) {
		this.courses = new String[maxSize][3];
	}
	public CourseBag(String fileName,String major) {
		super();
		this.setCoursesToGraduate(fileName, major);
	}

	public void setCoursesToGraduate(String file, String major) {
		major = major.toUpperCase();
		MajorCourseBag majorBag = new MajorCourseBag(file, major);
		String[] arrayToFill = majorBag.getCourseNumbers();
		itemCount = arrayToFill.length;
		courses = new String[itemCount][3];
		for (int i = 0; i < itemCount; i++) {
			courses[i][0] = arrayToFill[i];
			this.setGrade(courses[i][CNUMBER], null);
			this.setCourseType(courses[i][CNUMBER], "NEED");
		}
	}
	public int find(String courseNumber) {
		
		for (int i = 0; i < itemCount; i++){
			if (courses[i][CNUMBER].equals(courseNumber)) {
				return i;
			}
		}
		return -1;
	}
	public void display() {
		System.out.println("Courses: ");
		for (int i = 0; i < itemCount; i++) {
			if (this.courses[i][CNUMBER] == null) {
				break;
			}
			else {
				System.out.println("Course Number: " + courses[i][CNUMBER] + "\t Course Grade: " + courses[i][CGRADE] + "\t Course Type: " + courses[i][CTYPE]);
			}
		}
	}
	public boolean isDuplicate(String courseNumber) {
		if (this.find(courseNumber) == -1)
			return false;
		else
			return true;
	}
	public void setCourseType(String courseNumber, String type) {
		courses[this.find(courseNumber)][CTYPE] = type;
	}
	public void setGrade(String courseNumber, String grade) {
		
		if (grade != null) {
			grade.toUpperCase();
			if (grade.equals("A") || grade.equals("B") || grade.equals("C") || grade.equals("D") || grade.equals("F") || grade.equals("W") || grade.equals("IP") || grade.equals("N/A")) {
				courses[this.find(courseNumber)][CGRADE] = grade.toUpperCase();
			}
			else {
				Util.displayError("Invalid input for Student Grade");
				courses[this.find(courseNumber)][CGRADE] = "N/A";
			}
		}
		else
			courses[this.find(courseNumber)][CGRADE] = "N/A";
	}
	public String getGrade(String courseNumber) {
		return courses[this.find(courseNumber)][CGRADE];
	}
	public String getCourseType(String courseNumber) {
		return courses[this.find(courseNumber)][CGRADE];
	}
	public String[] getCourseInfo(int index) {
		return courses[index];
	}
	public int getCourseCount() {
		return itemCount;
	}
	public void addCourseNumber(String courseNumber) {
		try {
		this.courses[itemCount++][CNUMBER] = courseNumber;
		} catch (ArrayIndexOutOfBoundsException e) {
			itemCount--;
			increaseArraySize();
			addCourseNumber(courseNumber);
		}
	}
	public String delete(String courseNumber) {
 		int index = this.find(courseNumber);
		if (index == -1) {
			System.out.println("This Course does not exist.");
			return null;
		}
		else {
			String deletedCourse = courses[index][CNUMBER];
			for (int i = index; i < itemCount-1; i++) {
				if (i == itemCount-1)
				{
					courses[i] = null;
				}
				else {
				courses[i][CNUMBER] = courses[i+1][CNUMBER];
				courses[i][CGRADE] = courses[i+1][CGRADE];
				courses[i][CTYPE] = courses[i+1][CTYPE];
				}
			}
			itemCount--;
			return deletedCourse;
		}
	}
	public void add(String[] course) {
		if (isDuplicate(course[CNUMBER])) {
			System.out.println("Course Number: " + course + " appears to be a duplicate. Please reenter it correctly or exclude its further entry.");
		}
		else {
			if (course[CGRADE] == null) {
				course[CGRADE] = "N/A";
			}
			if (course[CTYPE] == null) {
				course[CTYPE] = "NOT REQUIRED";
			}
			if (course[CTYPE].equals("TAKING")) {
				course[CGRADE] = "IP";
			}
			this.addCourseNumber(course[CNUMBER]);
			this.setGrade(course[CNUMBER], course[CGRADE]);
			this.setCourseType(course[CNUMBER], course[CTYPE]);
		}
	}
	public void add(Course course) {
		String[] provided = {course.getCourseNumber(), null, null};
		this.add(provided);
	}
	public void addByCourseNumber(String courseNumber) {
		String[] courseToAdd = {courseNumber, null, null};
		this.add(courseToAdd);
	}
	public void increaseArraySize() {
		String[][] newCourses = new String[courses.length + 1][3];
		for (int i = 0; i < courses.length; i++) {
			newCourses[i][0] = courses[i][0];
			newCourses[i][1] = courses[i][2];
			newCourses[i][2] = courses[i][2];
		}
		courses = newCourses.clone();
	}
	public void save() {
		ObjectOutputStream objectOutput = null;
		try {
			FileOutputStream fileOutput = new FileOutputStream("courseBag.dat");
			objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(courses);
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
	public void load() { //Unlike the import/export methods, the load method will overwrite any data that is already in the array
		ObjectInputStream objectInput = null;
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream("courseBag.dat");
			objectInput = new ObjectInputStream(fileInput);
			courses = (String[][])objectInput.readObject();
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
	public void importData(String fileName) { //If you attempt to add a course with the same course number as one already in the array, it will output a message complaining, and reject the input.
		if (fileName.equals("DEFAULT"))
			fileName = "courseBagExport.txt";
		File file = null;
		Scanner input = null;
		try {
			file = new File(fileName);
			input = new Scanner(file).useDelimiter("&&& ");
			while(input.hasNextLine()) {
				String[] inputCourse = input.nextLine().split("&&& ");
				this.add(inputCourse);	
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
	}
	public void exportData() {
		File file = null;
		Writer output = null;
		try {
			file = new File("courseBagExport.txt");
			output = new FileWriter(file);
			for (int i = 0; i < itemCount; i++) {
				output.write(courses[i][CNUMBER] + "&&& ");
				output.write(courses[i][CGRADE] + "&&& ");
				output.write(courses[i][CTYPE] + "&&& ");
				output.write("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
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
}

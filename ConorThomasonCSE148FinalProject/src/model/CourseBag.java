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
			courses[this.find(courseNumber)][CGRADE] = grade.toUpperCase();
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
		this.courses[itemCount++][CNUMBER] = courseNumber;
	}
	public String delete(String courseNumber) {
		int index = this.find(courseNumber);
		if (index == -1) {
			System.out.println("This Course does not exist.");
			return null;
		}
		else {
			String deletedCourse = courses[index][CNUMBER];
			for (int i = 0; i < itemCount-1; i++) {
				int newIndex = i+1;
				courses[i][CNUMBER] = courses[newIndex][CNUMBER];
				courses[i][CGRADE] = courses[newIndex][CGRADE];
				courses[i][CTYPE] = courses[newIndex][CTYPE];
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
			courses[itemCount++][CNUMBER] = course[CNUMBER];
			this.setGrade(course[CNUMBER], course[CGRADE]);
			this.setCourseType(course[CNUMBER], course[CTYPE]);
		}
	}
	public void add(Course course) {
		String[] provided = {course.getCourseNumber(), null, null};
		this.add(provided);
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

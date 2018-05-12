package model;

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
import java.util.Scanner;

public class MajorCourseBag {
	private Course majorCourses[];
	private String majorName;
	private String courseNumber[];
	private int itemCount;
	
	public MajorCourseBag(Course majorCourses[]) {
		this.majorCourses = majorCourses;
	}
	private void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public String getMajorName() {
		return majorName;
	}
	public MajorCourseBag(int maxSize) {
		majorCourses = new Course[maxSize];
	}
	public MajorCourseBag(String major) {
		this.importMajor("DEFAULT", major.toUpperCase());
	}
	public MajorCourseBag(String file, String major) {
		this.importMajor(file, major.toUpperCase());
	}
	public int getNumberOfCourses() {
		return itemCount;
	}
	public void add(Course course) {
		if (isDuplicate(course.getCourseNumber()))
			System.out.println(course.getCourseTitle() + ", Course Number: " + course.getCourseNumber() + " appears to be a duplicate. Please reenter it correctly or exclude its further entry.");
		else
			majorCourses[itemCount++] = course;
	}
	public void display() {
		System.out.println("Courses Required: \n");
		for (int i = 0; i < itemCount; i++) {
			if (majorCourses[i] == null)
				break;
			else
				System.out.println(majorCourses[i].toString());
		}
	}
	public Course getCourse(int index) {
		return majorCourses[index];
	}
	public Course[] getMajorCourses() {
		return majorCourses;
	}
	public String[] getCourseNumbers() {
		return courseNumber;
	}
	public boolean isDuplicate(String courseNumber) {
		if (this.find(courseNumber) == -1)
			return false;
		else
			return true;
	}
	public void fillCourseNumber() {
		courseNumber = new String[itemCount];
		for (int i = 0; i < courseNumber.length; i++) {
			courseNumber[i] = majorCourses[i].getCourseNumber();
		}
	}
	
	public int find(String courseNumber) {
		for (int i = 0; i < itemCount; i++) {
			if (courseNumber.equals(this.majorCourses[i].getCourseNumber())) {
					return i;
			}
		}
		return -1;
	}
	public void parseCoursesFromMaster(MasterCourseBag masterBag) {
		for (int i = 0; i < courseNumber.length; i++) {
			this.add(masterBag.getCourse(masterBag.find(courseNumber[i])));
		}
	}
	public void exportMajor(String major) { //Appends to an existing majorFile.txt - if it doesn't exist, it creates a new one.
		major = "*" + major + "*".toUpperCase();
		File checkFile = new File("majorFile.txt");
		Writer output = null;
		try {
			if (!checkFile.exists()) {
				checkFile.createNewFile();
			}
			output = new FileWriter(checkFile, true); //true is used to append the data as opposed to replacing the file.
			output.write("\n" + major + "\n");
			for (int i = 0; i < courseNumber.length; i++)
			{
				output.write(courseNumber[i] + "&&& ");
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
			output.close();
			} catch(IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
	public void importMajor(String fileName, String major) { //Designed to read major-specific courses, not the entire text file.
		major = "*" + major + "*".toUpperCase();
		if (fileName.equals("DEFAULT"))
			fileName = "majorFile.txt";
		File file = null;
		Scanner input = null;
		try {
			file = new File(fileName);
			input = new Scanner(file);
			while (input.hasNextLine()) {
				if (input.nextLine().equals(major)) {
					setMajorName(major);
					String majorCodes = input.nextLine();
					courseNumber = majorCodes.split("&&& ");
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
	}
	
}

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
import java.io.Serializable;
import java.io.Writer;
import java.util.Scanner;

public class MajorCourseBag implements Serializable {
	private String majorName;
	private String courseNumber[];
	private String courseNumberStringFormat;
	private int itemCount = 0;
	
	public MajorCourseBag(String major) {
		this.importMajor("DEFAULT", major.toUpperCase());
	}
	public MajorCourseBag(String file, String major) {
		this.importMajor(file, major.toUpperCase());
	}
	public MajorCourseBag(int size) {
		courseNumber = new String[size];
	}
	public String[] getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String[] courseNumber) {
		this.courseNumber = courseNumber;
	}
	public void setNumberOfCourses(int itemCount) {
		this.itemCount = itemCount;
	}
	public void setCourseNumberStringFormat(String courseNumberStringFormat) {
		this.courseNumberStringFormat = courseNumberStringFormat;
	}
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public void add(String inputNumber) {
		try {
		courseNumber[itemCount++] = inputNumber;
		} catch (ArrayIndexOutOfBoundsException e) {
			itemCount--;
			increaseArraySize();
			add(inputNumber);
		}
		courseNumberArrayToString();
	}
	public void increaseArraySize() {
		String[] newCourses = new String[courseNumber.length + 1];
		for (int i = 0; i < courseNumber.length; i++) {
			newCourses[i] = courseNumber[i];
		}
		courseNumber = newCourses.clone();
	}
	public String delete(String inputNumber) {
		int index = this.find(inputNumber);
		if (index == -1) {
			System.out.println("This Course does not exist.");
			return null;
		}
		else {
			String deletedCourse = courseNumber[index];
			for (int i = index; i < itemCount-1; i++) {
				if (i == itemCount-1)
				{
					courseNumber[i] = null;
				}
				else {
				courseNumber[i] = courseNumber[i+1];
				}
			}
			itemCount--;
			courseNumberArrayToString();
			return deletedCourse;
		}
	}
	public String getCourseNumberStringFormat() {
		return courseNumberStringFormat;
	}
	private void courseNumberArrayToString() {
		String convertedString = "";
		for (int i = 0; i < courseNumber.length; i++) {
			convertedString += " " + courseNumber[i];
		}
		courseNumberStringFormat = convertedString;
	}
	public String getMajorName() {
		return majorName;
	}
	public int getNumberOfCourses() {
		return itemCount;
	}
	public String[] getCourseNumbers() {
		return courseNumber;
	}
	public boolean isDuplicate(String inputNumber) {
		if (this.find(inputNumber) == -1)
			return false;
		else
			return true;
	}
	public int find(String inputNumber) {
		for (int i = 0; i < itemCount; i++) {
			if (inputNumber.equals(courseNumber[i])) {
					return i;
			}
		}
		return -1;
	}
	public void parseCoursesFromMaster(MasterCourseBag masterBag) {
		for (int i = 0; i < courseNumber.length; i++) {
			this.add(masterBag.getCourse(masterBag.find(courseNumber[i])).getCourseNumber());
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
					setMajorName(major.substring(1, major.length()-1));
					String majorCodes = input.nextLine();
					courseNumber = majorCodes.split("&&& ");
					itemCount = courseNumber.length;
					this.courseNumberArrayToString();
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

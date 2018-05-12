package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Scanner;

import view.Util;

public class AllMajorBags implements Serializable {
	private MajorCourseBag[] allMajors;
	private int itemCount = 0;
	public AllMajorBags(MajorCourseBag[] allMajors, int itemCount) {
		this.allMajors = allMajors;
		this.itemCount = itemCount;
	}
	public AllMajorBags(int maxSize) {
		allMajors = new MajorCourseBag[maxSize];
	}
	public AllMajorBags(String fileName) {
		allMajors = new MajorCourseBag[1];
		importAllMajors(fileName);
	}
	public int find(String majorName) {
		for (int i = 0; i < itemCount; i++){
			if (allMajors[i].getMajorName().equals(majorName)) {
				return i;
			}
		}
		return -1;
	}
	public boolean isDuplicate(String isbn) {
		if (this.find(isbn) == -1)
			return false; //It's not a duplicate, thanks to the find method returning -1 if no isbn is found
		else
			return true;
	}
	public int getItemCount() {
		return itemCount;
	}
	public MajorCourseBag getMajor(int index) {
		return allMajors[index];
	}
	public MajorCourseBag delete(String majorName) {
 		int index = this.find(majorName);
		if (index == -1) {
			System.out.println("This Course does not exist.");
			return null;
		}
		else {
			MajorCourseBag deletedMajor = allMajors[index];
			for (int i = index; i < itemCount-1; i++) {
				if (i == itemCount-1)
				{
					allMajors[i] = null;
				}
				else {
				allMajors[i] = allMajors[i+1];
				}
			}
			itemCount--;
			return deletedMajor;
		}
	}
	public void add(MajorCourseBag majorBag) {
		if (isDuplicate(majorBag.getMajorName())){
			Util.displayError("This major has already been created");
		}
		try {
		allMajors[itemCount++] = majorBag;
		} catch (ArrayIndexOutOfBoundsException e) {
			itemCount--;
			increaseArraySize();
			add(majorBag);
		}
	}
	public void increaseArraySize() {
		MajorCourseBag[] newMajors = new MajorCourseBag[allMajors.length + 1];
		for (int i = 0; i < allMajors.length; i++) {
			newMajors[i] = allMajors[i];
		}
		allMajors = newMajors.clone();
	}
	public void save() {
		ObjectOutputStream objectOutput = null;
		try {
			FileOutputStream fileOutput = new FileOutputStream("allMajors.dat");
			objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(allMajors);
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
			fileInput = new FileInputStream("allMajors.dat");
			objectInput = new ObjectInputStream(fileInput);
			allMajors = (MajorCourseBag[])objectInput.readObject();
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
	public void importAllMajors(String fileName) { //Designed to read entire major course file, and accept every major from the file. No need for an export, it already exists.
		if (fileName.equals("DEFAULT"))
			fileName = "majorFile.txt";
		File file = null;
		Scanner input = null;
		try {
			file = new File(fileName);
			input = new Scanner(file);
			while (input.hasNextLine()) {
				MajorCourseBag majorCourseBag;
				String majorName = "";
				String inputLine = input.nextLine();
				if (inputLine.charAt(0) == '*') {
					majorName = inputLine.substring(1, inputLine.length()-1);
					majorCourseBag = new MajorCourseBag(majorName);
					this.add(majorCourseBag);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
	}
	@Override
	public String toString() {
		String arraysString = "\n";
		for (int i = 0; i < allMajors.length; i++) {
			String[] courseNumbers = allMajors[i].getCourseNumbers();
			arraysString += allMajors[i].getMajorName() + " ";
			for (int j = 0; j < courseNumbers.length; j++) {
				arraysString += courseNumbers[j] + " ";
			}
			arraysString += "\n";
		}
		return "All Majors: " + arraysString + "Item Count:" + itemCount;
	}
}

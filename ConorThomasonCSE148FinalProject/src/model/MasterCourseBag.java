package model;

/*
 * The MasterCourseBag should contain courses taken, courses currently taking, and courses to be taken to graduate in a given major for each student. 
 * The information should be stored in one array or three arrays as String objects in the form of course numbers. 
 * Course grade should also be included for each course the student took. 
 * If you would like to use one array, you should also have a data field called courseType to indicate whether the students took, is taking or will need to take the said course for a given major.
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

public class MasterCourseBag implements java.io.Serializable {
	private Course courses[];
	private int itemCount;

	public MasterCourseBag(int maxSize) { //Creates the MasterCourseBag, requires a maximum size. 
		courses = new Course[maxSize]; //One of those times that makes me think dynamic arrays *might* exist in Java, but I honestly can't find anything that doesn't say *Doesn't exist*
	}
	public void add(Course course) {
		if (this.isDuplicate(course.getCourseNumber()))
			System.out.println(course.getCourseTitle() + ", Course Number: " + course.getCourseNumber() + " appears to be a duplicate. Please reenter it correctly or exclude its further entry.");
		else
			courses[itemCount++] = course;
	}
	public void display() { //Prints all of the active course entries - any entries that are null are ignored in the output.
		System.out.println("Courses Stored:");
		for (int i = 0; i < itemCount; i++) {
			if (courses[i] == null)
				break;
			else
				System.out.print(courses[i].toString() + "\n");
		}
		System.out.println();
	}
	public Course getCourse(int index) {
		return courses[index];
	}
	public int find(String course) { //Search array for course via Course number, returns index number in array. If nothing is found, returns -1.
		for (int i = 0; i < itemCount; i++) {
			if (course.equals(this.courses[i].getCourseNumber()))
				return i;
		}
		return -1;
	}
	public boolean isDuplicate(String course) {
		if (this.find(course) == -1)
			return false; //It's not a duplicate, thanks to the find method returning -1 if no course number is found
		else
			return true;
	}
	public Course delete(String courseNumber) {
 		int index = this.find(courseNumber);
		if (index == -1) {
			System.out.println("This Course does not exist.");
			return null;
		}
		else {
			Course deletedCourse = courses[index];
			for (int i = index; i < itemCount-1; i++) {
				if (i == itemCount-1)
				{
					courses[i] = null;
				}
				else {
				courses[i] = courses[i+1];
				}
			}
			itemCount--;
			return deletedCourse;
		}
	}
	public void save() {
		ObjectOutputStream objectOutput = null;
		try {
			FileOutputStream fileOutput = new FileOutputStream("courses.dat");
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
			fileInput = new FileInputStream("courses.dat");
			objectInput = new ObjectInputStream(fileInput);
			courses = (Course[])objectInput.readObject();
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
	public void exportSerialized() { //These aren't actually used for exporting/importing, was toying with the idea of serialization being the import/export method. 
		//It works, I just wasn't sure if other platforms serialized the same way.
		File file = null; //Instantiate file early so the catches can be used later
		ObjectOutputStream objectOutput = null;
		try {
			file = new File("coursesExport.txt"); //File created, given name for export. File will always have this name.
			objectOutput = new ObjectOutputStream(new FileOutputStream(file));
			for (int i = 0; i < itemCount; i++)
			{
				objectOutput.writeObject(courses[i]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				objectOutput.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void importSerialized(String fileName) {
		FileInputStream fileInput = null;
		ObjectInputStream objectInput = null;
		try {
			fileInput = new FileInputStream(fileName); //Users can import a file with any name, as long as it's serialized.
			objectInput = new ObjectInputStream(fileInput);
			while(true) {
				try {
					courses[itemCount] = (Course)objectInput.readObject();				
				} catch (EOFException e) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				objectInput.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void exportData() {
		File file = null;
		Writer output = null;
		try {
			file = new File("masterCoursesExport.txt");
			output = new FileWriter(file);
			for (int i = 0; i < itemCount; i++) {
				output.write(courses[i].getCourseTitle() + "&&& ");
				output.write(courses[i].getCourseNumber() + "&&& ");
				if (courses[i].getTextbookAssigned() != null) {
					output.write(courses[i].getTextbookAssigned().getBookTitle() + "&&& ");
					output.write(courses[i].getTextbookAssigned().getAuthorName() + "&&& ");
					output.write(courses[i].getTextbookAssigned().getPublisher() + "&&& ");
					output.write(courses[i].getTextbookAssigned().getBookPrice() + "&&& ");
					output.write(courses[i].getTextbookAssigned().getIsbn() + "&&& ");
				}
				output.write(courses[i].getNumberOfCredits() + "&&& \n");
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
			fileName = "masterCoursesExport.txt";
		File file = null;
		Scanner input = null;
		try {
			file = new File(fileName);
			input = new Scanner(file).useDelimiter("&&& ");
			while(input.hasNext()) {
				Textbook inputTextbook = new Textbook(" "," "," "," ","");
				Course inputCourse = new Course("","",inputTextbook,0);
				inputCourse.setCourseTitle(input.next());
				inputCourse.setCourseNumber(input.next());
				try {
					if (!input.hasNextInt())
					{
							inputTextbook.setBookTitle(input.next());
							inputTextbook.setAuthorName(input.next());
							inputTextbook.setPublisher(input.next());
							inputTextbook.setBookPrice(input.next());
							inputTextbook.setIsbn(input.next());
					}
				}catch (NoSuchElementException e) {
					break;
				}
				inputCourse.setTextbookAssigned(inputTextbook);
				inputCourse.setNumberOfCredits(input.nextInt());
				this.add(inputCourse);
				input.nextLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
	}
	public int getNumberOfCourses() {
		return itemCount;
	}
}

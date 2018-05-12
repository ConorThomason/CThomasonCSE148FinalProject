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

public class TextBookBag implements java.io.Serializable {
	private Textbook textbooks[];
	private int itemCount;

	public TextBookBag(int maxSize) { //Creates the TextbookBag, requires a maximum size. 
		try {
		textbooks = new Textbook[maxSize]; //One of those times that makes me think dynamic arrays *might* exist in Java, but I honestly can't find anything that doesn't say *Doesn't exist*
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public void add(Textbook textbook) {
		if (this.isDuplicate(textbook.getIsbn()))
			System.out.println(textbook.getBookTitle() + ", ISBN: " + textbook.getIsbn() + " appears to be a duplicate. Please reenter it correctly or exclude its further entry.");
		else if (!textbook.checkValidIsbn(textbook.getIsbn()))
			System.out.println(textbook.getBookTitle() + ", ISBN: " + textbook.getIsbn() + " appears to have an invalid ISBN. Please reenter it correctly or exclude its further entry.");
		else
			textbooks[itemCount++] = textbook;
	}
	public void display() { //Prints all of the active textbook entries - any entries that are null are ignored in the output.
		System.out.println("Textbooks Stored:");
		for (int i = 0; i < itemCount; i++) {
			if (textbooks[i] == null)
				break;
			else
				System.out.print(textbooks[i].toString() + "\n");
		}
		System.out.println();
	}
	public int find(String isbn) { //Search array for textbook via ISBN, returns index number in array. If nothing is found, returns -1.
		for (int i = 0; i < itemCount; i++) {
			if (isbn.equals(this.textbooks[i].getIsbn()))
				return i;
		}
		return -1;
	}
	public boolean isDuplicate(String isbn) {
		if (this.find(isbn) == -1)
			return false; //It's not a duplicate, thanks to the find method returning -1 if no isbn is found
		else
			return true;
	}
	public Textbook delete(String isbn) {
		int index = this.find(isbn);
		if (index == -1) {
			System.out.println("This Textbook does not exist.");
			return null;
		}
		else {
			Textbook deletedTextbook = textbooks[index];
			for (int i = index; i < itemCount-1; i++) {
				if (i == itemCount-1)
				{
					textbooks[i] = null;
				}
				else {
				textbooks[i] = textbooks[i+1];
				}
			}
			itemCount--;
			return deletedTextbook;
		}
	}
	public Textbook getTextbook(String isbn) { //Uses find method to return an actual textbook, if found in array. If it isn't, returns null.
		int arrayIndex = this.find(isbn);
		if (arrayIndex == -1) {
			return null;
		}
		else
			return textbooks[arrayIndex];
	}
	public Textbook getTextbookDirect(int index) {
		return textbooks[index];
	}
	public void save() {
		ObjectOutputStream objectOutput = null;
		try {
			FileOutputStream fileOutput = new FileOutputStream("textbooks.dat");
			objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(textbooks);
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
	public void load() { //Unlike the import/export methods, the load method will overwrite any data that is already in the textbooks array
		ObjectInputStream objectInput = null;
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream("textbooks.dat");
			objectInput = new ObjectInputStream(fileInput);
			textbooks = (Textbook[])objectInput.readObject();
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
			file = new File("textbooksExport.txt"); //File created, given name for export. File will always have this name.
			objectOutput = new ObjectOutputStream(new FileOutputStream(file));
			for (int i = 0; i < itemCount; i++)
			{
				objectOutput.writeObject(textbooks[i]);
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
					textbooks[itemCount] = (Textbook)objectInput.readObject();				
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
			file = new File("textbooksExport.txt");
			output = new FileWriter(file);
			for (int i = 0; i < itemCount; i++) {
				output.write(textbooks[i].getBookTitle() + "&&& ");
				output.write(textbooks[i].getAuthorName() + "&&& ");
				output.write(textbooks[i].getPublisher() + "&&& ");
				output.write(textbooks[i].getBookPrice() + "&&& ");
				output.write(textbooks[i].getIsbn() + "&&& \n");
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
	public void importData(String fileName) { //If you attempt to add a textbook with the same ISBN as a textbook already in the array, a message will be output and the entry won't be input.
		if (fileName.equals("DEFAULT"))
			fileName = "textbooksExport.txt";
		File file = null;
		Scanner input = null;
		try {
			file = new File(fileName);
			input = new Scanner(file).useDelimiter("&&& ");
			while(input.hasNext()) {
				Textbook inputTextbook = new Textbook(" "," "," "," "," ");
				inputTextbook.setBookTitle(input.next());
				inputTextbook.setAuthorName(input.next());
				inputTextbook.setPublisher(input.next());
				inputTextbook.setBookPrice(input.next());
				inputTextbook.setIsbn(input.next());
				this.add(inputTextbook);
				input.nextLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			input.close();
		}
	}
	public int getNumberOfTextbooks() {
		return itemCount;
	}
}

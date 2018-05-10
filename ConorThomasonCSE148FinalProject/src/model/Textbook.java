package model;

public class Textbook implements java.io.Serializable {
	private String bookTitle;
	private String authorName;
	private String publisher;
	private String bookPrice;
	private String isbn;

	public Textbook(String bookTitle, String authorName, String publisher, String bookPrice, String isbn) {
		try {
		this.setBookTitle(bookTitle);
		this.setAuthorName(authorName);
		this.setPublisher(publisher);
		this.setBookPrice(bookPrice);
		this.setIsbn(isbn);
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public Textbook(Textbook textbook) {
		try {
			this.setBookTitle(textbook.getBookTitle());
			this.setAuthorName(textbook.getAuthorName());
			this.setPublisher(textbook.getPublisher());
			this.setBookPrice(textbook.getBookPrice());
			this.setIsbn(textbook.getIsbn());
		} catch (IllegalArgumentException e)
		{
			System.out.println("An invalid parameter has been provided in the creation of this " + this.getClass().getSimpleName() + ", please check your entry and try again");
		}
	}
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		try {
			this.bookTitle = bookTitle;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Book Title entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		try {
			this.authorName = authorName;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Author's Name entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		try {
			this.publisher = publisher;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Publisher entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
	}
	public String getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(String bookPrice) {
		try {
			this.bookPrice = bookPrice;
		} catch (IllegalArgumentException e)
		{
			System.out.println("The Book Title entry for this " + this.getClass().getSimpleName() + ", appears to be an incorrect/invalid entry, please try again");
		}
		String bookPriceToConvert = "";
		if (bookPrice.charAt(0) != '$') //Added a check for $ so that it would be uniform throughout
		{
			bookPriceToConvert += '$';
		}
		bookPriceToConvert += bookPrice;
		this.bookPrice = bookPriceToConvert;
	}
	public String getIsbn() {
		if (isbn == null) {
			return "TOBEINPUT";
		}
		else
			return isbn;
	}
	public void setIsbn(String isbn) {
		String isbnToConvert = "";
		try {
			if (checkValidIsbn(isbn) == false){
				throw new IsbnInvalidException();
			}
		}catch (IsbnInvalidException e) {
			System.out.println(e.getMessage());
		}
		for (int i = 0; i < isbn.length(); i++)
		{
			if (isbn.charAt(i) == '-')
				continue;
			else
				isbnToConvert += isbn.charAt(i);
		}
		this.isbn = isbnToConvert;
	}
	public boolean checkValidIsbn(String isbn) { //Uses a method to verify an ISBN by hand, transcribed into code.
		int productSum = 0;
		if (isbn.equals("TOBEINPUT")) {
			return true;
		}
		else if (isbn.length() == 13){
			for (int i = 12; i >= 0; i = i-2)
			{
				productSum += Character.getNumericValue(isbn.charAt(i));
				if (i > 0)
					productSum += Character.getNumericValue(isbn.charAt(i-1)) * 3;
			}
			if (productSum % 10 == 0)
				return true;
			else
				return false;
		}
		else if (isbn.length() == 10) {
			for (int i = 1; i >= 10; i++)
			{
				productSum += Character.getNumericValue(isbn.charAt(i)) * i;
			}
			if (productSum % 11 == 0)
				return true;
			else
				return false;
		}
		
		return false;
	}
	public String toString() {
		if (this.checkValidIsbn(this.getIsbn()) == true)
			return "Book Title: " + bookTitle + "\t Author's Name(s): " + authorName + "\t Publisher: " + publisher
					+ "\t Book Price: " + bookPrice + "\t ISBN:" + isbn;
		else
			return "No Textbook";
	}

}

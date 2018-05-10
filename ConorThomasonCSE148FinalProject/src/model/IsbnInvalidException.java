package model;

public class IsbnInvalidException extends Exception {
	
	public IsbnInvalidException(){
		super("The ISBN entered does not conform to the ISBN format; Attempted entry into the TextbookBag will be rejected");
	}
}

package model;

public class GpaTooLargeException extends Exception {
	
	public GpaTooLargeException() {
		super("GPA entered exceeds 4.0; reset GPA to 4.0");
	}

}

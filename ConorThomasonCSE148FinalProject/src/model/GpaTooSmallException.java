package model;

public class GpaTooSmallException extends Exception {
	public GpaTooSmallException() {
		super("GPA entered is less than 0; reset GPA to 0");
	}
}

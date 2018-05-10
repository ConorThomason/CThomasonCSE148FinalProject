package model;

public class ArrayTooSmallException extends Exception {
	public ArrayTooSmallException() {
		super("The array you are attempting to use has been defined as too small for another entry; please delete an entry or redefine your array boundaries");
	}
}

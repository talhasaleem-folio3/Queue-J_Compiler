package controller;



public class Token {
	
	String classPart;
	String valuePart;
	int lineNumber;
	
	public Token(String classPart, String valuePart, int lineNumber) {
		this.classPart = classPart;
		this.valuePart = valuePart;
		this.lineNumber = lineNumber;
	}

}

package controller;

import java.util.ArrayList;

public class ClassTable {

	private String className;
	private String classType;
	private String classAccessModifier;
	private String classFinal;
	private String classParent;
	private ArrayList<AttributeTable> link;

	public ClassTable(String className, String classType, String classAccessModifier, String classFinal,
			String classParent, ArrayList<AttributeTable> link) {
		super();
		this.className = className;
		this.classType = classType;
		this.classAccessModifier = classAccessModifier;
		this.classFinal = classFinal;
		this.classParent = classParent;
		this.link = link;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getClassAccessModifier() {
		return classAccessModifier;
	}

	public void setClassAccessModifier(String classAccessModifier) {
		this.classAccessModifier = classAccessModifier;
	}

	public String getClassFinal() {
		return classFinal;
	}

	public void setClassFinal(String classFinal) {
		this.classFinal = classFinal;
	}

	public String getClassParent() {
		return classParent;
	}

	public void setClassParent(String classParent) {
		this.classParent = classParent;
	}

	public ArrayList<AttributeTable> getLink() {
		return link;
	}

	public void setLink(ArrayList<AttributeTable> link) {
		this.link = link;
	}

}

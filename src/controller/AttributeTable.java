package controller;

import java.util.ArrayList;

public class AttributeTable {

	private String AttributeName;
	private String AttributeType;
	private String AttributeAccessModifier;
	private String AttributeStatic;
	private String AttributeFinal;
	private ArrayList<FunctionTable> functionLink;

	public AttributeTable(String attributeName, String attributeType, String attributeAccessModifier,
			String attributeStatic, String attributeFinal, ArrayList<FunctionTable> functionLink) {
		super();
		AttributeName = attributeName;
		AttributeType = attributeType;
		AttributeAccessModifier = attributeAccessModifier;
		AttributeStatic = attributeStatic;
		AttributeFinal = attributeFinal;
		this.functionLink = functionLink;
	}

	public String getAttributeName() {
		return AttributeName;
	}

	public void setAttributeName(String attributeName) {
		AttributeName = attributeName;
	}

	public String getAttributeType() {
		return AttributeType;
	}

	public void setAttributeType(String attributeType) {
		AttributeType = attributeType;
	}

	public String getAttributeAccessModifier() {
		return AttributeAccessModifier;
	}

	public void setAttributeAccessModifier(String attributeAccessModifier) {
		AttributeAccessModifier = attributeAccessModifier;
	}

	public String getAttributeStatic() {
		return AttributeStatic;
	}

	public void setAttributeStatic(String attributeStatic) {
		AttributeStatic = attributeStatic;
	}

	public String getAttributeFinal() {
		return AttributeFinal;
	}

	public void setAttributeFinal(String attributeFinal) {
		AttributeFinal = attributeFinal;
	}

	public ArrayList<FunctionTable> getFunctionLink() {
		return functionLink;
	}

	public void setFunctionLink(ArrayList<FunctionTable> functionLink) {
		this.functionLink = functionLink;
	}

}

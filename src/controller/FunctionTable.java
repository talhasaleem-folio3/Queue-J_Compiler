package controller;

public class FunctionTable {

	private String functionAttributeName;
	private String functionAttributeType;
	private int scopeValue;

	public FunctionTable(String functionAttributeName, String functionAttributeType, int scopeValue) {
		super();
		this.functionAttributeName = functionAttributeName;
		this.functionAttributeType = functionAttributeType;
		this.scopeValue = scopeValue;
	}

	public String getFunctionAttributeName() {
		return functionAttributeName;
	}

	public void setFunctionAttributeName(String functionAttributeName) {
		this.functionAttributeName = functionAttributeName;
	}

	public String getFunctionAttributeType() {
		return functionAttributeType;
	}

	public void setFunctionAttributeType(String functionAttributeType) {
		this.functionAttributeType = functionAttributeType;
	}

	public int getScopeValue() {
		return scopeValue;
	}

	public void setScopeValue(int scopeValue) {
		this.scopeValue = scopeValue;
	}

}

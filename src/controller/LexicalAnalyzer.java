package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

	private Pattern p;
	private Pattern p1;
	private Matcher m;
	private Matcher m1;
	private String[][] keywords;
	private String[][] punctuators;
	private String[][] operators;
	private ArrayList<Token> tokens;
	private SemanticCumSyntaxAnalyzer sa;

	public LexicalAnalyzer() throws FileNotFoundException {

		this.keywords = new String[33][2];
		this.punctuators = new String[14][2];
		this.operators = new String[27][2];
		initializeKeywords();
		initializePunctuators();
		initializeOperators();
		tokens = new ArrayList<>();
		lA();
		this.sa = new SemanticCumSyntaxAnalyzer(tokens);
	}
	
	public String getCompileMessage(){
		return sa.compileMessage();
	}
	
	public String getTokenString(){
		String value = "";
		for(int i = 0; i < tokens.size(); i++){
			value += ("(" + tokens.get(i).classPart + ", " + tokens.get(i).valuePart + ", "
					+ tokens.get(i).lineNumber + ")\t\t\t");
			if(i%2 == 0){
				value += "\n";
			}
		}
		
		return value;
	}

	private void check(String word, int lineNumber) {

		if (isKeyword(word) != " ") {
			tokens.add(new Token(isKeyword(word), word, lineNumber));
		} else if (isID(word)) {
			tokens.add(new Token("ID", word, lineNumber));
		} else if (isPunctuator(word) != " ") {
//			tokens.add(new Token(isPunctuator(word), word, lineNumber));
			if(isPunctuator(word).equals("\'")){
				tokens.add(new Token("Invalid_Lexeme", word, lineNumber));
			} else {
				tokens.add(new Token(isPunctuator(word), word, lineNumber));
			}
		} else if (isInteger(word)) {
			tokens.add(new Token("Int_Constant", word, lineNumber));
		} else if (isString(word)) {
			word = word.substring(1, word.length()-1);
			tokens.add(new Token("String_Constant", word, lineNumber));
		} else if (isChar(word)) {
			word = word.substring(1, word.length()-1);
			tokens.add(new Token("Char_Constant", word, lineNumber));
		} else if (isFloat(word)) {
			tokens.add(new Token("Float_Constant", word, lineNumber));
		} else if (isOperator(word) != " ") {
			tokens.add(new Token(isOperator(word), word, lineNumber));
		} else {
			tokens.add(new Token("Invalid_Lexeme", word, lineNumber));
		}
	}

	private void lA() throws FileNotFoundException {
		File inputFile = new File("file.txt");
		Scanner in = new Scanner(inputFile);

		int lineNumber = 1;
		while (in.hasNextLine()) {

			String input = in.nextLine();

			int count = 0;
			String temp = "";

			ABC: while (count < input.length()) {

				// If It is Space Or Tab

				if (isSpace(input.charAt(count)) || isTab(input.charAt(count))) {
					if (temp != "") {
						check(temp, lineNumber);
						temp = "";
					}
					count++; // Two space Issue
					while (true) {
						if (count < input.length()) {
							if (isSpace(input.charAt(count)) || isTab(input.charAt(count))) {
								count++;
							} else {
								if (temp != "") {
									check(temp, lineNumber);
									temp = "";
									break;
								}
								break;
							}
						} else {
							continue ABC;
						}

					}
				}

				// If It is a Punctuator

				else if (("" + input.charAt(count)).equals(isPunctuator(("" + input.charAt(count))))) {
					if (temp != "") {

						if (input.charAt(count) == '.') {
							boolean flag = true;
							for (int i = 0; i < temp.length(); i++) {
								if (!(temp.charAt(i) >= '0' && temp.charAt(i) <= '9')) {
									flag = false;
									break;
								}
							}

							if (flag) {
								if ((count + 1) < input.length()) {
									if (input.charAt(count + 1) >= '0' && input.charAt(count + 1) <= '9') {
										temp += input.charAt(count);
										count++;
										continue ABC;
									}
								}
							}
						}
						check(temp, lineNumber);
					}

					temp = "" + input.charAt(count);

					if (temp.equals(".")) {
						if ((count + 1) < input.length()) {
							if (input.charAt(count + 1) >= '0' && input.charAt(count + 1) <= '9') {
								count++;
								continue ABC;
							}
						}
					}

					if (temp.equals("\"")) {
						count++;
						while (true) {
							if (count < input.length()) {
								temp += input.charAt(count);
								if (input.charAt(count) == '\"' && input.charAt(count - 1) != '\\'
										&& input.charAt(count - 2) != '\\') {
									break;
								}
								count++;
							} else {
								continue ABC;
							}

						}

					} else if (temp.equals("\'")) {
						count++;
						if (count < input.length()) {
							if (input.charAt(count) == '\\') {
								temp += input.charAt(count);
								count++;
							}
						}

						for (int i = 0; i < 2; i++) {
							if (count < input.length()) {
								temp += input.charAt(count);
								if (i == 0)
									count++;
							} else {
								break;
							}
						}
					}
					check(temp, lineNumber);
					count++;
					temp = "";

				}

				// If it is a Operator

				else if (isOperator(("" + input.charAt(count))) != " ") {
					if (temp != "") {
						check(temp, lineNumber);
					}

					temp = "" + input.charAt(count);

					if (temp.equals("+")) {
						if (input.charAt(count + 1) == '+' || input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}
					} else if (temp.equals("-")) {
						if (input.charAt(count + 1) == '-' || input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}
					} else if (temp.equals("%")) {
						if (input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}

					} else if (temp.equals("*")) {
						if (input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}

					} else if (temp.equals("=")) {
						if (input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}
					} else if (temp.equals(">")) {
						if (input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}
					} else if (temp.equals("<")) {
						if (input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}
					} else if (temp.equals("!")) {
						if (input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}
					} else if (temp.equals("/")) {
						if ((count + 1) < input.length()) {
							if (input.charAt(count + 1) == '=') {
								temp += input.charAt(count + 1);
								count++;
							} else if (input.charAt(count + 1) == '/') {
								temp = "";
								break;
							} else if (input.charAt(count + 1) == '*') {
								count += 2;

								while (true) {
									if (count < input.length()) {
										if (input.charAt(count) == '*') {
											if (input.charAt(count + 1) == '/') {
												count += 2;
												temp = "";
												continue ABC;
											}
										}
										count++;
									} else {
										if (in.hasNextLine()) {
											input = in.nextLine();
											count = 0;
											lineNumber++;
										} else {
											break ABC;
										}
									}

								}
							}
						}
					} else if (temp.equals("&")) {
						if (input.charAt(count + 1) == '&' || input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}
					} else if (temp.equals("|")) {
						if (input.charAt(count + 1) == '|' || input.charAt(count + 1) == '=') {
							temp += input.charAt(count + 1);
							count++;
						}
					}
					// else if (temp.equals("\"")) {
					// count++;
					// while (true) {
					// if (count < input.length()) {
					// temp += input.charAt(count);
					// if (input.charAt(count) == '\"' && input.charAt(count -
					// 1) != '\\') {
					// break;
					// }
					// count++;
					// } else {
					// continue ABC;
					// }
					//
					// }
					//
					// } else if (temp.equals("\'")) {
					// count++;
					// if (count < input.length()) {
					// if (input.charAt(count) == '\\') {
					// temp += input.charAt(count);
					// count++;
					// }
					// }
					//
					// for (int i = 0; i < 2; i++) {
					// if (count < input.length()) {
					// temp += input.charAt(count);
					// count++;
					// } else {
					// break;
					// }
					// }
					// }

					check(temp, lineNumber);
					temp = "";
					count++;

				} else {
					temp += input.charAt(count);
					count++;
				}

			}
			if (temp != "") {
				check(temp, lineNumber);
			}
			lineNumber++;
		}

		in.close();
	}

	private boolean isSpace(char word) {
		return (word) == ' ';
	}

	private boolean isTab(char word) {
		return (word) == '\t';
	}

	private void initializeOperators() {
		operators[0][0] = "+";
		operators[0][1] = "P_M";

		operators[1][0] = "-";
		operators[1][1] = "P_M";

		operators[2][0] = "*";
		operators[2][1] = "MDM";

		operators[3][0] = "/";
		operators[3][1] = "MDM";

		operators[4][0] = "%";
		operators[4][1] = "MDM";

		operators[5][0] = "++";
		operators[5][1] = "INC_DEC";

		operators[6][0] = "--";
		operators[6][1] = "INC_DEC";

		operators[7][0] = "<";
		operators[7][1] = "ROP";

		operators[8][0] = ">";
		operators[8][1] = "ROP";

		operators[9][0] = "<=";
		operators[9][1] = "ROP";

		operators[10][0] = ">=";
		operators[10][1] = "ROP";

		operators[11][0] = "!=";
		operators[11][1] = "ROP";

		operators[12][0] = "==";
		operators[12][1] = "ROP";

		operators[13][0] = "&";
		operators[13][1] = "&";

		operators[14][0] = "|";
		operators[14][1] = "|";

		operators[15][0] = "~";
		operators[15][1] = "~";

		operators[16][0] = "&&";
		operators[16][1] = "&&";

		operators[17][0] = "||";
		operators[17][1] = "||";

		operators[18][0] = "!";
		operators[18][1] = "!";

		operators[19][0] = "=";
		operators[19][1] = "AOP";

		operators[20][0] = "+=";
		operators[20][1] = "ASOP";

		operators[21][0] = "-=";
		operators[21][1] = "ASOP";

		operators[22][0] = "*=";
		operators[22][1] = "ASOP";

		operators[23][0] = "/=";
		operators[23][1] = "ASOP";

		operators[24][0] = "%=";
		operators[24][1] = "ASOP";

		operators[25][0] = "&=";
		operators[25][1] = "ASOP";

		operators[26][0] = "|=";
		operators[26][1] = "|=";
	}

	private void initializePunctuators() {
		punctuators[0][0] = "[";
		punctuators[0][1] = "[";

		punctuators[1][0] = "]";
		punctuators[1][1] = "]";

		punctuators[2][0] = "{";
		punctuators[2][1] = "{";

		punctuators[3][0] = "}";
		punctuators[3][1] = "}";

		punctuators[4][0] = "(";
		punctuators[4][1] = "(";

		punctuators[5][0] = ")";
		punctuators[5][1] = ")";

		punctuators[6][0] = ",";
		punctuators[6][1] = ",";

		punctuators[7][0] = ";";
		punctuators[7][1] = ";";

		punctuators[8][0] = ":";
		punctuators[8][1] = ":";

		punctuators[9][0] = "*";
		punctuators[9][1] = "*";

		punctuators[10][0] = "\"";
		punctuators[10][1] = "\"";

		punctuators[11][0] = "\'";
		punctuators[11][1] = "\'";

		punctuators[12][0] = "\\";
		punctuators[12][1] = "\\";

		punctuators[13][0] = ".";
		punctuators[13][1] = ".";
	}

	private void initializeKeywords() {
		keywords[0][0] = "boolean";
		keywords[0][1] = "DT";

		keywords[1][0] = "while";
		keywords[1][1] = "while";

		keywords[2][0] = "break";
		keywords[2][1] = "break";

		keywords[3][0] = "void";
		keywords[3][1] = "void";

		keywords[4][0] = "case";
		keywords[4][1] = "case";

		keywords[5][0] = "switch";
		keywords[5][1] = "switch";

		keywords[6][0] = "char";
		keywords[6][1] = "DT";

		keywords[7][0] = "super";
		keywords[7][1] = "super";

		keywords[8][0] = "class";
		keywords[8][1] = "class";

		keywords[9][0] = "static";
		keywords[9][1] = "static";

		keywords[10][0] = "continue";
		keywords[10][1] = "continue";

		keywords[11][0] = "return";
		keywords[11][1] = "return";

		keywords[12][0] = "default";
		keywords[12][1] = "default";

		keywords[13][0] = "protected";
		keywords[13][1] = "Access Modifier";

		keywords[14][0] = "do";
		keywords[14][1] = "do";

		keywords[15][0] = "public";
		keywords[15][1] = "Access Modifier";

		keywords[16][0] = "double";
		keywords[16][1] = "DT";

		keywords[17][0] = "private";
		keywords[17][1] = "Access Modifier";

		keywords[18][0] = "else";
		keywords[18][1] = "else";

		keywords[19][0] = "long";
		keywords[19][1] = "DT";

		keywords[20][0] = "extends";
		keywords[20][1] = "extends";

		keywords[21][0] = "new";
		keywords[21][1] = "new";

		keywords[22][0] = "int";
		keywords[22][1] = "DT";

		keywords[23][0] = "if";
		keywords[23][1] = "if";

		keywords[24][0] = "for";
		keywords[24][1] = "for";

		keywords[25][0] = "float";
		keywords[25][1] = "DT";

		keywords[26][0] = "final";
		keywords[26][1] = "final";

		keywords[27][0] = "String";
		keywords[27][1] = "DT";

		keywords[28][0] = "true";
		keywords[28][1] = "bool Constant";

		keywords[29][0] = "false";
		keywords[29][1] = "bool Constant";

		keywords[30][0] = "try";
		keywords[30][1] = "try";

		keywords[31][0] = "this";
		keywords[31][1] = "this";

		keywords[32][0] = "catch";
		keywords[32][1] = "catch";

	}

	private String isKeyword(String word) {
		for (int i = 0; i < keywords.length; i++) {
			if (word.equals(keywords[i][0])) {
				return keywords[i][1];
			}
		}
		return " ";
	}

	private String isPunctuator(String word) {
		for (int i = 0; i < punctuators.length; i++) {
			if (word.equals(punctuators[i][0])) {
				return punctuators[i][1];
			}
		}
		return " ";
	}

	private String isOperator(String word) {
		for (int i = 0; i < operators.length; i++) {
			if (word.equals(operators[i][0])) {
				return operators[i][1];
			}
		}
		return " ";
	}

	private boolean isID(String word) {
		p = Pattern.compile("(_+(\\w)*|[a-zA-Z](\\w)*)+");
		m = p.matcher(word);

		if (m.matches()) {
			return true;
		}

		return false;
	}

	private boolean isInteger(String word) {
		p = Pattern.compile("[0-9]{1,}[eE]?[+-]?[0-9]{0,}");
		m = p.matcher(word);

		if (m.matches()) {
			return true;
		}

		return false;

	}

	private boolean isFloat(String word) {
		p = Pattern.compile("[+-]?[0-9]{0,}\\.[0-9]{0,}([eE][+-]?[0-9]{0,})?");
		m = p.matcher(word);

		if (m.matches()) {
			return true;
		}

		return false;

	}

	private boolean isChar(String word) {
		p = Pattern.compile("'(\\[tvrnafb\\]|[^\\'])'");
		p1 = Pattern.compile("'\\\\.'");
		m = p.matcher(word);
		m1 = p1.matcher(word);
		if (m.matches() || m1.matches()) {
			return true;
		}

		return false;

	}

	private boolean isString(String word) {
		p = Pattern.compile("(\".*\")");
		m = p.matcher(word);

		if (m.matches()) {
			return true;
		}

		return false;

	}

}

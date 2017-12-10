package controller;

import java.util.ArrayList;

public class SemanticCumSyntaxAnalyzer {

	// For Syntax

	private ArrayList<Token> token;
	private int tokenNum;
	private String message;

	// For Semantic

	private static int scope;
	private ArrayList<ClassTable> classTable;
	private ClassTable currentClass;
	private int numOfClasses;
	
	// For Intermediate
	
	private ArrayList<String> tags;

	private String classLookUp(String className) {
		for (int i = 0; i < classTable.size(); i++) {
			if (classTable.get(i).getClassName().equals(className)) {
				return classTable.get(i).getClassFinal();
			}
		}
		return null;
	}

	private void classInsert(String className, String classType, String classAccessModifier, String classFinal,
			String classParent, ArrayList<AttributeTable> link) {

		classTable.add(new ClassTable(className, classType, classAccessModifier, classFinal, classParent, link));
	}

	private String attributeLookUp(String attributeName, String attributeType, ArrayList<AttributeTable> link) {

		for (int i = 0; i < link.size(); i++) {
			if (link.get(i).getAttributeName().equals(attributeName)
					&& link.get(i).getAttributeType().equals(attributeType)) {
				return link.get(i).getAttributeType();
			}
		}

		return null;
	}

	private void attributeInsert(String attributeName, String attributeType, String attributeAccessModifier,
			String attributeStatic, String attributeFinal, ArrayList<FunctionTable> link) {
		currentClass.getLink().add(new AttributeTable(attributeName, attributeType, attributeAccessModifier,
				attributeStatic, attributeFinal, link));

	}

	public SemanticCumSyntaxAnalyzer(ArrayList<Token> token) {
		this.token = token;
		this.token.add(new Token("$", "$", 0));
		this.tokenNum = 0;
		SemanticCumSyntaxAnalyzer.scope = 0;
		this.classTable = new ArrayList<>();
		this.numOfClasses = -1;
		this.tags = new ArrayList<>();

		if (validate()) {
			this.message = "Successfully Compiled";
		} else {
			if (this.token.get(tokenNum).lineNumber == 0) {
				this.message = "Successfully Compiled";
			}
			this.message = "Semantic Error at Line Num: " + this.token.get(tokenNum).lineNumber;
		}
	}

	public String compileMessage() {
		return this.message;
	}

	private boolean validate() {

		if (start()) {
			if (this.token.get(tokenNum).valuePart.equals("$"))
				return true;
		}

		return false;
	}

	private boolean start() {

		String AM = classAccessMod("");
		if (AM.equals("default") || AM.equals("public")) {
			
			String finals = finals("");
			if (finals.equals("final") || finals.equals("object")) {
				
				if (token.get(tokenNum).classPart.equals("class")) {
					tokenNum++;
					String classType = "class";

					if (token.get(tokenNum).classPart.equals("ID")) {
						String N = token.get(tokenNum).valuePart;
						tokenNum++;

						String T = classLookUp(N);

						if (T != null) {
							return false;
						}

						String PC = extend("");
						if (!PC.equals("error")) {
							
							numOfClasses++;
							classInsert(N, classType, AM, finals, PC, new ArrayList<>());
							currentClass = classTable.get(numOfClasses);
							
							if (token.get(tokenNum).classPart.equals("{")) {
								tokenNum++;
								if (XX()) {
									if (token.get(tokenNum).classPart.equals("}")) {
										tokenNum++;
										if (start()) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	private String classAccessMod(String AM) {

		if (token.get(tokenNum).valuePart.equals("public")) {
			tokenNum++;
			AM = "public";
			return AM;
		}
		AM = "default";
		return AM;
	}

	private String extend(String PC) {

		if (token.get(tokenNum).classPart.equals("extends")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				PC = token.get(tokenNum).valuePart;
				tokenNum++;

				String T = classLookUp(PC);

				if (T == "final") {
					return "error";
				}

				if (T == null) {
					return "error";
				}
			}
			return PC;
		}
		return "-";
	}

	private String finals(String finals) {

		if (token.get(tokenNum).classPart.equals("final")) {
			tokenNum++;
			finals = "final";
			return finals;
		}
		finals = "object";
		return finals;
	}

	private boolean XX() {

		if (class_Declaration()) {
			if (XX()) {
				return true;
			}
		}
		return true;
	}

	private String accessMod(String AM) {

		if (token.get(tokenNum).classPart.equals("Access Modifier")) {
			AM = token.get(tokenNum).valuePart;
			tokenNum++;
			return AM;
		}
		return "default";
	}

	// private boolean statics() {
	//
	// if (token.get(tokenNum).classPart.equals("static")) {
	// tokenNum++;
	// return true;
	// }
	//
	// return true;
	// }

	private boolean constant() {

		if (token.get(tokenNum).classPart.equals("Int_Constant")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("Float_Constant")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("Char_Constant")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("String_Constant")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("bool Constant")) {
			tokenNum++;
			return true;
		}

		return false;
	}

	// private boolean for_Inc_Dec() {
	//
	// if (AB()) {
	// if (token.get(tokenNum).classPart.equals("INC_DEC")) {
	// tokenNum++;
	// return true;
	// }
	// }
	//
	// else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
	// tokenNum++;
	// if (AB()) {
	// return true;
	// }
	// }
	// return false;
	// }

	private boolean AB() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (e()) {
				if (point()) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean point() {

		if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (AB()) {
				return true;
			}
		}

		return true;
	}

	// private boolean calling() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (LF()) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	private boolean LF() {

		if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (param()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (e()) {
						if (c()) {
							return true;
						}
					}
				}
			}

		}

		else if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals("]")) {
					tokenNum++;
					if (c()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (f()) {
				return true;
			}
		}

		return false;
	}

	private boolean MST() {

		if (sst()) {
			if (MST()) {
				return true;
			}
		}

		return true;
	}

	private boolean returns() {

		if (token.get(tokenNum).classPart.equals("return")) {
			tokenNum++;
			if (look()) {
				return true;
			}
		}
		return false;
	}

	private boolean look() {

		if (token.get(tokenNum).classPart.equals(";")) {
			tokenNum++;
			return true;
		}

		else if (expression()) {
			if (token.get(tokenNum).classPart.equals(";")) {
				tokenNum++;
				return true;
			}
		}

		return false;
	}

	private boolean if_sst() {

		if (token.get(tokenNum).classPart.equals("if")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("(")) {
				tokenNum++;
				if (expression()) {
					if (token.get(tokenNum).classPart.equals(")")) {
						tokenNum++;
						if (body()) {
							if (oElse()) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean oElse() {

		if (token.get(tokenNum).classPart.equals("else")) {
			tokenNum++;
			if (body()) {
				return true;
			}
		}

		return true;
	}

	private boolean body() {

		if (token.get(tokenNum).classPart.equals(";")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("{")) {
			tokenNum++;
			if (MST()) {
				if (token.get(tokenNum).classPart.equals("}")) {
					tokenNum++;
					return true;
				}
			}
		}

		else if (sst()) {
			return true;
		}

		return false;
	}

	private boolean do_while_sst() {

		if (token.get(tokenNum).classPart.equals("do")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("{")) {
				tokenNum++;
				if (MST()) {
					if (token.get(tokenNum).classPart.equals("}")) {
						tokenNum++;
						if (token.get(tokenNum).classPart.equals("while")) {
							tokenNum++;
							if (token.get(tokenNum).classPart.equals("(")) {
								tokenNum++;
								if (expression()) {
									if (token.get(tokenNum).classPart.equals(")")) {
										tokenNum++;
										if (token.get(tokenNum).classPart.equals(";")) {
											tokenNum++;
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean tryCatch() {

		if (token.get(tokenNum).classPart.equals("try")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("{")) {
				tokenNum++;
				if (MST()) {
					if (token.get(tokenNum).classPart.equals("}")) {
						tokenNum++;
						if (token.get(tokenNum).classPart.equals("catch")) {
							tokenNum++;
							if (token.get(tokenNum).classPart.equals("(")) {
								tokenNum++;
								if (token.get(tokenNum).classPart.equals("ID")) {
									tokenNum++;
									if (token.get(tokenNum).classPart.equals("ID")) {
										tokenNum++;
										if (token.get(tokenNum).classPart.equals(")")) {
											tokenNum++;
											if (token.get(tokenNum).classPart.equals("{")) {
												tokenNum++;
												if (MST()) {
													if (token.get(tokenNum).classPart.equals("}")) {
														tokenNum++;
														return true;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean whileLoop() {

		if (token.get(tokenNum).classPart.equals("while")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("(")) {
				tokenNum++;
				if (expression()) {
					if (token.get(tokenNum).classPart.equals(")")) {
						tokenNum++;
						if (body()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean switch_sst() {

		if (token.get(tokenNum).classPart.equals("switch")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("(")) {
				tokenNum++;
				if (expression()) {
					if (token.get(tokenNum).classPart.equals(")")) {
						tokenNum++;
						if (token.get(tokenNum).classPart.equals("{")) {
							tokenNum++;
							if (syntax()) {
								if (token.get(tokenNum).classPart.equals("}")) {
									tokenNum++;
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean syntax() {

		if (cases()) {
			if (syntax()) {
				return true;
			}
		}

		else if (defaults()) {
			return true;
		}

		return true;
	}

	private boolean cases() {

		if (token.get(tokenNum).classPart.equals("case")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(":")) {
					tokenNum++;
					if (switch_body()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean switch_body() {

		if (body()) {
			return true;
		}

		return true;
	}

	private boolean defaults() {

		if (token.get(tokenNum).classPart.equals("default")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals(":")) {
				tokenNum++;
				if (switch_body()) {
					if (caseChoice()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean caseChoice() {

		if (cases()) {
			if (caseChoice()) {
				return true;
			}
		}

		return true;
	}

	private boolean for_sst() {

		if (token.get(tokenNum).classPart.equals("for")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("(")) {
				tokenNum++;
				if (f1()) {
					if (f2()) {
						if (token.get(tokenNum).classPart.equals(";")) {
							tokenNum++;
							if (f3()) {
								if (token.get(tokenNum).classPart.equals(")")) {
									tokenNum++;
									if (body()) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean f1() {

		if (token.get(tokenNum).classPart.equals("DT")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF26()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF51()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("this")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals(".")) {
				tokenNum++;
				if (assign_sst()) {
					if (test()) {
						if (token.get(tokenNum).classPart.equals(";")) {
							tokenNum++;
							return true;
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals(";")) {
			tokenNum++;
			return true;
		}

		return false;
	}

	private boolean LF51() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF27()) {
				return true;
			}
		}

		else if (e()) {
			if (X()) {
				if (test()) {
					if (token.get(tokenNum).classPart.equals(";")) {
						tokenNum++;
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean f2() {

		if (expression()) {
			return true;
		}
		return true;
	}

	private boolean f3() {

		if (token.get(tokenNum).classPart.equals("this")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals(".")) {
				tokenNum++;
				if (assign_sst()) {
					if (test()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (e()) {
				if (LF52()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				return true;
			}
		}

		return true;
	}

	private boolean LF52() {

		if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (e()) {
					if (LF52()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (Y()) {
				if (test()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ASOP")) {
			tokenNum++;
			if (expression()) {
				if (test()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			return true;
		}

		return false;
	}

	private boolean for_Assign() {

		if (assignment()) {
			if (test()) {
				return true;
			}
		}

		return false;
	}

	private boolean test() {

		if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (for_Assign()) {
				return true;
			}
		}

		return true;
	}

	// private boolean func_call() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("(")) {
	// tokenNum++;
	// if (param()) {
	// if (token.get(tokenNum).classPart.equals(")")) {
	// tokenNum++;
	// if (e()) {
	// if (c()) {
	// return true;
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean e() {

		if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals("]")) {
					tokenNum++;
					return true;
				}
			}
		}

		return true;
	}

	private boolean fun_syntax() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("(")) {
				tokenNum++;
				if (param()) {
					if (token.get(tokenNum).classPart.equals(")")) {
						tokenNum++;
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean param() {

		if (param2()) {
			return true;
		}

		return true;
	}

	private boolean param2() {

		if (expression()) {
			if (comma()) {
				return true;
			}
		}

		return false;
	}

	private boolean comma() {

		if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (param2()) {
				return true;
			}
		}

		return true;
	}

	// private boolean array_get() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("[")) {
	// tokenNum++;
	// if (expression()) {
	// if (token.get(tokenNum).classPart.equals("]")) {
	// tokenNum++;
	// if (c()) {
	// return true;
	// }
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean c() {

		if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (d()) {
				if (c()) {
					return true;
				}
			}
		}

		return true;
	}

	private boolean d() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF1()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF1() {

		if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (param()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (e()) {
						if (c()) {
							return true;
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals("]")) {
					tokenNum++;
					return true;
				}
			}
		}

		return true;
	}

	// private boolean array_syntax() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("[")) {
	// tokenNum++;
	// if (expression()) {
	// if (token.get(tokenNum).classPart.equals("]")) {
	// tokenNum++;
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean obj_call() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals(".")) {
	// tokenNum++;
	// if (f()) {
	// return true;
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean f() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF2()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF2() {

		if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals("]")) {
					tokenNum++;
					if (c()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (param()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (e()) {
						if (c()) {
							return true;
						}
					}
				}
			}
		}

		else if (g()) {
			return true;
		}

		return false;
	}

	private boolean g() {

		if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (f()) {
				return true;
			}
		}

		return true;
	}

	// private boolean attribute1() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (dec()) {
	// return true;
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean LF4() {
	//
	// if (dec()) {
	// return true;
	// }
	//
	// else if (token.get(tokenNum).classPart.equals("final")) {
	// tokenNum++;
	// if (dec1()) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	// private boolean attribute1_0() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (dec()) {
	// return true;
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean dec() {
	//
	// if (token.get(tokenNum).classPart.equals("DT")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (init()) {
	// if (list()) {
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean init() {

		if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (LF5()) {
				return true;
			}
		}

		return true;
	}

	private boolean LF5() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF6()) {
				return true;
			}
		}

		else if (LF7()) {
			return true;
		}

		return false;
	}

	private boolean LF6() {

		if (init()) {
			return true;
		}

		else if (LF3()) {
			if (op()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF7() {

		if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (op()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (op()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (op()) {
					return true;
				}
			}
		}

		else if (constant()) {
			if (op()) {
				return true;
			}
		}

		return false;
	}

	private boolean op() {

		if (MDM2()) {
			if (PM2()) {
				if (relational2()) {
					if (bit_And2()) {
						if (bit_OR2()) {
							if (cond_And2()) {
								if (cond_OR2()) {
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean list() {

		if (token.get(tokenNum).classPart.equals(";")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (init()) {
					if (list()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	// private boolean attribute1_1() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (token.get(tokenNum).classPart.equals("final")) {
	// tokenNum++;
	// if (dec1()) {
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean dec1() {
	//
	// if (token.get(tokenNum).classPart.equals("DT")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (init1()) {
	// if (list1()) {
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean init1() {

		if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (LF8()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF8() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF9()) {
				return true;
			}
		} else if (LF7()) {
			return true;
		}

		return false;
	}

	private boolean LF9() {

		if (LF3()) {
			if (op()) {
				return true;
			}
		}

		return true;
	}

	private boolean list1() {

		if (token.get(tokenNum).classPart.equals(";")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (init1()) {
					if (list1()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	// private boolean objects() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (LF10()) {
	// return true;
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean LF10() {
	//
	// if (obj_dec()) {
	// return true;
	// }
	//
	// else if (token.get(tokenNum).classPart.equals("final")) {
	// tokenNum++;
	// if (obj_dec1()) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	// private boolean object1() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (obj_dec()) {
	// return true;
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean obj_dec() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (obj_init()) {
	// if (obj_list()) {
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean obj_init() {

		if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (LF11()) {
				return true;
			}
		}

		return true;
	}

	private boolean LF11() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (obj_init()) {
				return true;
			}
		}

		else if (obj_create()) {
			return true;
		}

		return false;
	}

	private boolean obj_list() {

		if (token.get(tokenNum).classPart.equals(";")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (obj_init()) {
				if (obj_list()) {
					return true;
				}
			}
		}

		return false;
	}

	// private boolean object2() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (token.get(tokenNum).classPart.equals("final")) {
	// tokenNum++;
	// if (obj_dec1()) {
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean obj_dec1() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (obj_init1()) {
	// if (obj_list1()) {
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean obj_init1() {

		if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (LF12()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF12() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			return true;
		}

		else if (obj_create()) {
			return true;
		}

		return false;
	}

	private boolean obj_list1() {

		if (token.get(tokenNum).classPart.equals(";")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (obj_init1()) {
					if (obj_list1()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean obj_create() {

		if (token.get(tokenNum).classPart.equals("new")) {
			tokenNum++;
			if (fun_syntax()) {
				return true;
			}
		}

		return false;
	}

//	int tagNum = 0;
	
	private boolean expression() {

		if (token.get(tokenNum).classPart.equals("ID")) {
//			tags.add(token.get(tokenNum).valuePart);
			tokenNum++;
			if (LF3()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									if (cond_And2()) {
										if (cond_OR2()) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									if (cond_And2()) {
										if (cond_OR2()) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (MDM2()) {
						if (PM2()) {
							if (relational2()) {
								if (bit_And2()) {
									if (bit_OR2()) {
										if (cond_And2()) {
											if (cond_OR2()) {
												return true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									if (cond_And2()) {
										if (cond_OR2()) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		else if (constant()) {
			if (MDM2()) {
				if (PM2()) {
					if (relational2()) {
						if (bit_And2()) {
							if (bit_OR2()) {
								if (cond_And2()) {
									if (cond_OR2()) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean cond_OR2() {

		if (token.get(tokenNum).classPart.equals("||")) {
			tokenNum++;
			if (cond_And()) {
				if (cond_OR2()) {
					return true;
				}
			}
		}
		return true;
	}

	private boolean cond_And() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF3()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									if (cond_And2()) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									if (cond_And2()) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (MDM2()) {
						if (PM2()) {
							if (relational2()) {
								if (bit_And2()) {
									if (bit_OR2()) {
										if (cond_And2()) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									if (cond_And2()) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		else if (constant()) {
			if (MDM2()) {
				if (PM2()) {
					if (relational2()) {
						if (bit_And2()) {
							if (bit_OR2()) {
								if (cond_And2()) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean cond_And2() {

		if (token.get(tokenNum).classPart.equals("&&")) {
			tokenNum++;
			if (bit_OR()) {
				if (cond_And2()) {
					return true;
				}
			}
		}

		return true;
	}

	private boolean bit_OR() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF3()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									return true;
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									return true;
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (MDM2()) {
						if (PM2()) {
							if (relational2()) {
								if (bit_And2()) {
									if (bit_OR2()) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								if (bit_OR2()) {
									return true;
								}
							}
						}
					}
				}
			}
		}

		else if (constant()) {
			if (MDM2()) {
				if (PM2()) {
					if (relational2()) {
						if (bit_And2()) {
							if (bit_OR2()) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean bit_OR2() {

		if (token.get(tokenNum).classPart.equals("|")) {
			tokenNum++;
			if (bit_And()) {
				if (bit_OR2()) {
					return true;
				}
			}
		}

		return true;
	}

	private boolean bit_And() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF3()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								return true;
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								return true;
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (MDM2()) {
						if (PM2()) {
							if (relational2()) {
								if (bit_And2()) {
									return true;
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							if (bit_And2()) {
								return true;
							}
						}
					}
				}
			}
		}

		else if (constant()) {
			if (MDM2()) {
				if (PM2()) {
					if (relational2()) {
						if (bit_And2()) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean bit_And2() {

		if (token.get(tokenNum).classPart.equals("&")) {
			tokenNum++;
			if (relational()) {
				if (bit_And2()) {
					return true;
				}
			}
		}

		return true;
	}

	private boolean relational() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF3()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							return true;
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							return true;
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (MDM2()) {
						if (PM2()) {
							if (relational2()) {
								return true;
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (MDM2()) {
					if (PM2()) {
						if (relational2()) {
							return true;
						}
					}
				}
			}
		}

		else if (constant()) {
			if (MDM2()) {
				if (PM2()) {
					if (relational2()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean relational2() {

		if (token.get(tokenNum).classPart.equals("ROP")) {
			tokenNum++;
			if (PM()) {
				if (relational2()) {
					return true;
				}
			}
		}

		return true;
	}

	private boolean PM() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF3()) {
				if (MDM2()) {
					if (PM2()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (MDM2()) {
					if (PM2()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (MDM2()) {
						if (PM2()) {
							return true;
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (MDM2()) {
					if (PM2()) {
						return true;
					}
				}
			}
		}

		else if (constant()) {
			if (MDM2()) {
				if (PM2()) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean PM2() {

		if (token.get(tokenNum).classPart.equals("P_M")) {
			tokenNum++;
			if (MDM()) {
				if (PM2()) {
					return true;
				}
			}
		}

		return true;
	}

	private boolean MDM() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF3()) {
				if (MDM2()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (MDM2()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (MDM2()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (MDM2()) {
					return true;
				}
			}
		}

		else if (constant()) {
			if (MDM2()) {
				return true;
			}
		}

		return false;
	}

	private boolean MDM2() {

		if (token.get(tokenNum).classPart.equals("MDM")) {
			tokenNum++;
			if (F()) {
				if (MDM2()) {
					return true;
				}
			}
		}

		return true;
	}

	private boolean F() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF3()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				return true;
			}
		}

		else if (constant()) {
			return true;
		}

		return false;
	}

	private boolean LF3() {

		if (LF()) {
			return true;
		}

		else if (e()) {
			if (point()) {
				return true;
			}
		}

		return true;
	}

	// private boolean array0() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (finals()) {
	// if (array()) {
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean array() {
	//
	// if (token.get(tokenNum).classPart.equals("DT")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("[")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("]")) {
	// tokenNum++;
	// if (array_choice()) {
	// if (array_initialize()) {
	// if (token.get(tokenNum).classPart.equals(";")) {
	// tokenNum++;
	// return true;
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean array_Dec() {
	//
	// if (token.get(tokenNum).classPart.equals("DT")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("[")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("]")) {
	// tokenNum++;
	// if (array_choice()) {
	// return true;
	// }
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean array_initialize() {

		if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (LF13()) {
				return true;
			}
		}

		return true;
	}

	private boolean LF13() {

		if (array_init1()) {
			return true;
		}

		else if (array_init2()) {
			return true;
		}

		return false;
	}

	private boolean array_choice() {

		if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (token.get(tokenNum).classPart.equals("[")) {
					tokenNum++;
					if (token.get(tokenNum).classPart.equals("]")) {
						tokenNum++;
						if (array_choice()) {
							return true;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean array_init1() {

		if (token.get(tokenNum).classPart.equals("new")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("DT")) {
				tokenNum++;
				if (token.get(tokenNum).classPart.equals("[")) {
					tokenNum++;
					if (expression()) {
						if (token.get(tokenNum).classPart.equals("]")) {
							tokenNum++;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean array_init2() {

		if (token.get(tokenNum).classPart.equals("{")) {
			tokenNum++;
			if (value()) {
				if (token.get(tokenNum).classPart.equals("}")) {
					tokenNum++;
					return true;
				}
			}
		}

		return false;
	}

	private boolean value() {

		if (expression()) {
			if (h()) {
				return true;
			}
		}

		return false;
	}

	private boolean h() {

		if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (value()) {
				return true;
			}
		}

		return true;
	}

	// private boolean obj_array_0() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (finals()) {
	// if (obj_array()) {
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean obj_array() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("[")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("]")) {
	// tokenNum++;
	// if (obj_array_initialize()) {
	// return true;
	// }
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	// private boolean obj_array_dec() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("[")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("]")) {
	// tokenNum++;
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean obj_array_initialize() {

		if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (obj_array_init()) {
				return true;
			}
		}

		return true;
	}

	private boolean obj_array_init() {

		if (obj_array_init1()) {
			return true;
		}

		else if (obj_array_init2()) {
			return true;
		}

		return false;
	}

	private boolean obj_array_init1() {

		if (token.get(tokenNum).classPart.equals("new")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (token.get(tokenNum).classPart.equals("[")) {
					tokenNum++;
					if (expression()) {
						if (token.get(tokenNum).classPart.equals("]")) {
							tokenNum++;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean obj_array_init2() {

		if (token.get(tokenNum).classPart.equals("{")) {
			tokenNum++;
			if (value2()) {
				if (token.get(tokenNum).classPart.equals("}")) {
					tokenNum++;
					return true;
				}
			}
		}

		return false;
	}

	private boolean value2() {

		if (obj_create()) {
			if (i()) {
				return true;
			}
		} else if (expression()) {
			if (i()) {
				return true;
			}
		}

		return false;
	}

	private boolean i() {

		if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (value2()) {
				return true;
			}
		}

		return true;
	}

	// private boolean method() {
	//
	// if (accessMod()) {
	// if (statics()) {
	// if (finals()) {
	// if (return_type()) {
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("(")) {
	// tokenNum++;
	// if (argument()) {
	// if (token.get(tokenNum).classPart.equals(")")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("{")) {
	// tokenNum++;
	// if (supers()) {
	// if (MST()) {
	// if (token.get(tokenNum).classPart.equals("}")) {
	// tokenNum++;
	// return true;
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean supers() {
		if (token.get(tokenNum).classPart.equals("super")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("(")) {
				tokenNum++;
				if (param()) {
					if (token.get(tokenNum).classPart.equals(")")) {
						tokenNum++;
						return true;
					}
				}
			}
		}
		return true;
	}

	// private boolean return_type() {
	//
	// if (token.get(tokenNum).classPart.equals("DT")) {
	// tokenNum++;
	// if (e()) {
	// return true;
	// }
	// }
	//
	// else if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (e()) {
	// return true;
	// }
	// }
	//
	// else if (token.get(tokenNum).classPart.equals("void")) {
	// tokenNum++;
	// return true;
	// }
	//
	// return false;
	// }

	private boolean type_data() {

		if (token.get(tokenNum).classPart.equals("DT")) {
			tokenNum++;
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			return true;
		}

		return false;
	}

	private boolean args() {

		if (type_data()) {
			if (e()) {
				if (token.get(tokenNum).classPart.equals("ID")) {
					tokenNum++;
					if (comma_check()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean argument() {

		if (args()) {
			return true;
		}

		return true;
	}

	private boolean comma_check() {

		if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (args()) {
				return true;
			}
		}

		return true;
	}

	// private boolean constructor() {
	//
	// if (accessMod()) {
	// if (token.get(tokenNum).classPart.equals("(")) {
	// tokenNum++;
	// if (argument()) {
	// if (token.get(tokenNum).classPart.equals(")")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals("{")) {
	// tokenNum++;
	// if (supers()) {
	// if (MST()) {
	// if (token.get(tokenNum).classPart.equals("}")) {
	// tokenNum++;
	// return true;
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean class_Declaration() {

		String AM = accessMod("");
		if (AM.equals("default") || AM.equals("private") || AM.equals("public") || AM.equals("protected")) {
			if (LF14()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF14() {
		
		

		if (token.get(tokenNum).classPart.equals("static")) {
			tokenNum++;
			if (LF15()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("final")) {
			tokenNum++;
			if (LF20()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("DT")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF23()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("void")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (token.get(tokenNum).classPart.equals("(")) {
					tokenNum++;
					if (argument()) {
						if (token.get(tokenNum).classPart.equals(")")) {
							tokenNum++;
							if (token.get(tokenNum).classPart.equals("{")) {
								tokenNum++;
								if (supers()) {
									if (MST()) {
										if (token.get(tokenNum).classPart.equals("}")) {
											tokenNum++;
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF25()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF15() {

		if (token.get(tokenNum).classPart.equals("final")) {
			tokenNum++;
			if (LF20()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("DT")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF23()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("void")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (token.get(tokenNum).classPart.equals("(")) {
					tokenNum++;
					if (argument()) {
						if (token.get(tokenNum).classPart.equals(")")) {
							tokenNum++;
							if (token.get(tokenNum).classPart.equals("{")) {
								tokenNum++;
								if (supers()) {
									if (MST()) {
										if (token.get(tokenNum).classPart.equals("}")) {
											tokenNum++;
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF24()) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean LF20() {

		if (token.get(tokenNum).classPart.equals("DT")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF21()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF22()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("void")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (token.get(tokenNum).classPart.equals("(")) {
					tokenNum++;
					if (argument()) {
						if (token.get(tokenNum).classPart.equals(")")) {
							tokenNum++;
							if (token.get(tokenNum).classPart.equals("{")) {
								tokenNum++;
								if (supers()) {
									if (MST()) {
										if (token.get(tokenNum).classPart.equals("}")) {
											tokenNum++;
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean LF21() {

		if (init1()) {
			if (list1()) {
				return true;
			}
		}

		else if (LF18()) {
			return true;
		}

		return false;
	}

	private boolean LF22() {

		if (obj_init1()) {
			if (obj_list1()) {
				return true;
			}
		}

		else if (LF19()) {
			return true;
		}

		return false;
	}

	private boolean LF18() {

		if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (argument()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (token.get(tokenNum).classPart.equals("{")) {
						tokenNum++;
						if (supers()) {
							if (MST()) {
								if (token.get(tokenNum).classPart.equals("}")) {
									tokenNum++;
									return true;
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("]")) {
				tokenNum++;
				if (array_choice()) {
					if (array_initialize()) {
						if (token.get(tokenNum).classPart.equals(";")) {
							tokenNum++;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean LF19() {

		if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (argument()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (token.get(tokenNum).classPart.equals("{")) {
						tokenNum++;
						if (supers()) {
							if (MST()) {
								if (token.get(tokenNum).classPart.equals("}")) {
									tokenNum++;
									return true;
								}
							}
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("]")) {
				tokenNum++;
				if (obj_array_initialize()) {
					if (token.get(tokenNum).classPart.equals(";")) {
						tokenNum++;
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean LF23() {

		if (LF18()) {
			return true;
		}

		else if (init()) {
			if (list()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF24() {

		if (LF19()) {
			return true;
		}

		else if (obj_init()) {
			if (obj_list()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF25() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF24()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (argument()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (token.get(tokenNum).classPart.equals("{")) {
						tokenNum++;
						if (supers()) {
							if (MST()) {
								if (token.get(tokenNum).classPart.equals("}")) {
									tokenNum++;
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	private boolean for_dec() {

		if (token.get(tokenNum).classPart.equals("DT")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF26()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF27()) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean LF26() {

		if (init()) {
			if (list()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("]")) {
				tokenNum++;
				if (array_choice()) {
					if (array_initialize()) {
						if (token.get(tokenNum).classPart.equals(";")) {
							tokenNum++;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean LF27() {

		if (obj_init()) {
			if (obj_list()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("]")) {
				tokenNum++;
				if (obj_array_initialize()) {
					if (token.get(tokenNum).classPart.equals(";")) {
						tokenNum++;
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean sst() {

		if (returns()) {
			return true;
		}

		else if (if_sst()) {
			return true;
		}

		else if (do_while_sst()) {
			return true;
		}

		else if (whileLoop()) {
			return true;
		}

		else if (tryCatch()) {
			return true;
		}

		else if (switch_sst()) {
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("static")) {
			tokenNum++;
			if (for_dec()) {
				return true;
			}
		}

		else if (kkk()) {
			return true;
		}

		else if (for_sst()) {
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("break")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals(";")) {
				tokenNum++;
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("continue")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals(";")) {
				tokenNum++;
				return true;
			}
		}

		return false;
	}

	private boolean kkk() {

		if (token.get(tokenNum).classPart.equals("DT")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (LF26()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (token.get(tokenNum).classPart.equals(";")) {
					tokenNum++;
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("this")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals(".")) {
				tokenNum++;
				if (assign_sst()) {
					if (token.get(tokenNum).classPart.equals(";")) {
						tokenNum++;
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF53()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF53() {

		if (MM()) {
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF27()) {
				return true;
			}
		}

		return false;
	}

	private boolean MM() {

		if (e()) {
			if (LF54()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF54() {

		if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("ID")) {
				tokenNum++;
				if (MM()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals(";")) {
				tokenNum++;
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (Y()) {
				if (token.get(tokenNum).classPart.equals(";")) {
					tokenNum++;
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ASOP")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(";")) {
					tokenNum++;
					return true;
				}
			}
		}

		return false;
	}

	// private boolean assignment() {
	//
	// if (thiss()) {
	// if (assign_sst()) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	// private boolean thiss() {
	//
	// if (token.get(tokenNum).classPart.equals("this")) {
	// tokenNum++;
	// if (token.get(tokenNum).classPart.equals(".")) {
	// tokenNum++;
	// return true;
	// }
	// }
	// return true;
	// }

	// private boolean assign_sst() {
	//
	// if (token.get(tokenNum).classPart.equals("ID")) {
	// tokenNum++;
	// if (e()) {
	// if (X()) {
	// return true;
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean assign_sst() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (TS()) {
				return true;
			}
		}

		return false;
	}

	private boolean assignment() {

		if (token.get(tokenNum).classPart.equals("this")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals(".")) {
				tokenNum++;
				if (token.get(tokenNum).classPart.equals("ID")) {
					tokenNum++;
					if (e()) {
						if (X()) {
							return true;
						}
					}
				}
			}
		}

		else if (assign_sst()) {
			return true;
		}

		return false;
	}

	private boolean TS() {

		if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (param()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					return true;
				}
			}
		}

		else if (e()) {
			if (X()) {
				return true;
			}
		}

		return false;
	}

	private boolean X() {

		if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (assign_sst()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (Y()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("ASOP")) {
			tokenNum++;
			if (expression()) {
				return true;
			}
		}

		return false;
	}

	private boolean Y() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF37()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("INC_DEC")) {
			tokenNum++;
			if (AB()) {
				if (op()) {
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (op()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("!")) {
			tokenNum++;
			if (F()) {
				if (op()) {
					return true;
				}
			}
		}

		else if (constant()) {
			if (op()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("new")) {
			tokenNum++;
			if (LF38()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("{")) {
			tokenNum++;
			if (LF39()) {
				return true;
			}
		}

		return false;
	}

	// private boolean Z() {
	//
	// if (token.get(tokenNum).classPart.equals("AOP")) {
	// tokenNum++;
	// if (Y()) {
	// return true;
	// }
	// }
	//
	// return true;
	// }

	private boolean LF38() {

		if (token.get(tokenNum).classPart.equals("DT")) {
			tokenNum++;
			if (token.get(tokenNum).classPart.equals("[")) {
				tokenNum++;
				if (expression()) {
					if (token.get(tokenNum).classPart.equals("]")) {
						tokenNum++;
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF40()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF40() {

		if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals("]")) {
					tokenNum++;
					return true;
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (param()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					return true;
				}
			}
		}

		return false;
	}

	private boolean LF39() {

		if (expression()) {
			if (LF41()) {
				return true;
			}
		}

		else if (obj_create()) {
			if (i()) {
				if (token.get(tokenNum).classPart.equals("}")) {
					tokenNum++;
					return true;
				}
			}
		}

		return false;
	}

	private boolean LF41() {

		if (token.get(tokenNum).classPart.equals(",")) {
			tokenNum++;
			if (LF39()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("}")) {
			tokenNum++;
			return true;
		}

		return false;
	}

	private boolean LF37() {

		if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals("]")) {
					tokenNum++;
					if (LF42()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (Y()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (param()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (e()) {
						if (c()) {
							return true;
						}
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (LF43()) {
				return true;
			}
		}

		return true;
	}

	private boolean LF42() {

		if (token.get(tokenNum).classPart.equals("AOP")) {
			tokenNum++;
			if (Y()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (LF49()) {
				return true;
			}
		}

		return true;
	}

	private boolean LF43() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF47()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF47() {

		if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals("]")) {
					tokenNum++;
					if (LF48()) {
						return true;
					}
				}
			}
		}

		else if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (LF43()) {
				return true;
			}
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (param()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (e()) {
						if (c()) {
							return true;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean LF48() {

		if (token.get(tokenNum).classPart.equals(".")) {
			tokenNum++;
			if (LF49()) {
				return true;
			}
		}

		return true;
	}

	private boolean LF49() {

		if (token.get(tokenNum).classPart.equals("ID")) {
			tokenNum++;
			if (LF50()) {
				return true;
			}
		}

		return false;
	}

	private boolean LF50() {

		if (token.get(tokenNum).classPart.equals("[")) {
			tokenNum++;
			if (expression()) {
				if (token.get(tokenNum).classPart.equals("]")) {
					tokenNum++;
					if (LF48()) {
						return true;
					}
				}
			}
		}

		else if (LF48()) {
			return true;
		}

		else if (token.get(tokenNum).classPart.equals("(")) {
			tokenNum++;
			if (param()) {
				if (token.get(tokenNum).classPart.equals(")")) {
					tokenNum++;
					if (e()) {
						if (c()) {
							if (c()) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

}

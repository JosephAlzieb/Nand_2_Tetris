package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class JackTokenizer {

  private Scanner scanner;
  private static ArrayList<String> keyWords;
  private static String symbols;
  private static String operations;
  private ArrayList<String> tokens;
  private String jackCode;
  private String tokenType;
  private String keyWord;
  private char symbol;
  private String identifier;
  private String stringVal;
  private int intVal;
  private static ArrayList<String> libraries;
  private int pointer;
  private boolean first;

  public JackTokenizer(File file) {
    try {
      scanner = new Scanner(new FileReader(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    jackCode = "";
    while (scanner.hasNextLine()) {
      String strLine = scanner.nextLine();
      while (strLine.equals("") || hasComments(strLine)) {
        if (hasComments(strLine)) {
          strLine = removeComments(strLine);
        }
        if (strLine.trim().equals("")) {
          if (scanner.hasNextLine()) {
            strLine = scanner.nextLine();
          } else {
            break;
          }
        }
      }
      jackCode += strLine.trim();
    }
    // add tokens from the jackcode string into an arraylist of the tokens
    tokens = new ArrayList<String>();
    while (jackCode.length() > 0) {
      while (jackCode.charAt(0) == ' ') {
        jackCode = jackCode.substring(1);
      }
      // keyword
      for (int i = 0; i < keyWords.size(); i++) {
        if (jackCode.startsWith(keyWords.get(i).toString())) {
          String keyword = keyWords.get(i).toString();
          tokens.add(keyword);
          jackCode = jackCode.substring(keyword.length());
        }

      }
      // symbol
      if (symbols.contains(jackCode.substring(0, 1))) {
        char symbol = jackCode.charAt(0);
        tokens.add(Character.toString(symbol));
        jackCode = jackCode.substring(1);
      }
      // integer constant
      else if (Character.isDigit(jackCode.charAt(0))) {
        String value = jackCode.substring(0, 1);
        jackCode = jackCode.substring(1);
        while (Character.isDigit(jackCode.charAt(0))) {
          value += jackCode.substring(0, 1);
          jackCode = jackCode.substring(1);

        }
        tokens.add(value);

      }
      // string constant
      else if (jackCode.substring(0, 1).equals("\"")) {
        jackCode = jackCode.substring(1);
        String strString = "\"";
        while ((jackCode.charAt(0) != '\"')) {
          strString += jackCode.charAt(0);
          jackCode = jackCode.substring(1);

        }
        strString = strString + "\"";
        tokens.add(strString);
        jackCode = jackCode.substring(1);

      }
      // identifier
      else if (Character.isLetter(jackCode.charAt(0)) || (jackCode.substring(0, 1).equals("_"))) {
        String strIdentifier = jackCode.substring(0, 1);
        jackCode = jackCode.substring(1);
        while ((Character.isLetter(jackCode.charAt(0))) || (jackCode.substring(0, 1).equals("_"))) {
          strIdentifier += jackCode.substring(0, 1);
          jackCode = jackCode.substring(1);
        }

        tokens.add(strIdentifier);

      }
      // start out with pointer at position 0
      first = true;
      pointer = 0;

    }

  }

  // list of keywords, symbols, and libraries for reference

  static {
    keyWords = new ArrayList<String>();
    keyWords.add("class");
    keyWords.add("constructor");
    keyWords.add("function");
    keyWords.add("method");
    keyWords.add("field");
    keyWords.add("static");
    keyWords.add("var");
    keyWords.add("int");
    keyWords.add("char");
    keyWords.add("boolean");
    keyWords.add("void");
    keyWords.add("true");
    keyWords.add("false");
    keyWords.add("null");
    keyWords.add("this");
    keyWords.add("do");
    keyWords.add("if");
    keyWords.add("else");
    keyWords.add("while");
    keyWords.add("return");
    keyWords.add("let");
    operations = "+-*/&|<>=";
    symbols = "{}()[].,;+-*/&|<>=-~";
    libraries = new ArrayList<String>();
    libraries.add("Array");
    libraries.add("Math");
    libraries.add("String");
    libraries.add("Array");
    libraries.add("Output");
    libraries.add("Screen");
    libraries.add("Keyboard");
    libraries.add("Memory");
    libraries.add("Sys");
    libraries.add("Square");
    libraries.add("SquareGame");
  }

  // test if the line argument has comments in it
  private boolean hasComments(String strLine) {
    boolean bHasComments = false;
    if (strLine.contains("//") || strLine.contains("/*") || strLine.startsWith(" *")) {
      bHasComments = true;
    }
    return bHasComments;

  }

  // removes comments from a line
  private String removeComments(String strLine) {
    String strNoComments = strLine;
    if (hasComments(strLine)) {
      int offSet;
      if (strLine.startsWith(" *")) {
        offSet = strLine.indexOf("*");
      } else if (strLine.contains("/*")) {
        offSet = strLine.indexOf("/*");
      } else {
        offSet = strLine.indexOf("//");
      }
      strNoComments = strLine.substring(0, offSet).trim();

    }
    return strNoComments;
  }

  public boolean hasMoreTokens() {
    return pointer < tokens.size() - 1;
  }

  public void advance() {
    if (hasMoreTokens()) {
      if (!first) {
        pointer++;
      }
      // if at position 0 of tokens, we do not want to increment yet
      else if (first) {
        first = false;
      }
      String currentItem = tokens.get(pointer);
      // assign current token type and corresponding field variable (keyword, symbol, intval, stringval, or identifier)
      // for this current token - position of where we are in the tokens array
      if (keyWords.contains(currentItem)) {
        tokenType = "KEYWORD";
        keyWord = currentItem;
      } else if (symbols.contains(currentItem)) {
        symbol = currentItem.charAt(0);
        tokenType = "SYMBOL";
      } else if (Character.isDigit(currentItem.charAt(0))) {
        intVal = Integer.parseInt(currentItem);
        tokenType = "INT_CONST";
      } else if (currentItem.substring(0, 1).equals("\"")) {
        tokenType = "STRING_CONST";
        stringVal = currentItem.substring(1, currentItem.length() - 1);
      } else if ((Character.isLetter(currentItem.charAt(0))) || (currentItem.charAt(0) == '_')) {
        tokenType = "IDENTIFIER";
        identifier = currentItem;
      }
    } else {
      return;
    }
  }

  public void decrementPointer() {
    if (pointer > 0) {
      pointer--;
    }
  }
  // returns the type of the current token - keyword, symbol, identifier, int_constant, or string_constant
  public String tokenType() {
    return tokenType;

  }

  // returns the keyword which is the current token, should be called only when tokenType() is keyword
  public String keyWord() {
    return keyWord;
  }

  // returns character which is current token, should be called only when tokenType() is symbol
  public char symbol() {
    return symbol;
  }

  // returns identifier which is the current token - should be called only when tokenType() is identifier
  public String identifier() {
    return identifier;
  }

  // returns integer value of the current token - should be called only when tokenType() is INT_CONST
  public int intVal() {
    return intVal;
  }

  // returns string value of current token without double quotes, should be called only when tokenType() is string_const
  public String stringVal() {
    return stringVal;
  }

  // indicates if a symbol is an operation, i.e., =, +, -, &, |, etc.
  public boolean isOperation() {
    for (int i = 0; i < operations.length(); i++) {
      if (operations.charAt(i) == symbol) {
        return true;
      }
    }
    return false;
  }
}

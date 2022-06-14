package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class JackTokenizer {

  private Scanner mScanner;
  private static ArrayList<String> keyWords;
  private static String symbols;
  private static String operations;
  private ArrayList<String> tokens;
  private String jackcode;
  private String mTokenType;
  private String mKeyWord;
  private char mSymbol;
  private String mIdentifier;
  private String mStringVal;
  private int mIntVal;
  private static ArrayList<String> libraries;
  private int pointer;
  private boolean bFirst;

  public JackTokenizer(File file) {
    try {
      mScanner = new Scanner(new FileReader(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    jackcode = "";
    while (mScanner.hasNextLine()) {
      String strLine = mScanner.nextLine();
      while (strLine.equals("") || hasComments(strLine)) {
        if (hasComments(strLine)) {
          strLine = removeComments(strLine);
        }
        if (strLine.trim().equals("")) {
          if (mScanner.hasNextLine()) {
            strLine = mScanner.nextLine();
          } else {
            break;
          }
        }
      }
      jackcode += strLine.trim();
    }
    // add tokens from the jackcode string into an arraylist of the tokens
    tokens = new ArrayList<String>();
    while (jackcode.length() > 0) {
      while (jackcode.charAt(0) == ' ') {
        jackcode = jackcode.substring(1);
      }
      // keyword
      for (int i = 0; i < keyWords.size(); i++) {
        if (jackcode.startsWith(keyWords.get(i).toString())) {
          String keyword = keyWords.get(i).toString();
          tokens.add(keyword);
          jackcode = jackcode.substring(keyword.length());
        }

      }
      // symbol
      if (symbols.contains(jackcode.substring(0, 1))) {
        char symbol = jackcode.charAt(0);
        tokens.add(Character.toString(symbol));
        jackcode = jackcode.substring(1);
      }
      // integer constant
      else if (Character.isDigit(jackcode.charAt(0))) {
        String value = jackcode.substring(0, 1);
        jackcode = jackcode.substring(1);
        while (Character.isDigit(jackcode.charAt(0))) {
          value += jackcode.substring(0, 1);
          jackcode = jackcode.substring(1);

        }
        tokens.add(value);

      }
      // string constant
      else if (jackcode.substring(0, 1).equals("\"")) {
        jackcode = jackcode.substring(1);
        String strString = "\"";
        while ((jackcode.charAt(0) != '\"')) {
          strString += jackcode.charAt(0);
          jackcode = jackcode.substring(1);

        }
        strString = strString + "\"";
        tokens.add(strString);
        jackcode = jackcode.substring(1);

      }
      // identifier
      else if (Character.isLetter(jackcode.charAt(0)) || (jackcode.substring(0, 1).equals("_"))) {
        String strIdentifier = jackcode.substring(0, 1);
        jackcode = jackcode.substring(1);
        while ((Character.isLetter(jackcode.charAt(0))) || (jackcode.substring(0, 1).equals("_"))) {
          strIdentifier += jackcode.substring(0, 1);
          jackcode = jackcode.substring(1);
        }

        tokens.add(strIdentifier);

      }
      // start out with pointer at position 0
      bFirst = true;
      pointer = 0;

    }

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


}

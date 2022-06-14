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

  }

}

package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CompilationEngine {

  private FileWriter fw;
  private JackTokenizer jtoken;
  private boolean bFirstRoutine;

  public CompilationEngine(File inFile, File outFile) {
    try {
      fw = new FileWriter(outFile);
      jtoken = new JackTokenizer(inFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    bFirstRoutine = true;
  }

  public void compileClass() {
    try {
      jtoken.advance();
      fw.write("<class>\n");
      fw.write("<keyword> class </keyword>\n");
      jtoken.advance();
      fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
      jtoken.advance();
      fw.write("<symbol> { </symbol>\n");
      compileClassVarDec();
      compileSubRoutine();
      fw.write("<symbol> } </symbol>\n");
      fw.write("</class>\n");
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // compiles a static declaration or a field declaration
  public void compileClassVarDec() {
    jtoken.advance();
    try {
      while (jtoken.keyWord().equals("static") || jtoken.keyWord().equals("field")) {
        fw.write("<classVarDec>\n");
        // field or static
        fw.write("<keyword> " + jtoken.keyWord() + " </keyword>\n");
        jtoken.advance();
        // if for example, field Square square (Square)
        if (jtoken.tokenType().equals("IDENTIFIER")) {
          fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
        }
        // if for example, field int square (int)
        else {
          fw.write("<keyword> " + jtoken.keyWord() + "</keyword>\n");

        }
        jtoken.advance();
        // third word of the classvardec - e.g. square in the above - field int square
        fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
        jtoken.advance();
        // if there are multiple in 1 line - e.g. field int x, y
        if (jtoken.symbol() == ',') {
          fw.write("<symbol> , </symbol>\n");
          jtoken.advance();
          fw.write(("<identifier> " + jtoken.identifier() + "</identifier>\n"));
          jtoken.advance();
        }
        // semicolon
        fw.write("<symbol> ; </symbol>\n");
        jtoken.advance();
        fw.write("</classVarDec>\n");
      }

      // if reach a subroutine, go back in the arraylist to accomodate for advance in the next call
      if (jtoken.keyWord().equals("function") || jtoken.keyWord().equals("method") || jtoken.keyWord().equals("constructor")) {
        jtoken.decrementPointer();
        return;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // compiles a complete method, function, or a constructor
  public void compileSubRoutine() {
    boolean hasSubRoutines = false;

    jtoken.advance();
    try {
      // once reach the end, return  - no more subroutines - base case for the recursive call
      if (jtoken.symbol() == '}' && jtoken.tokenType().equals("SYMBOL")) {
        return;
      }
      // subroutinedec tag
      if ((bFirstRoutine) && (jtoken.keyWord().equals("function") || jtoken.keyWord().equals("method") || jtoken.keyWord().equals("constructor"))) {
        bFirstRoutine = false;
        fw.write("<subroutineDec>\n");
        hasSubRoutines = true;
      }
      // function ,e
      if (jtoken.keyWord().equals("function") || jtoken.keyWord().equals("method") || jtoken.keyWord().equals("constructor")) {
        hasSubRoutines = true;
        fw.write("<keyword> " + jtoken.keyWord() + " </keyword>\n");
        jtoken.advance();
      }
      // if there is an identifier in the subroutine statement position 2 e.g. function Square getX()
      if (jtoken.tokenType().equals("IDENTIFIER")) {
        fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
        jtoken.advance();
      }
      // if keyword instead for subroutine statement position 2 e.g. function int getX()
      else if (jtoken.tokenType().equals("KEYWORD")) {
        fw.write("<keyword> " + jtoken.keyWord() + "</keyword>\n");
        jtoken.advance();
      }
      // name of the subroutine
      if (jtoken.tokenType().equals("IDENTIFIER")) {
        fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
        jtoken.advance();
      }
      // get parameters, or lack there of
      if (jtoken.symbol() == '(') {
        fw.write("<symbol> ( </symbol>\n");
        fw.write("<parameterList>\n");

        compileParameterList();
        fw.write("</parameterList>\n");
        fw.write("<symbol> ) </symbol>\n");

      }
      jtoken.advance();
      // start subroutine body
      if (jtoken.symbol() == '{') {
        fw.write("<subroutineBody>\n");
        fw.write("<symbol> { </symbol>\n");
        jtoken.advance();
      }
      // get all var declarations in the subroutine
      while (jtoken.keyWord().equals("var") && (jtoken.tokenType().equals("KEYWORD"))) {
        fw.write("<varDec>\n ");
        jtoken.decrementPointer();
        compileVarDec();
        fw.write(" </varDec>\n");
      }
      fw.write("<statements>\n");
      compileStatements();
      fw.write("</statements>\n");
      fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");
      if (hasSubRoutines) {
        fw.write("</subroutineBody>\n");
        fw.write("</subroutineDec>\n");
        bFirstRoutine = true;
      }

      // recursive call
      compileSubRoutine();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  // compiles a (possibly empty) parameter list including the "()"
  public void compileParameterList() {
    jtoken.advance();
    try {
      // until reach the end - )
      while (!(jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ')')) {
        if (jtoken.tokenType().equals("IDENTIFIER")) {
          fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
          jtoken.advance();
        } else if (jtoken.tokenType().equals("KEYWORD")) {
          fw.write("<keyword> " + jtoken.keyWord() + "</keyword>\n");
          jtoken.advance();
        }
        // commas separate the list, if there are multiple
        else if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == ',')) {
          fw.write("<symbol> , </symbol>\n");
          jtoken.advance();

        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // compiles a var declaration
  public void compileVarDec() {
    jtoken.advance();
    try {

      if (jtoken.keyWord().equals("var") && (jtoken.tokenType().equals("KEYWORD"))) {
        fw.write("<keyword> var </keyword>\n");
        jtoken.advance();
      }
      // type of var, if identifier, e.g. Square or Array
      if (jtoken.tokenType().equals("IDENTIFIER")) {
        fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
        jtoken.advance();
      }
      // type of var, if keyword, e.g. int or boolean
      else if (jtoken.tokenType().equals("KEYWORD")) {
        fw.write("<keyword> " + jtoken.keyWord() + " </keyword>\n");
        jtoken.advance();
      }
      // name of var
      if (jtoken.tokenType().equals("IDENTIFIER")) {
        fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
        jtoken.advance();
      }
      // if there are mutliple in 1 line
      if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == ',')) {
        fw.write("<symbol> , </symbol>\n");
        jtoken.advance();
        fw.write(("<identifier> " + jtoken.identifier() + "</identifier>\n"));
        jtoken.advance();
      }
      // end of var line
      if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == ';')) {
        fw.write("<symbol> ; </symbol>\n");
        jtoken.advance();

      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  // compiles a sequence of statements, not including the enclosing "{}" - do, let, if, while or return
  public void compileStatements() {
    try {
      if (jtoken.symbol() == '}' && (jtoken.tokenType().equals("SYMBOL"))) {
        return;
      } else if (jtoken.keyWord().equals("do") && (jtoken.tokenType().equals("KEYWORD"))) {
        fw.write("<doStatement>\n ");
        compileDo();
        fw.write((" </doStatement>\n"));

      } else if (jtoken.keyWord().equals("let") && (jtoken.tokenType().equals("KEYWORD"))) {
        fw.write("<letStatement>\n ");
        compileLet();
        fw.write((" </letStatement>\n"));
      } else if (jtoken.keyWord().equals("if") && (jtoken.tokenType().equals("KEYWORD"))) {
        fw.write("<ifStatement>\n ");
        compileIf();
        fw.write((" </ifStatement>\n"));
      } else if (jtoken.keyWord().equals("while") && (jtoken.tokenType().equals("KEYWORD"))) {
        fw.write("<whileStatement>\n ");
        compileWhile();
        fw.write((" </whileStatement>\n"));
      } else if (jtoken.keyWord().equals("return") && (jtoken.tokenType().equals("KEYWORD"))) {
        fw.write("<returnStatement>\n ");
        compileReturn();
        fw.write((" </returnStatement>\n"));
      }
      jtoken.advance();
      compileStatements();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


}

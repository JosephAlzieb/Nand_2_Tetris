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
}

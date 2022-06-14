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
}

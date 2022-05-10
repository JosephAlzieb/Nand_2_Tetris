package virtual_machine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

  private FileWriter fw;

  public CodeWriter(File fileOut) {
    try {
      fw = new FileWriter(fileOut);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeArithmetic(String command) {

  }

  public void writePushPop(CommandType commandType, String arg1, Integer arg2) {

  }
}

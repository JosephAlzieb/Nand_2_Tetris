package virtual_machine;

import java.io.File;
import java.util.ArrayList;

public class Main {

  public static void main(String[] args) {
    File fileIn = new File(args[0]);
    File fileOut;
    ArrayList<File> files = new ArrayList<>();
    if (args.length != 1)
      throw new IllegalArgumentException("Inaccurate usage. Please enter in the following format: java VMTranslator (directory/filename)");
    else if (fileIn.isFile() && !(args[0].endsWith(".vm")))
      throw new IllegalArgumentException("Not the correct file type. Please enter a .vm file or a directory containing .vm files. ");
    else if (fileIn.isFile() && args[0].endsWith(".vm")) {
      files.add(fileIn);
    } else // fileName is a directory - access all files in the directory
    {
      // get Files with .vm
    }
    fileOut = new File( "result.asm");
  }
}

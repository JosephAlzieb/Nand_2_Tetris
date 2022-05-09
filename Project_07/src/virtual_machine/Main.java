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
    } else
    {
      // get Files with .vm in the directory
      files = getVMFiles(fileIn);
    }
    fileOut = new File( "result.asm");

    ArrayList<Parser> parsers = new ArrayList<>();

    for (File file : files) {
      Parser parser = new Parser(file);
      parsers.add(parser);
    }
  }

  public static ArrayList<File> getVMFiles(File directory) {
    File[] files = directory.listFiles();
    ArrayList<File> allFiles = new ArrayList<>();
    if (files != null) {
      for (File file : files) {
        if (file.getName().endsWith(".vm")) allFiles.add(file);
      }
    }
    return allFiles;

  }
}

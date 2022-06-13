package compiler;

import java.io.File;
import java.util.ArrayList;

public class JackAnalyzer {

  public static void main(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("there is no files to translate");
    }

    File fileIn = new File(args[0]);
    File fileOut;
    ArrayList<File> files = new ArrayList<>();
    if (fileIn.isFile() && !(args[0].endsWith(".jack"))) {
      throw new IllegalArgumentException("incorrect file type.");
    } else {
      if (args[0].endsWith(".jack")) {
        files.add(fileIn);
        String firstPart = args[0].substring(0, args[0].length() - 5);
        fileOut = new File(firstPart + ".xml");
      }
      else{
        files = getJackFiles(fileIn);
        fileOut = new File(fileIn + ".xml");
      }
    }
  }

  public static ArrayList<File> getJackFiles(File directory) {
    File[] files = directory.listFiles();
    ArrayList<File> allFiles = new ArrayList<>();
    if (files != null) {
      for (File file : files) {
        if (file.getName().endsWith(".jack")) allFiles.add(file);
      }
    }
    return allFiles;
  }
}

package virtual_machine;

import static virtual_machine.CommandType.C_ARITHMETIC;
import static virtual_machine.CommandType.C_POP;
import static virtual_machine.CommandType.C_PUSH;

import java.io.File;
import java.util.ArrayList;

public class Main {

  public static void main(String[] args) {
    File fileIn = new File(args[0]);
    File fileOut = new File("result.asm");
    ArrayList<File> files = new ArrayList<>();
    if (args.length != 1) {
      throw new IllegalArgumentException("there is no files to translate");
    } else if (fileIn.isFile() && !(args[0].endsWith(".vm"))) {
      throw new IllegalArgumentException("incorrect file type.");
    } else if (fileIn.isFile() && args[0].endsWith(".vm")) {
      files.add(fileIn);
    } else {
      // get Files with .vm in the directory
      files = getVMFiles(fileIn);
    }
    fileOut = new File("result.asm");

      ArrayList<Parser> parsers = new ArrayList<>();

      for (File file : files) {
        Parser parser = new Parser(file);
        parsers.add(parser);
      }

      CodeWriter codeWriter = new CodeWriter(fileOut);

      for (Parser parser : parsers) {
        while (parser.hasMoreCommands()) {
          parser.advance();
          if (parser.getCommandType().equals(C_ARITHMETIC)) {
            codeWriter.writeArithmetic(parser.arg1());
          } else if (parser.getCommandType().equals(C_PUSH) || parser.getCommandType()
              .equals(C_POP)) {
            codeWriter.writePushPop(parser.getCommandType(), parser.arg1(), parser.arg2());
          }
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

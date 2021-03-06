package virtual_machine;

import static virtual_machine.CommandType.*;

import java.io.File;
import java.util.ArrayList;

public class VMTranslator {

  public static void main(String[] args) {

    if (args.length != 1) {
      throw new IllegalArgumentException("there is no files to translate");
    }

    File fileIn = new File(args[0]);
    File fileOut;
    ArrayList<File> files = new ArrayList<>();
    if (fileIn.isFile() && !(args[0].endsWith(".vm"))) {
      throw new IllegalArgumentException("incorrect file type.");
    } else {
      if (args[0].endsWith(".vm")) {
        files.add(fileIn);
        String firstPart = args[0].substring(0, args[0].length() - 3);
        fileOut = new File(firstPart + ".asm");
      }
      else{
        files = getVMFiles(fileIn);
        fileOut = new File(fileIn + ".asm");
      }
    }

      ArrayList<Parser> parsers = new ArrayList<>();

      for (File file : files) {
        Parser parser = new Parser(file);
        parser.setFileName(file.getName());
        parsers.add(parser);
      }

      CodeWriter codeWriter = new CodeWriter(fileOut);
      codeWriter.writeInit();


    for (Parser parser : parsers) {
        codeWriter.setFileName(parser.getFileName());
        while (parser.hasMoreCommands()) {
          String currentCommand = parser.advance();
          if (parser.getCommandType().equals(C_ARITHMETIC)) {
            codeWriter.writeArithmetic(parser.arg1());
          } else if (parser.getCommandType().equals(C_PUSH) || parser.getCommandType().equals(C_POP)) {
            codeWriter.writePushPop(parser.getCommandType(), parser.arg1(), parser.arg2());
          } else if (parser.getCommandType().equals(C_LABEL)) {
            codeWriter.writeLabel(parser.arg1());
          }else if (parser.getCommandType().equals(C_GOTO)) {
            codeWriter.writeGoto(parser.arg1());
          } else if (parser.getCommandType().equals(C_IF)) {
            codeWriter.writeIf(parser.arg1());
          } else if (parser.getCommandType().equals(C_FUNCTION)) {
            codeWriter.writeFunction(parser.arg1(), parser.arg2());
          } else if (parser.getCommandType().equals(C_RETURN)) {
            codeWriter.writeReturn();
          } else if (parser.getCommandType().equals(C_CALL)) {
            codeWriter.writeCall(parser.arg1(), parser.arg2());
          }
        }
      }

      codeWriter.close();
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

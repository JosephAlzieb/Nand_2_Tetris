package virtual_machine;

import static virtual_machine.CommandType.C_ARITHMETIC;
import static virtual_machine.CommandType.C_POP;
import static virtual_machine.CommandType.C_PUSH;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class Parser {

  private static List<String> arithCommands = List.of("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not");
  private Scanner scanner;
  private String currentCommand;
  private String arg0 = null;
  private String arg1 = null;
  private String arg2 = null;

  public Parser(File file) {
    try {
      scanner = new Scanner(new FileReader(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public boolean hasMoreCommands() {
    return scanner.hasNextLine();
  }

  private boolean hasComments(String line) {
    return line.contains("//");
  }

  private String removeComments(String line) {
    String newLine = line;
    if (hasComments(line)) {
      int offSet = line.indexOf("//");
      newLine = line.substring(0, offSet).trim();
    }
    return newLine;
  }

  public void advance() {
    String nextLine = scanner.nextLine();
    while (nextLine.equals("") || hasComments(nextLine)) {
      if (hasComments(nextLine)) {
        nextLine = removeComments(nextLine);
      }
      if (nextLine.trim().equals("")) {
        nextLine = scanner.nextLine();
      }
    }
    currentCommand = nextLine;
    String[] commands = currentCommand.split(" ");
    arg0 = commands[0];
    if (commands.length > 1) {
      arg1 = commands[1];
    }
    if (commands.length > 2) {
      arg2 = commands[2];
    }
  }
}

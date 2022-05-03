package assembler;

import static assembler.InstructionsType.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

  private final ArrayList<String> instructions = new ArrayList<>();
  private int currentCommandIndex = 0;
  String currentCommand;


  public Parser(String fileName) {
    try(FileReader fileReader = new FileReader(fileName)) {

      BufferedReader reader = new BufferedReader(fileReader);
      String strLine;
      while ((strLine = reader.readLine()) != null) {
        String command = removeCommentsAndSpaces(strLine);
        if (!command.equals("")) {
          instructions.add(command);
        }
      }
      fileReader.close();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String removeCommentsAndSpaces(String strLine) {
    String strWhiteSpace = strLine;

    // remove comments
    if (strWhiteSpace.contains("//")) {
      int nOffSet = strWhiteSpace.indexOf("//");
      strWhiteSpace = strWhiteSpace.substring(0, nOffSet).trim();
    }

    // remove spaces and go to new line, skip over empty line
    if (!strWhiteSpace.equals("")) {
      strWhiteSpace = strWhiteSpace.trim().replaceAll("\\s", "");
    }
    return strWhiteSpace;
  }

  public boolean hasMoreCommands() {
    return (currentCommandIndex +1 < instructions.size()) || (currentCommandIndex == 0 && instructions.size() >= 1);
  }

  public String getNextCommand() {
    currentCommand = instructions.get(currentCommandIndex);
    currentCommandIndex ++;
    return currentCommand;
  }

  public InstructionsType commandType() {
    if (currentCommand.charAt(0) == '@') {
      return A_INSTRUCTION;
    } else if (currentCommand.charAt(0) == '(') {
      return LABEL;
    } else {
      return C_INSTRUCTION;
    }
  }

  // des = comp ; jump
  public String dest() {
    String dest;
    if (currentCommand.contains("=")) {
      int nIndex = currentCommand.indexOf("=");
      dest = currentCommand.substring(0, nIndex);
    } else {
      dest = null;
    }
    return dest;
  }

  public String comp() {
    String comp;

    if (currentCommand.contains("=") && currentCommand.contains(";")) {
      int equalsIndex = currentCommand.indexOf("=");
      int semiIndex = currentCommand.indexOf(";");
      comp = currentCommand.substring(equalsIndex + 1, semiIndex);
    } else if (currentCommand.contains("=")) {
      int equalsIndex = currentCommand.indexOf("=");
      comp = currentCommand.substring(equalsIndex + 1);
    } else if (currentCommand.contains(";")) {
      int semiIndex = currentCommand.indexOf(";");
      comp = currentCommand.substring(0, semiIndex);
    } else {
      return null;
    }
    return comp;
  }
}

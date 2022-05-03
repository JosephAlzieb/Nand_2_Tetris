package assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

  private final ArrayList<String> instructions = new ArrayList<>();
  private int currentCommandIndex = 0;


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

}

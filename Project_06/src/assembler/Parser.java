package assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

  private ArrayList<String> instructions = new ArrayList<>();

  public Parser(String fileName) {
    try(FileReader fileReader = new FileReader(fileName)) {

      BufferedReader reader = new BufferedReader(fileReader);
      String strLine;
      while ((strLine = reader.readLine()) != null) {
      }
      fileReader.close();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

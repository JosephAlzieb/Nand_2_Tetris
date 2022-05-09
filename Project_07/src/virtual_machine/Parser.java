package virtual_machine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Parser {

  private Scanner scanner;

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

}

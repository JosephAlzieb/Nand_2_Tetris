package assembler;

import static assembler.InstructionsType.LABEL;

public class Main {

  public static void main(String[] args) {

    String fileName = "";
    Parser parser = new Parser(fileName);

    SymbolTable symbolTable = new SymbolTable();

    while (parser.hasMoreCommands()) {
      String currentCommand = parser.getNextCommand();
      if (parser.commandType().equals(LABEL)) {
        int length = currentCommand.length();
        int line = parser.getCurrentCommandIndex();
        symbolTable.addSymbolToAddress(parser.symbol().substring(1, length - 1), line);
        parser.removeInstruction(line);
      }
    }
  }
}

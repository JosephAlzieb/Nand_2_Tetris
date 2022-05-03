package assembler;

import static assembler.InstructionsType.A_INSTRUCTION;
import static assembler.InstructionsType.C_INSTRUCTION;
import static assembler.InstructionsType.LABEL;

import java.io.FileWriter;
import java.io.IOException;

public class Main {

  public static void main(String[] args) {

    String fileName = "this come from args ... later";
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

    parser.reset();

    // example: Add.asm ==> Add
    String fileNameSplit = fileName.substring(0, fileName.length() - 4);
    try (FileWriter fileWriter = new FileWriter(fileNameSplit + ".hack")) {
      while (parser.hasMoreCommands()) {
        String currentCommand = parser.getNextCommand();
        StringBuilder commands = new StringBuilder();
        String binary;
        if (parser.commandType().equals(A_INSTRUCTION)
            && ((Character.isLetter(parser.symbol().charAt(0)))
            && symbolTable.contains(parser.symbol()))) {

          int address = symbolTable.getAddress(parser.symbol());
          binary = Integer.toBinaryString(address);
          commands.append(binary);
        } else if (parser.commandType().equals(A_INSTRUCTION)
            && Character.isLetter(parser.symbol().charAt(0))
            && !symbolTable.contains(parser.symbol())) {

          symbolTable.addSymbol(parser.symbol());
          int address = symbolTable.getAddress(parser.symbol());
          binary = Integer.toBinaryString(address);
          commands.append(binary);
        } else if (parser.commandType().equals(C_INSTRUCTION)) {
          String strComp = parser.comp();
          String strDest = parser.dest();
          String strJump = parser.jump();
          commands.append("111");
//          sbCommand.append(Code.comp(strComp));
//          sbCommand.append(Code.dest(strDest));
//          sbCommand.append(Code.jump(strJump));
        } else {
          commands.append("error");
        }
        fileWriter.write(commands + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

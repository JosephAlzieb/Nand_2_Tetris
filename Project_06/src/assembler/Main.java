package assembler;

import static assembler.InstructionsType.A_INSTRUCTION;
import static assembler.InstructionsType.C_INSTRUCTION;
import static assembler.InstructionsType.LABEL;

import java.io.FileWriter;
import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    String fileName = "C:\\Users\\Joseph\\Documents\\Von NAND zu Tetris 2022\\nand2tetris\\nand2tetris\\projects\\06\\max\\Max.asm";
//    if (args.length == 0) {
//      return;
//    } else {
//      fileName = args[args.length - 1];
//    }

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

    System.out.println("=============================");
    System.out.println("SymbolTable");
    symbolTable.printTable();
    System.out.println("=============================");

    System.out.println("=============================");
    System.out.println("translation from Asm to Bin");
    System.out.println("=============================");
    // example: Add.asm ==> Add
    String fileNameSplit = fileName.substring(0, fileName.length() - 4);
    try (FileWriter fileWriter = new FileWriter("max.hack")) {
      while (parser.hasMoreCommands()) {
        String currentCommand = parser.getNextCommand();
        StringBuilder commands = new StringBuilder();
        String binary;
        String symbol = parser.symbol();
        if (parser.commandType().equals(A_INSTRUCTION)
            && ((Character.isLetter(symbol.charAt(0)))
            && symbolTable.contains(symbol))) {
          int address = symbolTable.getAddress(symbol);
          binary = Integer.toBinaryString(address);
          commands.append(binary);
        } else if (parser.commandType().equals(A_INSTRUCTION)
            && Character.isLetter(symbol.charAt(0))
            && !symbolTable.contains(symbol)) {

          symbolTable.addSymbol(symbol);
          int address = symbolTable.getAddress(symbol);
          binary = Integer.toBinaryString(address);
          commands.append(binary);
        } else if (parser.commandType().equals(A_INSTRUCTION)) {
          binary = Integer.toBinaryString(Integer.parseInt(symbol));
          commands.append(binary);
        } else if (parser.commandType().equals(LABEL)){
          int address = symbolTable.contains(symbol) ? symbolTable.getAddress(symbol):4444;
          binary = Integer.toBinaryString(address);
          commands.append(binary);
        } else if (parser.commandType().equals(C_INSTRUCTION)) {
          String strComp = parser.comp();
          String strDest = parser.dest();
          String strJump = parser.jump();
          commands.append("111");
          commands.append(Code.comp(strComp));
          commands.append(Code.dest(strDest));
          commands.append(Code.jump(strJump));
        } else {
          commands.append("Error");
        }
        System.out.println(commands);
        fileWriter.write(commands + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

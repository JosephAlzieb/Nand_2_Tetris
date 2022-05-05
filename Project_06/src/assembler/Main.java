package assembler;

import static assembler.InstructionsType.A_INSTRUCTION;
import static assembler.InstructionsType.C_INSTRUCTION;
import static assembler.InstructionsType.LABEL;

import java.io.FileWriter;
import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    String fileName;  //"C:\\Users\\Joseph\\Documents\\Von NAND zu Tetris 2022\\nand2tetris\\nand2tetris\\projects\\06\\max\\Max.asm";
    if (args.length == 0) {
      return;
    } else {
      fileName = args[args.length - 1];
    }

    Parser parser = new Parser(fileName);

    SymbolTable symbolTable = new SymbolTable();

    scannLabels(parser, symbolTable);

    printSteps(parser, symbolTable);

    translateAsmToBin(parser, symbolTable);

  }

  private static void printSteps(Parser parser, SymbolTable symbolTable) {
    System.out.println("=============================");
    System.out.println("Asm-code after removing Labels");
    parser.printAsmProgramm();
    System.out.println("=============================");

    parser.reset();

    System.out.println("=============================");
    System.out.println("SymbolTable");
    symbolTable.printTable();
    System.out.println("=============================");

    System.out.println("=============================");
    System.out.println("translation from Asm to Bin");
    System.out.println("=============================");
    // example: Add.asm ==> Add
    // String fileNameSplit = fileName.substring(0, fileName.length() - 4);
  }


  private static void translateAsmToBin(Parser parser, SymbolTable symbolTable) {
    try (FileWriter fileWriter = new FileWriter("result.hack")) {
      while (parser.hasMoreCommands()) {
        parser.getNextCommand(); // return the current command, and goes one further.
        StringBuilder command = new StringBuilder();
        String binary;
        String symbol = parser.symbol();
        if (parser.commandType().equals(A_INSTRUCTION)
            && ((Character.isLetter(symbol.charAt(0)))
            && symbolTable.contains(symbol))) {

          int address = symbolTable.getAddress(symbol);
          binary = Integer.toBinaryString(address);
          command.append(binary);

        } else if (parser.commandType().equals(A_INSTRUCTION)
            && Character.isLetter(symbol.charAt(0))
            && !symbolTable.contains(symbol)) {

          symbolTable.addSymbol(symbol);
          int address = symbolTable.getAddress(symbol);
          binary = Integer.toBinaryString(address);
          command.append(binary);

        } else if (parser.commandType().equals(A_INSTRUCTION)) {

          binary = Integer.toBinaryString(Integer.parseInt(symbol));
          command.append(binary);

        } else if (parser.commandType().equals(C_INSTRUCTION)) {
          String strComp = parser.comp();
          String strDest = parser.dest();
          String strJump = parser.jump();
          command.append("111");
          command.append(Code.comp(strComp));
          command.append(Code.dest(strDest));
          command.append(Code.jump(strJump));
        } else {
          command.append("Error");
        }
        if (command.length() < 15){
          command = fillWithZeros(command);
        }
        System.out.println(command);
        fileWriter.write(command + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static StringBuilder fillWithZeros(StringBuilder command) {
    return new StringBuilder("0".repeat(Math.max(0, 16 - command.length())) + command);
  }

  private static void scannLabels(Parser parser, SymbolTable symbolTable) {
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

package assembler;

import java.util.HashMap;

public class SymbolTable {

  private HashMap<String, Integer> symbolTable;
  private int nextFreeAddress = 16;

  public SymbolTable() {
    symbolTable = new HashMap<>();
    symbolTable.put("SP", 0);
    symbolTable.put("LCL", 1);
    symbolTable.put("ARG", 2);
    symbolTable.put("THIS", 3);
    symbolTable.put("THAT", 4);
    for (int nC = 0; nC < 16; nC++) {
      symbolTable.put("R" + nC, nC);
    }
    symbolTable.put("SCREEN", 16384);
    symbolTable.put("KBD", 24576);

  }

  public void addSymbolToAddress(String symbol, int sympoleAddress) {
    symbolTable.put(symbol, sympoleAddress);

  }

  public void addSymbol(String symbol) {
    symbolTable.put(symbol, nextFreeAddress);
    nextFreeAddress++;
  }

  public boolean contains(String symbol) {
    return (symbolTable.containsKey(symbol));
  }

  public int getAddress(String symbol) {
    return (symbolTable.get(symbol));
  }
}

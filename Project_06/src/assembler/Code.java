package assembler;

public class Code {

  public static String dest(String strDest) {
    return switch (strDest) {
      case null -> "000";
      case "M" -> "001";
      case "D" -> "010";
      case "MD", "DM" -> "011";
      case "A" -> "100";
      case "AM", "MA" -> "101";
      case "AD", "DA" -> "110";
      case "AMD", "MAD", "ADM", "MDA", "DMA", "DAM" -> "111";
      default -> "Invalid Destination";
    };
  }

  public static String comp(String strComp) {

    return switch (strComp) {
      case "0" -> "0101010";
      case "1" -> "0111111";
      case "-1" -> "0111010";
      case "D" -> "0001100";
      case "A" -> "0110000";
      case "!D" -> "0001101";
      case "!A" -> "0110001";
      case "-D" -> "0001111";
      case "-A" -> "0110011";
      case "D+1", "1+D" -> "0011111";
      case "A+1", "1+A" -> "0110111";
      case "D-1" -> "0001110";
      case "A-1" -> "0110010";
      case "D+A", "A+D" -> "0000010";
      case "D-A" -> "0010011";
      case "A-D" -> "0000111";
      case "D&A", "A&D" -> "0000000";
      case "D|A", "A|D" -> "0010101";
      case "M" -> "1110000";
      case "!M" -> "1110001";
      case "-M" -> "1110011";
      case "M+1", "1+M" -> "1110111";
      case "M-1" -> "1110010";
      case "D+M", "M+D" -> "1000010";
      case "D-M" -> "1010011";
      case "M-D" -> "1000111";
      case "D&M", "M&D" -> "1000000";
      case "D|M", "M|D" -> "1010101";
      default -> "Invalid Competition";
    };
  }

  public static String jump(String strJump) {

    return switch (strJump) {
      case null -> "000";
      case "JGT" -> "001";
      case "JEQ" -> "010";
      case "JGE" -> "011";
      case "JLT" -> "100";
      case "JNE" -> "101";
      case "JLE" -> "110";
      case "JMP" -> "111";
      default -> "Invalid Jump";
    };
  }

}

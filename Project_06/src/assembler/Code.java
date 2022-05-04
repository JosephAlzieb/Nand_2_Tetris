package assembler;

public class Code {

  public static String dest(String strDest) {
    return switch (strDest) {
      case "null" -> "000";
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

}

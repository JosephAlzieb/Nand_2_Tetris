package virtual_machine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

  private FileWriter fw;

  public CodeWriter(File fileOut) {
    try {
      fw = new FileWriter(fileOut);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeArithmetic(String command) {
    String code = null;
    switch (command) {
      case "add" -> code = getArithFormat1() + "M=M+D\n";
      case "sub" -> code = getArithFormat1() + "M=M-D\n";
      case "neg" -> code = """
          D=0
          @SP
          A=M-1
          M=D-M
          """;
      case "eq" -> {
        code = getArithFormat1() + getArithFormat2("JNE");
        jumpNumber++;
      }
      case "gt" -> {
        code = getArithFormat1() + getArithFormat2("JLE");
        jumpNumber++;
      }
      case "lt" -> {
        code = getArithFormat1() + getArithFormat2("JGE");
        jumpNumber++;
      }
      case "and" -> code = getArithFormat1() + "M=M&D\n";
      case "or" -> code = getArithFormat1() + "M=M|D\n";
      case "not" -> code = """
          @SP
          A=M-1
          M=!M
          """;
    }
    try {
        System.out.println(code);
      if (code != null) {
        fw.write(code);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void writePushPop(CommandType commandType, String arg1, Integer arg2) {

  private String translatePushCommand(String arg1, Integer arg2) {
    String code = null;
    if (arg1.equals("static")) {
      code = getPushFormat2(String.valueOf(arg2 + 16));
    }else if (arg1.equals("this")) {
      code = getPushFormat1("THIS", arg2);
    }else if (arg1.equals("local")) {
      code = getPushFormat1("LCL", arg2);

    } else if (arg1.equals("argument")) {
      code = getPushFormat1("ARG", arg2);
    } else if (arg1.equals("that")) {
      code = getPushFormat1("THAT", arg2);
    }else if (arg1.equals("constant")) {
//      code = getConstantFormat(arg2);
    }else if (arg1.equals("pointer") && arg2 == 0) {
      code = getPushFormat2("THIS");
    } else if (arg1.equals("pointer") && arg2 == 1) {
      code = getPushFormat2("THAT");
    } else if (arg1.equals("temp")) {
      code = getPushFormat1("R5", arg2 + 5);
    }
    return code;
  }


  private String getArithFormat2(String jump) {
    return "D=M-D\n"
        + "@FALSE" + jumpNumber + "\n"
        + "D;" + jump
        + "\n@SP\n"
        + "A=M-1\n"
        + "M=-1\n"
        + "@CONTINUE" + jumpNumber
        + "\n0;JMP\n"
        + "(FALSE" + jumpNumber + ")\n"
        + "@SP\n"
        + "A=M-1\n"
        + "M=0\n"
        + "(CONTINUE" + jumpNumber + ")\n";
    }

    private String getArithFormat1() {
    return """
        @SP
        AM=M-1
        D=M
        A=A-1
        """;
  }

 }

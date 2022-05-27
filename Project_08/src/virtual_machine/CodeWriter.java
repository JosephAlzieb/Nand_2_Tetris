package virtual_machine;

import static virtual_machine.CommandType.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

  private BufferedWriter fw;

  private int jumpNumber = 0;
  private static int labelNum = 0;
  private String fileName = "";

  public CodeWriter(File fileOut) {
    try {
      fw = new BufferedWriter(new FileWriter(fileOut));
      setFileName(fileOut.getName());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
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
    String code = null;
    if (commandType.equals(C_PUSH)) {
      code = translatePushCommand(arg1, arg2);
    }
    else if(commandType.equals(C_POP)){
      code = translatePopCommand(arg1, arg2);
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

  private String translatePopCommand(String arg1, Integer arg2) {
    String code = null;
    if (arg1.equals("static")) {
      code = getPopFormat2(String.valueOf(arg2 + 16));
    }else if (arg1.equals("this")) {
      code = getPopFormat1("THIS", arg2);
    }else if (arg1.equals("local")) {
      code = getPopFormat1("LCL", arg2);

    } else if (arg1.equals("argument")) {
      code = getPopFormat1("ARG", arg2);
    } else if (arg1.equals("that")) {
      code = getPopFormat1("THAT", arg2);
    }else if (arg1.equals("pointer") && arg2 == 0) {
      code = getPopFormat2("THIS");
    } else if (arg1.equals("pointer") && arg2 == 1) {
      code = getPopFormat2("THAT");
    } else if (arg1.equals("temp")) {
      code = getPopFormat1("R5", arg2 + 5);
    }
    return code;
  }

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
      code = getConstantFormat(arg2);
    }else if (arg1.equals("pointer") && arg2 == 0) {
      code = getPushFormat2("THIS");
    } else if (arg1.equals("pointer") && arg2 == 1) {
      code = getPushFormat2("THAT");
    } else if (arg1.equals("temp")) {
      code = getPushFormat1("R5", arg2 + 5);
    }
    return code;
  }

  private String getConstantFormat(Integer constant) {
    return "@" + constant + "\n" + "D=A\n"
        + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n"
        + "M=M+1\n";
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

  private String getPushFormat1(String arg1, Integer arg2) {
    return "@" + arg1
        + "\nD=M"
        + "\n@" + arg2
        + "\nA=D+A\n"
        + "D=M\n"
        + "@SP\n"
        + "A=M\n"
        + "M=D\n"
        + "@SP\n"
        + "M=M+1\n";
  }

  private String getPopFormat1(String arg1, Integer arg2) {
    return "@" + arg1
        + "\nD=M"
        +"\n@" + arg2
        +"\nD=D+A\n"
        + "@R13\n"
        + "M=D\n"
        + "@SP\n"
        + "AM=M-1\n"
        + "D=M\n"
        + "@R13\n"
        + "A=M\n"
        + "M=D\n";
  }

  private String getPushFormat2(String index) {
    return "@" + index
        + "\nD=M\n"
        + "@SP\n"
        + "A=M\n"
        + "M=D\n"
        + "@SP\n"
        + "M=M+1\n";
  }

  private String getPopFormat2(String index) {
    return "@" + index
        + "\nD=A\n"
        + "@R13\n"
        + "M=D\n"
        + "@SP\n"
        + "AM=M-1\n"
        + "D=M\n"
        + "@R13\n"
        + "A=M\n"
        + "M=D\n";
  }

  public void writeInit() {
    String code = """
          @256
          D=A
          @SP
          M=D
          """;
    try {
      fw.write(code);
      System.out.println(code);
// initialize the stack pointer to 0x0100
      writeCall("Sys.init", 0);  // call the function that calls Main.main
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  public void writeLabel(String label) {
    String code = "(" + label + ")\n";
    try {
      fw.write(code);
      System.out.println(code);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeGoto(String label) {
    String code = "@" + label + "\n0;JMP\n";
    try {
      fw.write(code);
      System.out.println(code);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeIf(String label) {
    String code = getArithFormat1() + "@" + label + "\nD;JNE\n";
    try {
      fw.write(code);
      System.out.println(code);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * to implement the command function g nVars,
   * we effect the following logic:
   * g:
   * repeat nVars times:
   * push 0
   *
   * @param functionName
   * @param numberOFParameter
   */
  public void writeFunction(String functionName, Integer numberOFParameter) {
    String code = "(" + functionName + ")\n";
    try {
      fw.write(code);
      System.out.println(code);
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int i = 0; i < numberOFParameter; i++) {
      writePushPop(C_PUSH, "constant", 0);
    }
  }

  /**
   * what the method does is the following:
   *
   * frame = LCL              // frame is a temp. variable
   * retAddr = *(frame-5)     // retAddr is a temp. variable
   * *ARG = pop               // repositions the return value
   * // for the caller
   * SP=ARG+1                 // restores the caller’s SP
   * THAT = *(frame-1)        // restores the caller’s THAT
   * THIS = *(frame-2)        // restores the caller’s THIS
   * ARG = *(frame-3)         // restores the caller’s ARG
   * LCL = *(frame-4)         // restores the caller’s LCL
   * goto retAddr             // goto returnAddre
   */
  public void writeReturn() {
    String code = "@LCL\n" + "D=M\n" + "@FRAME\n" + "M=D\n" + "@5\n" + "A=D-A\n" + "D=M\n" + "@RET\n"
        + "M=D\n" + getPopFormat1("ARG", 0) + "@ARG\n" + "D=M\n" + "@SP\n" + "M=D+1\n"
        + "@FRAME\n" + "D=M-1\n" + "AM=D\n" + "D=M\n" + "@THAT\n" + "M=D\n" + "@FRAME\n"
        + "D=M-1\n" + "AM=D\n" + "D=M\n" + "@THIS\n" + "M=D\n" + "@FRAME\n" + "D=M-1\n" + "AM=D\n"
        + "D=M\n" + "@ARG\n" + "M=D\n" + "@FRAME\n" + "D=M-1\n" + "AM=D\n" + "D=M\n" + "@LCL\n"
        + "M=D\n" + "@RET\n" + "A=M\n" + "0;JMP\n";
    try {
      fw.write(code);
      System.out.println(code);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * what the method does is the following:
   *
   * push returnAddress     // saves the return address
   * push LCL               // saves the LCL of f
   * push ARG               // saves the ARG of f
   * push THIS              // saves the THIS of f
   * push THAT              // saves the THAT of f
   * ARG = SP-nArgs-5       // repositions SP for g
   * LCL = SP               // repositions LCL for g
   * goto g                 // transfers control to g
   * returnAddress:         // the generated symbol
   * @param functionName
   * @param numberOFParameter
   */
  public void writeCall(String functionName, Integer numberOFParameter) {
    String label = "RETURN_LABEL" + labelNum;
    labelNum++;
    String code = "@" + label + "\n" + "D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" + getPushFormat2("LCL")
        + getPushFormat2("ARG") + getPushFormat2("THIS") + getPushFormat2("THAT") + "@SP\n"
        + "D=M\n" + "@5\n" + "D=D-A\n" + "@" + numberOFParameter + "\n" + "D=D-A\n" + "@ARG\n" + "M=D\n"
        + "@SP\n" + "D=M\n" + "@LCL\n" + "M=D\n" + "@" + functionName + "\n0;JMP\n(" + label + ")\n";
    try {
      fw.write(code);
      System.out.println(code);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
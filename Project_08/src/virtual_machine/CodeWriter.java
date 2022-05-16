package virtual_machine;

import static virtual_machine.CommandType.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

  private BufferedWriter fw;
  private int jumpNumber = 0;


  public CodeWriter(File fileOut) {
    try {
      fw = new BufferedWriter(new FileWriter(fileOut));
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

  public void writeLabel(String label) {
    try {
      fw.write("(" + label + ")\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeGoto(String label) {
    try {
      fw.write("@" + label + "\n0;JMP\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeIf(String arg1) {

  }

  public void writeFunction(String arg1, Integer arg2) {
  }

  public void writeReturn() {
  }

  public void writeCall(String arg1, Integer arg2) {

  }

  public void close() {
    try {
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}

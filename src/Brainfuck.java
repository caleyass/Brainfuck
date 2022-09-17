import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Brainfuck language interpreter
 * used memory
 * short[] arr = chars[30000]
 * used operators
 * >    shift pointer right     pointer++
 * <    shift pointer left      pointer--
 * +    increment pointer       arr[pointer]++
 * -    decrement pointer       arr[pointer]--
 * [    begin loop              while (arr[pointer]) {
 * ]    end loop                }
 * .    print pointer           sout arr[pointer]
 * ,    enter from input string arr[pointer] = getchar
 */
public class Brainfuck {
    private static final int STACK_LENGTH = 30000;
    private static final char[] arr = new char[STACK_LENGTH];
    //private static final String strCommand = "+++++++[>++++++++++<-]>.";
    //private static final String strCommand = "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.";
    //private static final String strCommand = "++++[>+++[>+++<-]<-]>>.";
    private static final String strCommand = ",.";

    public static void main(String[] args) {
        System.out.println(interpret(strCommand));
    }

    private static String interpret(String strCommand) {
        StringBuilder resStr = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //arr of chars of an input string
        final char[] cmd_stack = strCommand.toCharArray();
        //iterate through input commands
        int cmd_pointer = 0;    //command pointer
        //tracking arr of output chars
        int pointer = 0;        //memory pointer

        int c = 0;

        while (cmd_pointer < cmd_stack.length){
            switch (cmd_stack[cmd_pointer]) {
                //move pointer to right
                case '>' -> pointer++;
                //move pointer to left
                case '<' -> pointer--;
                //increase the pointer
                case '+' -> {
                    if(arr[pointer] + 1 > 255)
                        arr[pointer] = 0;
                    else arr[pointer]++;
                }
                //decrease the pointer
                case '-' -> {
                    if(arr[pointer] - 1 < 0)
                        arr[pointer] = 255;
                    else arr[pointer]--;
                }
                //add the pointer to result string
                case '.' -> resStr.append(arr[pointer]);
                //input the char
                case ',' -> {
                    try {
                        arr[pointer] = (char) Integer.parseInt(br.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // [ jumps past the matching ] if the cell under the pointer is 0
                case '[' -> {
                    //if pointer = 0 then skip a [] loop
                    if(arr[pointer] == 0){
                        cmd_pointer++;
                        while (c>0 || cmd_stack[cmd_pointer] != ']'){
                            if(cmd_stack[cmd_pointer] == '[')
                                c++;
                            else if(cmd_stack[cmd_pointer] == ']')
                                c--;
                            cmd_pointer++;
                        }
                    }
                }
                // ] jumps back to the matching [ if the cell under the pointer is nonzero
                case ']' -> {
                    //if pointer != 0 then repeat [] loop
                    if(arr[pointer] != 0){
                        cmd_pointer--;
                        while (c>0 || cmd_stack[cmd_pointer] != '['){
                            if(cmd_stack[cmd_pointer] == '[')
                                c--;
                            else if(cmd_stack[cmd_pointer] == ']')
                                c++;
                            cmd_pointer--;
                        }
                    }
                }
            }
            cmd_pointer++;
        }
        return resStr.toString();
    }
}

package StackMachine;

import Parser.Parser;
import Lexer.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class StackMachine {

    private Stack<String> stack = new Stack<>();


    public Map performance(Parser parser) {
        
        Map<String, Integer> tableOfVar = new HashMap<>();
        int a, b;   // operands

        for (int i = 0; i <= parser.tokens_array.size() - 1; i++) {

            switch (parser.tokens_array.get(i)) {
                case "=":
                    a = getNum(tableOfVar);
                    tableOfVar.put(stack.pop(), a);
                    break;
                case "+":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(a + b));
                    break;
                case "-":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(b - a));
                    break;
                case "/":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(b / a));
                    break;
                case "%":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(b % a));
                    break;
                case "^":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    int result = (int) Math.pow(a, b);
                    stack.push(String.valueOf(result));
                    break;
                case "*":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(a * b));
                    break;
                case "!=":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(a != b));
                    break;
                case "==":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(a == b));
                    break;
                case "<":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(b < a));
                    break;
                case ">":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(b > a));
                    break;
                case "<=":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(b <= a));
                    break;
                case ">=":
                    a = getNum(tableOfVar);
                    b = getNum(tableOfVar);
                    stack.push(String.valueOf(b >= a));
                    break;
                case "!F":
                    a = getNum(tableOfVar);
                    boolean c = stack.pop().equals("true");
                    i = c?i:a;
                    break;
                case "!":
                    i = getNum(tableOfVar) - 1;
                    break;
                default:
                    stack.push(String.valueOf(parser.tokens_array.get(i)));
                    break;
            }

        }

        return tableOfVar;
    }



    private int getNum(Map<String, Integer> table) {

        String peek = stack.peek();
        Token.Type type;

        if ((peek.charAt(0) >= '0') && (peek.charAt(0) <= '9'))
            type = Token.Type.INT;
        else
            type = Token.Type.VAR;

        switch (type) {
            case VAR:
                return table.get(stack.pop());
            case INT:
                return Integer.valueOf(stack.pop());
            default:
                System.err.println();
                System.exit(10);
        }

        return -1;
    }

}

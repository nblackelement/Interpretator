package Parser;

import Lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class Parser {

    private Map<String, Integer> tableOfVariables = new HashMap<>();
    public List<String> tokens_array = new ArrayList<>();
    private Stack<String> stack = new Stack<>();
    private List<Token> tokens = new ArrayList<>();
    private int position = 0;
    private int p1;
    private int p2;
    private int p3;

    public void lang(List<Token> tokens) {

        this.tokens.addAll(tokens);

        while (this.tokens.size() != position) {
            if (!expr()) {
                System.err.println(" Error: Syntax mistake ");
                System.exit(4);
            }
        }
        System.out.println(tokens_array);
    }

    private boolean expr() {
        boolean expr = false;

        if (init() || assign() || for_loop() || if_con() || while_loop()){
            expr = true;
        }
        return expr;
    }

    private boolean init() {
        boolean init = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Token.Type.INT) {
            if (assign_op()) {
                if (getCurrentTokenLexemeInc() == Token.Type.SEM) {
                    init = true;
                }
            }
        }
        position = init ? position : old_position;
        return init;
    }

    private boolean assign() {
        boolean assign = false;
        int old_position = position;

        if (assign_op()) {
            if (getCurrentTokenLexemeInc() == Token.Type.SEM) {
                assign = true;
            }
        }
        position = assign ? position : old_position;
        return assign;
    }

    private boolean if_con(){
        boolean if_loop = false;
        int old_position = position;
        if(getCurrentTokenLexemeInc() == Token.Type.IF_W){
            if(if_expr()){
                if(body()){
                    if_loop = true;
                    tokens_array.set(p3 ,String.valueOf(tokens_array.size() - 1 ));
                }
            }
        }
        position = if_loop ? position : old_position;
        return if_loop;
    }


    private boolean if_expr(){
        int old_position = position;
        boolean if_expr = false;
        if(getCurrentTokenLexemeInc() == Token.Type.LRB){
            if(log_expr()){
                if(getCurrentTokenLexemeInc() == Token.Type.RRB){
                    if_expr = true;
                }
            }
        }

        position = if_expr ? position : old_position;
        return if_expr;
    }

    private boolean while_loop(){
        boolean while_loop = false;
        int old_position = position;
        if (getCurrentTokenLexemeInc() == Token.Type.WHILE_W) {
            int p4 = tokens_array.size();
            if (while_expr()) {
                if (body()) {
                    while_loop = true;
                    tokens_array.set(p3 ,String.valueOf(tokens_array.size()+1));
                    tokens_array.add(String.valueOf(p4));
                    tokens_array.add("!");
                }
            }
        }
        position = while_loop ? position : old_position;
        return while_loop;
    }


    private boolean for_loop() {
        boolean for_loop = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Token.Type.FOR_W) {
            if (for_expr()) {
                if (body()) {
                    for_loop = true;
                    tokens_array.set(p1 ,String.valueOf(tokens_array.size()+1));
                    tokens_array.add(String.valueOf(p2));
                    tokens_array.add("!");
                }
            }
        }
        position = for_loop ? position : old_position;
        return for_loop;
    }

    private boolean while_expr() {
        boolean while_expr = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Token.Type.LRB) {
            if (log_expr()) {
                if (getCurrentTokenLexemeInc() == Token.Type.RRB) {
                    while_expr = true;
                }
            }

        }
        position = while_expr ? position : old_position;
        return while_expr;
    }


    private boolean body() {
        boolean for_body = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Token.Type.LFB) {
            while (figure_br()) {
            }
            if (getCurrentTokenLexemeInc() == Token.Type.RFB) {
                for_body = true;
            }
        }
        position = for_body ? position : old_position;
        return for_body;
    }

    private boolean for_expr() {
        boolean for_expr = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Token.Type.LRB) {
            if (start_expr()) {
                if (for_log_expr()) {
                    if (assign_op()) {
                        if (getCurrentTokenLexemeInc() == Token.Type.RRB) {
                            for_expr = true;
                        }
                    }
                }
            }
        }
        position = for_expr ? position : old_position;
        return for_expr;
    }

    private boolean start_expr() {
        boolean start_expr = false;

        if (init() || assign()) {
            start_expr = true;
        }
        return start_expr;
    }

    private boolean for_log_expr() {
        boolean log_expr = false;
        int old_position = position;

        p2 = tokens_array.size();
        if (assign_op() || value()) {
            if (getCurrentTokenLexemeInc() == Token.Type.LOG_OP) {
                String log_op = getLastTokenValue();
                if (assign_op() || value()) {
                    if (getCurrentTokenLexemeInc() == Token.Type.SEM) {
                        log_expr = true;
                        tokens_array.add(log_op);
                        p1 = tokens_array.size();
                        tokens_array.add("p1");
                        tokens_array.add("!F");
                    }
                }
            }
        }
        position = log_expr ? position : old_position;
        return log_expr;
    }

    private boolean log_expr() {
        boolean if_log_expr = false;
        int old_position = position;
        if (assign_op() || value()) {
            if (getCurrentTokenLexemeInc() == Token.Type.LOG_OP) {
                String if_log_op = getLastTokenValue();
                if (assign_op() || value()) {
                    if_log_expr = true;
                    tokens_array.add(if_log_op);
                    p3 = tokens_array.size();
                    tokens_array.add("p3");
                    tokens_array.add("!F");
                }
            }
        }
        position = if_log_expr ? position : old_position;
        return if_log_expr;
    }


    private boolean assign_op() {
        boolean assign_op = false;
        int old_position = position;
        boolean add = false;
        String var = null;

        if (getCurrentTokenLexemeInc() == Token.Type.VAR) {

            var = getLastTokenValue();
            add = tokens_array.add(var);

            if (getCurrentTokenLexemeInc() == Token.Type.ASSIGN_OP) {
                stack.push(getLastTokenValue());
                if (value()) {
                    assign_op = true;
                    tableOfVariables.put(var, 0);
                }
            }
        }
        if (add && !assign_op) {
            tokens_array.remove(tokens_array.size()-1);
        }
        if (assign_op) {
            while (!stack.empty()) {
                tokens_array.add(stack.pop());
            }
        }
        position = assign_op ? position : old_position;
        return assign_op;
    }

    private boolean value() {
        boolean value = false;

        if (val()) {
            while (OPval()) {
            }
            value = true;
        }
        return value;
    }

    private boolean OPval() {
        boolean OPval = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Token.Type.OP) {
            String arthOp = getLastTokenValue();
            while (getPriority(arthOp) <= getPriority(stack.peek())) {
                tokens_array.add(stack.pop());
            }
            stack.push(arthOp);
            if (val()) {
                OPval = true;
            }
        }
        position = OPval ? position : old_position;
        return OPval;
    }

    private boolean val() {
        boolean val = false;

        if (getCurrentTokenLexemeInc() == Token.Type.VAR) {
            tokens_array.add(getLastTokenValue());
            if (!tableOfVariables.containsKey(getLastTokenValue())) {
                System.err.println("Error: Variety " + getLastTokenValue() + " not initialize");
                System.exit(6);
            }
            return true;
        } else {
            position--;
        }
        if (getCurrentTokenLexemeInc() == Token.Type.NUMBER) {
            tokens_array.add(getLastTokenValue());
            return true;
        } else {
            position--;
        }
        if (break_value()) {
            return true;
        }
        return val;
    }

    private boolean break_value() {
        boolean break_value = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Token.Type.LRB) {
            stack.push(getLastTokenValue());
            if (value()) {
                if (getCurrentTokenLexemeInc() == Token.Type.RRB) {
                    while (!stack.peek().equals("(")) {
                        tokens_array.add(stack.pop());
                    }
                    stack.pop();

                    break_value = true;
                }
            }
        }
        position = break_value ? position : old_position;
        return break_value;
    }



    private boolean figure_br() {
        boolean figure_br = false;

        if (init() || assign()  ) {
            figure_br = true;
        }
        return figure_br;
    }






    private Token.Type getCurrentTokenLexemeInc() {
        try {
            return tokens.get(position++).getKey();
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("Error: Lexeme \"" + Token.Type.INT + "\" expected");
            System.exit(3);
        }
        return null;
    }



    private String getLastTokenValue() {
        return tokens.get(position-1).getValue();
    }

    private int getPriority(String str) {
        switch (str) {
            case "+":
                return 1;
            case "*":
                return 2;
            case "^":
                return 2;
            case "-":
                return 1;
            case "/":
                return 2;
            case "%":
                return 2;
            case "=":
                return 0;
            case "(":
                return 0;
            default:
                System.err.println("Error: In symbol " + str);
                System.exit(5);
                return 0;
        }
    }
}

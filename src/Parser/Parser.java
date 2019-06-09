package Parser;

import Lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class Parser {

    private List<Token> tokenList = new ArrayList<>();
    private Stack<String> stack = new Stack<>();
    private int position = 0;
    private int p1, p2, p3;

    private Map<String, Integer> varTable = new HashMap<>();
    public List<String> tokens = new ArrayList<>();


    public void start(List<Token> tokenList) {

        this.tokenList = tokenList;

        while (tokenList.size() != position) {

            if (!expr()) {
                System.err.println("Syntax mistake.");
                System.exit(2);
            }
        }
    }



    private boolean expr() {

        return init() || assign() || for_loop() || if_loop() || while_loop();
    }


    private boolean init() {

        boolean init = false;
        int oldPosition = position;

        if (getCurrentTokenLexemeInc() == Token.Type.INT) {
            if (assign_op())
                if (getCurrentTokenLexemeInc() == Token.Type.SEM)
                    init = true;
        }

        position = init ? position : oldPosition;
        return init;
    }

    private boolean assign() {

        boolean assign = false;
        int oldPosition = position;

        if (assign_op())
            if (getCurrentTokenLexemeInc() == Token.Type.SEM)
                assign = true;

        position = assign ? position : oldPosition;
        return assign;
    }

    private boolean if_loop() {

        boolean if_loop = false;
        int oldPosition = position;

        if(getCurrentTokenLexemeInc() == Token.Type.IF_W)
            if(if_expr() && body()) {
                if_loop = true;
                tokens.set(p3, String.valueOf(tokens.size() - 1));
            }

        position = if_loop ? position : oldPosition;
        return if_loop;
    }

    private boolean while_loop() {

        boolean while_loop = false;
        int oldPosition = position;

        if (getCurrentTokenLexemeInc() == Token.Type.WHILE_W) {

            int p4 = tokens.size();

            if (if_expr() && body()) {
                while_loop = true;
                tokens.set(p3 ,String.valueOf(tokens.size() + 1));
                tokens.add(String.valueOf(p4));
                tokens.add("!");
            }
        }

        position = while_loop ? position : oldPosition;
        return while_loop;
    }

    private boolean for_loop() {

        boolean for_loop = false;
        int oldPosition = position;

        if (getCurrentTokenLexemeInc() == Token.Type.FOR_W) {

            if (for_expr() && body()) {
                for_loop = true;
                tokens.set(p1 ,String.valueOf(tokens.size()+1));
                tokens.add(String.valueOf(p2));
                tokens.add("!");
            }
        }

        position = for_loop ? position : oldPosition;
        return for_loop;
    }


    private boolean if_expr() {

        boolean expr = false;
        int oldPosition = position;

        if(getCurrentTokenLexemeInc() == Token.Type.LRB) {
            if(log_expr()) {
                if(getCurrentTokenLexemeInc() == Token.Type.RRB)
                    expr = true;
            }
        }

        position = expr ? position : oldPosition;
        return expr;
    }

    private boolean body() {

        boolean for_body = false;
        int oldPosition = position;

        if (getCurrentTokenLexemeInc() == Token.Type.LFB) {
            while (figure_break()) {}

            if (getCurrentTokenLexemeInc() == Token.Type.RFB)
                for_body = true;
        }

        position = for_body ? position : oldPosition;
        return for_body;
    }

    private boolean for_expr() {

        boolean for_expr = false;
        int oldPosition = position;

        if (getCurrentTokenLexemeInc() == Token.Type.LRB) {

            if (start_expr() && for_log_expr() && assign_op()) {
                if (getCurrentTokenLexemeInc() == Token.Type.RRB)
                    for_expr = true;
            }
        }

        position = for_expr ? position : oldPosition;
        return for_expr;
    }

    private boolean start_expr() {

        return init() || assign();
    }

    private boolean for_log_expr() {

        boolean log_expr = false;
        int oldPosition = position;

        p2 = tokens.size();

        if (assign_op() || value()) {
            if (getCurrentTokenLexemeInc() == Token.Type.LOG_OP) {

                String log_op = getLastTokenValue();

                if (assign_op() || value()) {
                    if (getCurrentTokenLexemeInc() == Token.Type.SEM) {
                        log_expr = true;

                        tokens.add(log_op);
                        p1 = tokens.size();
                        tokens.add("p1");
                        tokens.add("!F");
                    }
                }
            }
        }

        position = log_expr ? position : oldPosition;
        return log_expr;
    }

    private boolean log_expr() {

        boolean log_expr = false;
        int oldPosition = position;

        if (assign_op() || value()) {
            if (getCurrentTokenLexemeInc() == Token.Type.LOG_OP) {

                String if_log_op = getLastTokenValue();

                if (assign_op() || value()) {
                    log_expr = true;

                    tokens.add(if_log_op);
                    p3 = tokens.size();
                    tokens.add("p3");
                    tokens.add("!F");
                }
            }
        }

        position = log_expr ? position : oldPosition;
        return log_expr;
    }

    private boolean assign_op() {

        boolean assign_op = false;
        int oldPosition = position;
        boolean add = false;
        String var;

        if (getCurrentTokenLexemeInc() == Token.Type.VAR) {

            var = getLastTokenValue();
            add = tokens.add(var);

            if (getCurrentTokenLexemeInc() == Token.Type.ASSIGN_OP) {
                stack.push(getLastTokenValue());

                if (value()) {
                    assign_op = true;
                    varTable.put(var, 0);
                }
            }
        }

        if (add && !assign_op)
            tokens.remove(tokens.size()-1);
        if (assign_op) {
            while (!stack.empty())
                tokens.add(stack.pop());
        }

        position = assign_op ? position : oldPosition;
        return assign_op;
    }

    private boolean value() {

        if (value_expr()) {
            while (value_op()) {}
            return true;
        }

        return false;
    }

    private boolean value_op() {

        boolean value_op = false;
        int oldPosition = position;

        if (getCurrentTokenLexemeInc() == Token.Type.OP) {

            String if_value_op = getLastTokenValue();

            while (getPriority(if_value_op) <= getPriority(stack.peek()))
                tokens.add(stack.pop());

            stack.push(if_value_op);

            if (value_expr())
                value_op = true;
        }

        position = value_op ? position : oldPosition;
        return value_op;
    }

    private boolean value_expr() {

        if (getCurrentTokenLexemeInc() == Token.Type.VAR) {

            tokens.add(getLastTokenValue());

            if (!varTable.containsKey(getLastTokenValue())) {
                System.err.println("Error: Variety " + getLastTokenValue() + " not initialize");
                System.exit(3);
            }

            return true;

        } else
            position--;

        if (getCurrentTokenLexemeInc() == Token.Type.NUMBER) {
            tokens.add(getLastTokenValue());
            return true;

        } else {
            position--;
        }

        return break_value();
    }


    private boolean break_value() {

        boolean break_value = false;
        int oldPosition = position;

        if (getCurrentTokenLexemeInc() == Token.Type.LRB) {

            stack.push(getLastTokenValue());

            if (value()) {
                if (getCurrentTokenLexemeInc() == Token.Type.RRB) {

                    while (!stack.peek().equals("("))
                        tokens.add(stack.pop());

                    stack.pop();
                    break_value = true;
                }
            }
        }

        position = break_value ? position : oldPosition;
        return break_value;
    }

    private boolean figure_break() {

        return init() || assign();
    }

    private Token.Type getCurrentTokenLexemeInc() {

        try {
            return tokenList.get(position++).getKey();

        } catch (IndexOutOfBoundsException ex) {

            System.err.println("Error: Lexeme \"" + Token.Type.INT + "\" expected");
            System.exit(3);
        }

        return null;
    }


    private int getPriority(String str) {

        switch (str) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "^":
            case "/":
            case "%":
                return 2;
            case "=":
            case "(":
                return 0;
            default:
                System.err.println("Error: In symbol " + str);
                System.exit(3);
                return 0;
        }
    }

    private String getLastTokenValue() {
        return tokenList.get(position - 1).getValue();
    }

}

package Lexer;

import java.util.ArrayList;
import java.util.List;


public class Lexer {

    private List<Token> tokenList;


    public Lexer(String code) {

        if(code.length() != 0)
            tokenList = createTokens(code);

        else {
            System.err.println("\nCode Missing.");
            System.exit(1);
        }
    }


    // Create list of all program tokens
    private List<Token> createTokens(String code) {

        List<Token> tokens = new ArrayList<>();

        for(int i = 0; i < code.length() ;) {

            switch (code.charAt(i)) {
                case ';':
                    tokens.add(new Token(Token.Type.SEM, ";"));
                    i++;
                    break;
                case '(':
                    tokens.add(new Token(Token.Type.LRB, "("));
                    i++;
                    break;
                case ')':
                    tokens.add(new Token(Token.Type.RRB, ")"));
                    i++;
                    break;
                case '{':
                    tokens.add(new Token(Token.Type.LFB, "{"));
                    i++;
                    break;
                case '}':
                    tokens.add(new Token(Token.Type.RFB, "}"));
                    i++;
                    break;
                case '+':
                    if (!(code.charAt(i + 1) == '+')) {
                        tokens.add(new Token(Token.Type.OP, "+"));
                        i++;
                        break;
                    } else {
                        tokens.add(new Token(Token.Type.OP, "++"));
                        i += 2;
                        break;
                    }
                case '-':
                    if (!(code.charAt(i + 1) == '-')) {
                        tokens.add(new Token(Token.Type.OP, "-"));
                        i++;
                        break;
                    } else {
                        tokens.add(new Token(Token.Type.OP, "--"));
                        i += 2;
                        break;
                    }
                case '*':
                    tokens.add(new Token(Token.Type.OP, "*"));
                    i++;
                    break;
                case '/':
                    tokens.add(new Token(Token.Type.OP, "/"));
                    i++;
                    break;
                case '=':
                    if (code.charAt(i + 1) != '='){
                        tokens.add(new Token(Token.Type.ASSIGN_OP, "="));
                        i++;
                        break;
                    }
                case '>':
                    if(code.charAt(i+1) != '='){
                        tokens.add(new Token(Token.Type.LOG_OP, ">"));
                        i++;
                        break;
                    }
                case '<':
                    if(code.charAt(i+1) != '=')
                    {
                        tokens.add(new Token(Token.Type.LOG_OP, "<"));
                        i++;
                        break;
                    }
                default:

                    if (Character.isWhitespace(code.charAt(i)))
                        i++;

                    else {

                        String substr = getSubstring(code, i);

                        if (substr != null)
                            i += substr.length();
                        else
                            break;

                        switch (substr) {
                            case "if":
                                tokens.add(new Token(Token.Type.IF_W, substr));
                                break;
                            case "else":
                                tokens.add(new Token(Token.Type.ELSE_W, substr));
                                break;
                            case "while":
                                tokens.add(new Token(Token.Type.WHILE_W, substr));
                                break;
                            case "for":
                                tokens.add(new Token(Token.Type.FOR_W, substr));
                                break;
                            case "<=":
                            case "==":
                            case ">=":
                                tokens.add(new Token(Token.Type.LOG_OP, substr));
                                break;
                            case "int":
                                tokens.add(new Token(Token.Type.INT, substr));
                                break;
                            default:
                                if (Character.isDigit(substr.charAt(0)))
                                    tokens.add(new Token(Token.Type.NUMBER, substr));
                                else
                                    tokens.add(new Token(Token.Type.VAR, substr));
                        }
                    }
            }

        }


        return tokens;
    }


    private static String getSubstring(String code, int i){


        for(int j = i; j < code.length();){
            if(Character.isLetter(code.charAt(j))){
                j++;
            }
            else{
                if(Character.isDigit(code.charAt(j))){
                    j++;
                }
                else{
                    if((code.charAt(j) == '<' || code.charAt(j) == '=' || code.charAt(j) == '>') && code.charAt(j+1) == '='){
                        return code.substring(i,j+2);
                    }
                    return code.substring(i,j);}
            }

        }
        return null;
    }



    public List<Token> getTokenList() {
        return tokenList;
    }

}

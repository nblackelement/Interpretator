import Lexer.Lexer;
import Lexer.Token;
import Parser.Parser;
import StackMachine.*;

import java.util.List;
import java.util.Map;


public class Language {

    public static void main(String[] args) {

        Language language = new Language();
        String code = "int a = 0;" +
                "for(int i = 0 ; i < 5; i = i + 1) {" +
                "a = a + 2;" +
                "}";

        language.interpret(code);

    }



    public void interpret(String code) {

        // Initialization
        StackMachine stackMachine = new StackMachine();
        Parser parser = new Parser();
        Lexer lexer = new Lexer(code);

        List<Token> tokenList = lexer.getTokenList();

        // Execution
        parser.start(tokenList);
        Map result = stackMachine.performance(parser);

        // Output
        String reverseNotation = parser.tokens.toString();
        System.out.println("Reverse Notation: " + reverseNotation);
        System.out.println("Result: " + result);
    }


}

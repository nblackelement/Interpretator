package Lexer;


public class Token {

    public enum Type {
        VAR, ASSIGN_OP, LOG_OP, NUMBER, WHILE_W, LFB, RFB, OP, LRB, RRB, SEM, FOR_W, IF_W, ELSE_W, INT
    }

    private Type key;
    private String value;


    Token(Type key, String value) {
        this.key = key;
        this.value = value;
    }



    @Override
    public String toString() {
        return "Token{" +
                "key ='" + key + '\'' +
                ", value ='" + value + '\'' +
                '}';
    }

    public Type getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(Type key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

package me.client.compiler;

/**
 * Represents the Token Class and all of its functions
 * @author Abhinav Singhal
 * @version 1.2
 */
public class Token {

    /**
     * Public definition of enum and its values
     */
    public enum Types {
        NUMBER, PLUS, MINUS, DIVIDE, MULTIPLY, EndOfLine,
        EQUALS, STRING, GREATER_THAN, GREATER_THAN_EQUALS, LESS_THAN,
        LESS_THAN_EQUALS, NOTEQUALS, LPAREN, RPAREN, LABEL, PRINT,
        IDENTIFIER, COMMA, READ, DATA, INPUT, GOSUB, RETURN, FOR, NEXT,
        TO, STEP, IF, THEN, FUNCTION
    }

    /**
     * The value stored in a token.
     */
    private final String value;

    /**
     * Type of token the token is.
     */
    private final Types type;

    /**
     * Constructs a Token Object with only a given type.
     * @param type The type for the Token
     */
    public Token(Types type){
        this.type = type;
        value = null;
    }

    /**
     * Constructs Token Object with both value and type.
     * @param type The type for the Token
     * @param value The value stored in token
     */
    public Token(Types type, String value){
        this.type = type;
        this.value = value;
    }

    /**
     * Public accessor for value
     * @return The value of the Token.
     */
    public String getValue(){
        return this.value;
    }

    /**
     * Public accessor for enum
     * @return The type of the Token
     */
    public Types getType(){
        return this.type;
    }


    /**
     * Overloads default toString method
     * @return type and value as a String
     */
    public String toString(){
        if(type == Types.MULTIPLY) return "MULTIPLY(*)";
        else if(type == Types.MINUS) return "MINUS(-)";
        else if(type == Types.DIVIDE) return "DIVIDE(/)";
        else if(type == Types.PLUS) return "PLUS(+)";
        else if(type == Types.NUMBER) return "NUMBER" + "(" + value + ")";
        else if(type == Types.EndOfLine) return "EndOfLine";
        else if(type == Types.EQUALS) return "EQUALS(=)";
        else if(type == Types.GREATER_THAN_EQUALS) return "GREATER_THAN_EQUALS(>=)";
        else if(type == Types.GREATER_THAN) return "GREATER_THAN(>)";
        else if(type == Types.LESS_THAN_EQUALS) return "LESS_THAN_EQUALS(<=)";
        else if(type == Types.LESS_THAN) return "LESS_THAN(<)";
        else if(type == Types.NOTEQUALS) return "NOTEQUALS(<>)";
        else if(type == Types.STRING) return "STRING" + "(" + value + ")";
        else if(type == Types.IDENTIFIER) return "IDENTIFIER" + "(" + value + ")";
        else if(type == Types.PRINT) return "PRINT(PRINT)";
        else if(type == Types.LABEL) return "LABEL" + "(" + value + ")";
        else if(type == Types.LPAREN) return "LPRAEN(()";
        else if(type == Types.RPAREN) return "RPRAEN())";
        else if(type == Types.COMMA) return "COMMA(,)";
        else if(type == Types.DATA) return "DATA(DATA)";
        else if(type == Types.READ) return "READ(READ)";
        else if(type == Types.INPUT) return "INPUT(INPUT)";
        else if(type == Types.GOSUB) return "GOSUB(GOSUB)";
        else if(type == Types.RETURN) return "RETURN(RETURN)";
        else if(type == Types.FOR) return "FOR(FOR)";
        else if(type == Types.NEXT) return "NEXT(NEXT)";
        else if(type == Types.TO) return "TO(TO)";
        else if(type == Types.STEP) return "STEP(STEP)";
        else if(type == Types.IF) return "IF(IF)";
        else if(type == Types.THEN) return "THEN(THEN)";
        else if(type == Types.FUNCTION) return "FUNCTION(" + value + ")";
        else return null;
    }
}

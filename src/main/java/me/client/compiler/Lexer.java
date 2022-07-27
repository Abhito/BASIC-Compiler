package me.client.compiler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Lexer class creates a Array list of tokens created from a given String.
 * @author Abhinav Singhal
 * @version 1.6
 */
public class Lexer{
    /**
     * Hashmap that stores all known words and their token types.
     */
    private final HashMap<String, Token.Types> map = new HashMap<String, Token.Types>();
    /**
     * Constructs a Lexer Object and HashMap containing known words.
     */
    public Lexer(){
        map.put("PRINT", Token.Types.PRINT);
        map.put("PRINT$", Token.Types.PRINT);
        map.put("PRINT%", Token.Types.PRINT);
        map.put("READ", Token.Types.READ);
        map.put("READ$", Token.Types.READ);
        map.put("READ%", Token.Types.READ);
        map.put("DATA", Token.Types.DATA);
        map.put("DATA$", Token.Types.DATA);
        map.put("DATA%", Token.Types.DATA);
        map.put("INPUT", Token.Types.INPUT);
        map.put("INPUT$", Token.Types.INPUT);
        map.put("INPUT%", Token.Types.INPUT);
        map.put("GOSUB", Token.Types.GOSUB);
        map.put("GOSUB$", Token.Types.GOSUB);
        map.put("GOSUB%", Token.Types.GOSUB);
        map.put("RETURN", Token.Types.RETURN);
        map.put("FOR", Token.Types.FOR);
        map.put("NEXT", Token.Types.NEXT);
        map.put("STEP", Token.Types.STEP);
        map.put("TO", Token.Types.TO);
        map.put("IF", Token.Types.IF);
        map.put("THEN", Token.Types.THEN);
        map.put("RANDOM", Token.Types.FUNCTION);
        map.put("LEFT$", Token.Types.FUNCTION);
        map.put("RIGHT$", Token.Types.FUNCTION);
        map.put("MID$", Token.Types.FUNCTION);
        map.put("NUM$", Token.Types.FUNCTION);
        map.put("VAL", Token.Types.FUNCTION);
        map.put("VAL%", Token.Types.FUNCTION);
    }
    /**
     * Creates a list of tokens from a given String
     * @param line String to perform Lexical Analysis on
     * @return List of Tokens
     */
    public ArrayList<Token> lex(String line) throws IllegalArgumentException{
        ArrayList<Token> list = new ArrayList<Token>();
        String value = "";
        Token.Types state;
        int decimal = 0; //used to make sure numbers don't have more than one decimal point
        for(int i = 0; i < line.length();){ //For loop without increment
            //if there is a quotation
            if(line.charAt(i) == '"'){
                //used to check if there is an end quote
                boolean endQuote = false;
                i++;
                //add all characters until next quote
                for(;i < line.length() - 1 && line.charAt(i) != '"';i++){
                    value = value + line.charAt(i);
                    if(line.charAt(i+1) == '"'){
                        endQuote = true;
                    }
                }
                if(!endQuote){ //if no end quote then error
                    throw new IllegalArgumentException("No end quote found.");
                }
                else {
                    list.add(new Token(Token.Types.STRING, value));
                    value = "";
                    i++;
                }
            }
            //if character is a number
            else if(Character.isDigit(line.charAt(i))) {
                state = Token.Types.NUMBER; //set state to NUMBER
                //loop until current character is not a number.
                while( i < line.length() && Character.isDigit(line.charAt(i))){
                    //create the string for the number
                    value = value + line.charAt(i);
                    //check if number is a decimal after every digit
                    if (i < line.length() - 1 && line.charAt(i + 1) == '.') {
                        value = value + '.';//add decimal to number
                        decimal++; //increment decimal
                        if(decimal > 1){ //if decimal is greater than that means current number has more than
                                         //one decimal point
                            throw new IllegalArgumentException("Bad Input: Number has too many decimal points.");
                        }
                        i++; //increment with in the loop to avoid numbers getting skipped
                    }
                    i++;
                }
                decimal = 0; //reset decimal for next number
                list.add(new Token(state,value));
                value = ""; //reset value for the next number
            }
            //if its a minus sign
            else if(line.charAt(i) == '-'){
                //if next character is a number than its a negative number
                if(i < line.length() - 1 && (Character.isDigit(line.charAt(i+1)) || line.charAt(i+1) == '.')){
                    value = value + "-";//add negative sign to number
                }
                else list.add(new Token(symbolStateMachine(line.charAt(i))));
                i++;
            }
            //if any of the following characters
            else if(line.charAt(i) == '+' ||  line.charAt(i) == '/'
                    || line.charAt(i) == '*' || line.charAt(i) == '='
                    || line.charAt(i) == '(' || line.charAt(i) == ')'){
                list.add(new Token(symbolStateMachine(line.charAt(i))));
                i++;
            }
            //if there is a decimal
            else if(line.charAt(i) == '.') {
                value = value + '.'; //add decimal to number
                decimal++;
                //if decimal has no numbers to next to it. Then throw an error.
                if (i == 0 || !Character.isDigit(line.charAt(i - 1))){
                    if(i == line.length() - 1 || !Character.isDigit(line.charAt(i + 1)))
                        throw new IllegalArgumentException("Bad Input: There is a lone decimal point.");
                }
                i++;
            }
            else if(line.charAt(i) == '<'){
                //check for following character
                if(i < line.length() - 1){
                    if(line.charAt(i+1) == '>'){
                        state = Token.Types.NOTEQUALS;
                        i++;
                    }
                    else if(line.charAt(i+1) == '='){
                        state = Token.Types.LESS_THAN_EQUALS;
                        i++;
                    }
                    else state = Token.Types.LESS_THAN;
                } //if last char then
                else state = Token.Types.LESS_THAN;
                list.add(new Token(state));
                i++;
            }
            else if(line.charAt(i) == '>'){
                //same as above
                if(i < line.length() - 1) {
                    if (line.charAt(i + 1) == '=') {
                        state = Token.Types.GREATER_THAN_EQUALS;
                        i++;
                    } else state = Token.Types.GREATER_THAN;
                } else state = Token.Types.GREATER_THAN;
                list.add(new Token(state));
                i++;
            }
            //If there is a letter
            else if(Character.isLetter(line.charAt(i))){
                //create word
                for(;i < line.length() && Character.isLetter(line.charAt(i));i++){
                    value = value + line.charAt(i);
                }
                //check if word is followed by $ or %
                if(i < line.length() && (line.charAt(i) == '$' || line.charAt(i) == '%')){
                    value = value + line.charAt(i);
                    i++;
                }
                //if word is followed by : then its a label
                if(i < line.length() && line.charAt(i) == ':') {
                    list.add(new Token(Token.Types.LABEL, value));
                    i++;
                }
                //if word is in HashMap create specific token
                //if not then default to IDENTIFIER
                else list.add(new Token(map.getOrDefault(value, Token.Types.IDENTIFIER), value));
                value = "";
            }
            //if comma is found
            else if(line.charAt(i) == ','){
                list.add(new Token(Token.Types.COMMA));
                i++;
            }
            //Ignore Spaces and Tabs
            else if(line.charAt(i) == ' ' || line.charAt(i) == '\t') i++;
            else{ //Throw error if character didn't match any state
                throw new IllegalArgumentException("Bad Input: " + line.charAt(i) + " is not accepted.");
            }
        }
        list.add(new Token(Token.Types.EndOfLine)); //add Token EndOfLine at the end
        return list;
    }

    /**
     * Emits state for given symbol
     * @param symbol A given Math operation parenthesis
     */
    public Token.Types symbolStateMachine(char symbol){
        if(symbol == '+') return  Token.Types.PLUS;
        else if(symbol == '-') return Token.Types.MINUS;
        else if(symbol == '/') return Token.Types.DIVIDE;
        else if(symbol == '=') return Token.Types.EQUALS;
        else if(symbol == '(') return Token.Types.LPAREN;
        else if(symbol == ')') return Token.Types.RPAREN;
        else return Token.Types.MULTIPLY;
    }
}
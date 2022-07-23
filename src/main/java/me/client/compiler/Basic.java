package me.client.compiler;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Accepts only one argument.
 * Traverses every line in a given file prints out executed Code.
 * @author Abhinav Singhal
 * @version 1.6
 */
public class Basic{
    public static void main(String[] args) throws Exception{
        //Throw error if one argument wasn't given
        if(args.length != 1){
            throw new IllegalArgumentException("Only one argument accepted.");
        }
        else{
            //Instantiate Lexer class
            Lexer lexer = new Lexer();
            //Import file with given path
            //Change src/ to match file path
            File file = new File(args[0]);
            //Read all lines and save them in list.
            List<String> lines = Files.readAllLines(file.toPath());
            ArrayList<Token> tokenList = new ArrayList<Token>();
            for (String line : lines) { //Use enhance for loop to traverse list
                try {
                    tokenList.addAll(lexer.lex(line)); //call lex
                } catch(Exception e){
                    System.out.println(e);
                }
            }
            /*
            //Uncomment to see output from Lexer
            for (Token token : tokenList) { //Print ArrayList of tokens
                //System.out.print(token.toString() + " ");
                //if(token.getType() == Token.Types.EndOfLine) System.out.print("\n");
            }
            System.out.print("\n");
            */
            try {
                Parser parser = new Parser(tokenList); //call parser
                Node parsed = parser.parse();
                /*
                //Uncomment to see output from parser
                System.out.println(parsed); //print Node
                 */
                //Used to test Interpreter
                Interpreter interpreter = new Interpreter((StatementsNode) parsed);
                interpreter.initialize();
            }catch(Exception e) {
                System.out.println(e);
            }
        }
    }
}

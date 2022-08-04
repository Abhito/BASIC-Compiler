package me.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import me.client.compiler.*;

import java.util.ArrayList;


public class App implements EntryPoint
{
    private final HorizontalPanel mainPanel = new HorizontalPanel();
    private final HorizontalPanel buttonPanel = new HorizontalPanel();
    private final VerticalPanel inputPanel = new VerticalPanel();
    private final VerticalPanel outputPanel = new VerticalPanel();
    public static final VerticalPanel outputCode = new VerticalPanel();
    private final Button compileButton = new Button("Compile");
    private final TextArea codeArea = new TextArea();
    @Override
    public void onModuleLoad(){
        //Set up Button Panel
        Label inputLabel = new Label();
        inputLabel.setText("Input:");
        buttonPanel.add(inputLabel);
        buttonPanel.add(compileButton);

        //Set up Input Panel
        inputPanel.add(buttonPanel);
        inputPanel.add(codeArea);

        //Set up Output Panel
        Label outputLabel = new Label();
        outputLabel.setText("Output:");
        outputPanel.add(outputLabel);
        outputPanel.add(outputCode);

        //Set up Main Panel
        mainPanel.add(inputPanel);
        mainPanel.add(outputPanel);

        //Associate the Main panel with the HTML host page.
        RootPanel.get("editor").add(mainPanel);
        //Move cursor focus to the input box.
        codeArea.setFocus(true);

        //Listen for mouse events on the Compile Button
        compileButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                sendToCompiler();
            }
        });

    }

    private void sendToCompiler(){
        outputCode.clear();
        String input = codeArea.getValue();
        ArrayList<String> lines = new ArrayList<String>();

        int loc = input.indexOf('\n');
        while(loc > 0){
            String snippet = input.substring(0, loc);
            input = input.substring(loc+1);

            lines.add(snippet);

            loc = input.indexOf('\n');
        }
        lines.add(input);

        compile(lines);
    }

    private void compile(ArrayList<String> lines){
        Lexer lexer = new Lexer();
        ArrayList<Token> tokenList = new ArrayList<Token>();
        for (String line : lines) { //Use enhance for loop to traverse list
            try {
                tokenList.addAll(lexer.lex(line)); //call lex
            } catch(Exception e){
                System.out.println(e);
            }
        }
        try {
            Parser parser = new Parser(tokenList); //call parser
            Node parsed = parser.parse();
            //Used to test Interpreter
            Interpreter interpreter = new Interpreter((StatementsNode) parsed);
            interpreter.initialize();
        }catch(Exception e) {
            System.out.println(e);
        }
    }
}

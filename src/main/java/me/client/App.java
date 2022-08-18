package me.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import me.client.compiler.*;

import java.util.ArrayList;


public class App implements EntryPoint
{
    private final HorizontalPanel buttonPanel = new HorizontalPanel();
    private final VerticalPanel inputPanel = new VerticalPanel();
    private final VerticalPanel outputPanel = new VerticalPanel();
    public static final VerticalPanel outputCode = new VerticalPanel();
    private final Button compileButton = new Button("Compile");
    private final TextArea codeArea = new TextArea();
    @Override
    public void onModuleLoad(){
        inputPanel.addStyleName("inputSide");
        outputPanel.addStyleName("outputSide");

        //Set up Button Panel
        Label inputLabel = new Label();
        inputLabel.setText("Input:");
        compileButton.addStyleDependentName("compile");
        buttonPanel.addStyleName("buttonPanel");

        buttonPanel.add(inputLabel);
        buttonPanel.add(compileButton);

        //Set up Input Panel
        inputPanel.add(buttonPanel);
        inputPanel.add(codeArea);

        //Set up Output Panel
        Label outputLabel = new Label();
        outputLabel.setText("Output:");
        outputLabel.setStyleName("outputLabel");
        outputCode.addStyleName("outputCode");
        outputPanel.add(outputLabel);
        outputPanel.add(outputCode);

        //Associate the Main panel with the HTML host page.
        RootPanel.get("editor").add(inputPanel);
        RootPanel.get("editor").add(outputPanel);
        //Move cursor focus to the input box.
        codeArea.setFocus(true);

        //Listen for mouse events on the Compile Button
        compileButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                sendToCompiler();
            }
        });

        final int defaultlines = 5;
        codeArea.setVisibleLines(defaultlines);
        codeArea.getElement().setAttribute("wrap", "off");

        codeArea.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                System.out.println("Value Changed");
            }
        });
        codeArea.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent keyPressEvent) {
                int lines = 0;
                final String s = codeArea.getText();
                for(int i = 0; i != -1; i = s.indexOf("\n", i + 1)){
                    lines++;
                }
                if(keyPressEvent.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER){
                    lines++;
                }
                codeArea.setVisibleLines(Math.max(lines, defaultlines));
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
                Label error = new Label();
                error.setText(String.valueOf(e));
                error.setStyleName("errorLabel");
                outputCode.add(error);
            }
        }
        try {
            Parser parser = new Parser(tokenList); //call parser
            Node parsed = parser.parse();
            //Used to test Interpreter
            Interpreter interpreter = new Interpreter((StatementsNode) parsed);
            interpreter.initialize();
        }catch(Exception e) {
            Label error = new Label();
            error.setText(String.valueOf(e));
            error.setStyleName("errorLabel");
            outputCode.add(error);
        }
    }
}

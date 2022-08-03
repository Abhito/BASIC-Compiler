package me.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;


public class App implements EntryPoint
{
    private HorizontalPanel mainPanel = new HorizontalPanel();
    private HorizontalPanel buttonPanel = new HorizontalPanel();
    private VerticalPanel inputPanel = new VerticalPanel();
    private VerticalPanel outputPanel = new VerticalPanel();
    private VerticalPanel outputCode = new VerticalPanel();
    private Button compileButton = new Button("Compile");
    private TextArea codeArea = new TextArea();
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

        outputCode.clear();
        for(String line: lines) {
            Label label = new Label();
            label.setText(line);
            outputCode.add(label);
        }
    }
}

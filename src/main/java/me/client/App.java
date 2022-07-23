package me.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.ui.RootPanel;


public class App implements EntryPoint
{
    @Override
    public void onModuleLoad(){
        Text textNode = Document.get().createTextNode("Hello World!");
        RootPanel.getBodyElement().appendChild(textNode);
    }
}

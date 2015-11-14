package org.concordion.markdown;

import java.util.ArrayList;
import java.util.List;

public class ConcordionCommand {
    
    public Attribute command;
    public String text;
    public List<Attribute> attributes = new ArrayList<Attribute>();

    public ConcordionCommand(String command, String expression, String text) {
        this.command = new Attribute(command, expression);
        this.text = text;
    }

    public ConcordionCommand(String command, String expression) {
        this.command = new Attribute(command, expression);
    }

    public void addAttribute(String name, String value) {
        attributes.add(new Attribute(name, value));
    }
}
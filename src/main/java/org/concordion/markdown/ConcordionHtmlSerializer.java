package org.concordion.markdown;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.Node;

public class ConcordionHtmlSerializer extends ToHtmlSerializer {
    public ConcordionHtmlSerializer(LinkRenderer linkRenderer) {
        super(linkRenderer);
    }
   
    @Override
    public void visit(Node node) {
        if (node instanceof ConcordionNode) {
            switch (((ConcordionNode)node).getType()) {
                case Set:
                    visit((ConcordionSetNode)node);
                    break;
                case AssertEquals:
                    visit((ConcordionEqualsNode)node);
                    break;
                case Execute:
                    visit((ConcordionExecuteNode)node);
                    break;
                default:
                    throw new IllegalStateException("Unrecognised node type [" + node.getClass() + "]");
            }
        } else {
            super.visit(node);
        }
    }

    private void visit(ConcordionEqualsNode node) {
        printConcordionCommand("assertEquals", node.getExpression(), node.getText());
    }

    private void visit(ConcordionSetNode node) {
        printConcordionCommand("set", "#" + node.getVarName(), node.getText());
    }

    private void visit(ConcordionExecuteNode node) {
        printConcordionCommand("execute", node.getText());
    }
        
    private void printConcordionCommand(String command, String value, String text) {
        printer.print('<').print("span");
        printAttribute("concordion:" + command, value);
        printer.print('>');
        printer.printEncoded(text);
        printer.print('<').print('/').print("span").print('>');
    }

    private void printConcordionCommand(String command, String text) {
        printer.print('<').print("span");
        printAttribute("concordion:" + command, text);
        printer.print("/>");
    }
    
    private void printAttribute(String name, String value) {
        printer.print(' ').print(name).print('=').print("'").print(value).print("'");
    }
}
package org.concordion.markdown;

import java.util.Arrays;

import org.pegdown.LinkRenderer;
import org.pegdown.Printer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.Node;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class ConcordionHtmlSerializer extends ToHtmlSerializer {
    private ConcordionSerializerPlugin concordionSerializerPlugin;

    private static final class ConcordionSerializerPlugin implements ToHtmlSerializerPlugin {
        private String action;
        private String text;
        
        @Override
        public boolean visit(Node node, Visitor visitor, Printer printer) {
            if (node instanceof ConcordionSetNode) {
                setAction("set");
                setText(((ConcordionSetNode)node).getText());
                return true;
            }
            if (node instanceof ConcordionEqualsNode) {
                setAction("assertEquals");
                setText(((ConcordionEqualsNode)node).getText());
                return true;
            }
            return false;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public ConcordionHtmlSerializer(LinkRenderer linkRenderer) {
        super(linkRenderer, Arrays.asList(new ToHtmlSerializerPlugin[] {new ConcordionSerializerPlugin()}));
        concordionSerializerPlugin = (ConcordionSerializerPlugin) plugins.get(0);
    }

    @Override
    protected void printTag(SuperNode node, String tag) {
        printer.print('<').print(tag);
        if (concordionSerializerPlugin.getText() != null) {
            printAttribute("concordion:" + concordionSerializerPlugin.getAction(), concordionSerializerPlugin.getText());
            concordionSerializerPlugin.setText(null);
        }
        printer.print('>');
        visitChildren(node);
        printer.print('<').print('/').print(tag).print('>');
    }
    
    @Override
    protected void printTag(TextNode node, String tag) {
        printer.print('<').print(tag);
        if (concordionSerializerPlugin.getText() != null) {
            printAttribute("concordion:" + concordionSerializerPlugin.getAction(), concordionSerializerPlugin.getText());
            concordionSerializerPlugin.setText(null);
        }
        printer.print('>');
        printer.printEncoded(node.getText());
        printer.print('<').print('/').print(tag).print('>');
    }
   
    @Override
    public void visit(TextNode node) {
        if (node.getText().trim().length() > 0 && concordionSerializerPlugin.getText() != null) {
            printTag(node, "span");
        } else {
            super.visit(node);
        }
    }

    private void printAttribute(String name, String value) {
        printer.print(' ').print(name).print('=').print("'").print(value).print("'");
    }
}
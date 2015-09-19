package org.concordion.markdown;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.Node;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TableCaptionNode;
import org.pegdown.ast.TableCellNode;
import org.pegdown.ast.TableColumnNode;
import org.pegdown.ast.TableNode;

public class ConcordionHtmlSerializer extends ToHtmlSerializer {
    private static class Attribute {
        public final String name;
        public final String value;
        
        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
    
    private Attribute pendingAttribute = null;
    
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
                    throw new IllegalStateException();
            }
        }
    }
    
//-----------------------------------------------------------------------------------------------------------------------
// For execute on a table, the concordion:execute command is on the TableCaptionNode and has to be moved to the TableNode    
    
    @Override
    public void visit(TableNode node) {
        for (Node child : node.getChildren()) {
            if (child instanceof TableCaptionNode) {
                for (Node captionChild : child.getChildren()) {
                    if (captionChild instanceof ConcordionExecuteNode) {
                        pendingAttribute = new Attribute("concordion:" + "execute", ((ConcordionExecuteNode) captionChild).getExpression());
                    }
                    if (captionChild instanceof ConcordionVerifyRowsNode) {
                        pendingAttribute = new Attribute("concordion:" + "verifyRows", ((ConcordionVerifyRowsNode) captionChild).getExpression());
                    }
                } 
            }
        }
        // Call the super visit(TableNode) method and override printIndentedTag() below, so that the concordion:execute attribute is added to the tag
        super.visit(node);
    };
    
    @Override
    public void visit(TableCaptionNode node) {
        if (hasNonConcordionChildren(node)) {
            printer.println().print("<caption>");
            visitNonConcordionChildren(node);
            printer.print("</caption>");
        }
    }
    
    @Override
    protected void printIndentedTag(SuperNode node, String tag) {
        printer.println().print('<').print(tag);
        if (pendingAttribute != null) {
            printAttribute(pendingAttribute.name, pendingAttribute.value);
            pendingAttribute = null;
        }
        printer.print('>').indent(+2);
        visitChildren(node);
        printer.indent(-2).println().print('<').print('/').print(tag).print('>');
    }
    
//-----------------------------------------------------------------------------------------------------------------------
// For execute on a table, the concordion command attributes have to be directly on the <th> tags rather than on child <span> tags.     
    @Override
    public void visit(TableCellNode node) {
        if (inTableHeader) {
            for (Node child : node.getChildren()) {
                if (child instanceof ConcordionEqualsNode) {
                    pendingAttribute = new Attribute("concordion:" + "assertEquals", ((ConcordionEqualsNode)child).getExpression());
                }
                if (child instanceof ConcordionSetNode) {
                    pendingAttribute = new Attribute("concordion:" + "set", ((ConcordionSetNode)child).getVarName());
                }
            }
        }
        
        // Call the super visit(TableCellNode) method and override visit(TableColumnNode) below, so that the concordion commands are added to the <th> tag
        super.visit(node);
    }

    public void visit(TableColumnNode node) {
        super.visit(node);
        if (pendingAttribute != null) {
            printAttribute(pendingAttribute.name, pendingAttribute.value);
            pendingAttribute = null;
        }
    }
//-----------------------------------------------------------------------------------------------------------------------

    
    private void visit(ConcordionEqualsNode node) {
        printConcordionCommand("assertEquals", node.getExpression(), node.getText());
    }

    private void visit(ConcordionSetNode node) {
        printConcordionCommand("set", node.getVarName(), node.getText());
    }
        
    private void visit(ConcordionExecuteNode node) {
        printConcordionCommand("execute", node.getExpression(), node.getText());
    }
        
    private void printConcordionCommand(String command, String value, String text) {
        if (!inTableHeader) {
            printer.print('<').print("span");
            printAttribute("concordion:" + command, value);
            printer.print('>');
        }
        printer.printEncoded(text);
        if (!inTableHeader) {
            printer.print('<').print('/').print("span").print('>');
        }
    }
    
    private boolean hasNonConcordionChildren(Node node) {
        boolean hasNonConcordionChildren = false;
        
        for (Node child : node.getChildren()) {
            if (!(child instanceof ConcordionNode)) {
                hasNonConcordionChildren = true;
            }
        }
        return hasNonConcordionChildren;
    }
    
    private void visitNonConcordionChildren(Node node) {
        for (Node child : node.getChildren()) {
            if (!(child instanceof ConcordionNode)) {
                child.accept(this);
            }
        }
    }
}

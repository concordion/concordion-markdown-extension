package org.concordion.markdown;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.ExpLinkNode;
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
    private String concordionNamespacePrefix;
    
    public ConcordionHtmlSerializer(String concordionNamespacePrefix) {
        super(new ConcordionLinkRenderer(concordionNamespacePrefix));
        this.concordionNamespacePrefix = concordionNamespacePrefix;
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
                case Run:
                    // handled by ConcordionLinkRenderer
                    break;
                default:
                    throw new IllegalStateException("Visiting unknown Concordion command");
            }
        }
    }
    
//-----------------------------------------------------------------------------------------------------------------------
// For execute on a table and verify rows, the command is on the TableCaptionNode and has to be moved to the TableNode    
    
    @Override
    public void visit(TableNode node) {
        for (Node child : node.getChildren()) {
            if (child instanceof TableCaptionNode) {
                for (Node captionChild : child.getChildren()) {
                    if (captionChild instanceof ConcordionExecuteNode) {
                        pendingAttribute = new Attribute(namespaced("execute"), ((ConcordionExecuteNode) captionChild).getExpression());
                    }
                    if (captionChild instanceof ConcordionVerifyRowsNode) {
                        pendingAttribute = new Attribute(namespaced("verifyRows"), ((ConcordionVerifyRowsNode) captionChild).getExpression());
                    }
                } 
            }
        }
        // Call the super visit(TableNode) method and override printIndentedTag() below, so that the concordion:execute attribute is added to the tag
        super.visit(node);
    }

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
// For execute on a table and verify rows, the command attributes have to be directly on the <th> tags rather than on child <span> tags.     
    @Override
    public void visit(TableCellNode node) {
        if (inTableHeader) {
            for (Node child : node.getChildren()) {
                if (child instanceof ConcordionEqualsNode) {
                    pendingAttribute = new Attribute(namespaced("assertEquals"), ((ConcordionEqualsNode)child).getExpression());
                }
                if (child instanceof ConcordionSetNode) {
                    pendingAttribute = new Attribute(namespaced("set"), ((ConcordionSetNode)child).getVarName());
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
    @Override
    public void visit(ExpLinkNode node) {
        if (".".equals(node.url)) {
            String text = printChildrenToString(node);
            if (node.title.startsWith("#") && !(node.title.contains("="))) {
                printConcordionCommand("set", node.title, text);
            } else if (node.title.startsWith("?=")) {
                printConcordionCommand("assertEquals", node.title.substring(2), text);
            } else {
                //TODO escape "."
                if (".".equals(text)) {
                    text = "";
                }
                printConcordionCommand("execute", node.title, text);
            }
            System.out.println("Title: " + node.title);
        } else {
            super.visit(node);
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
            printAttribute(namespaced(command), value);
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
    
    private String namespaced(String command) {
        return concordionNamespacePrefix + ":" + command;
    };

    public static class ConcordionLinkRenderer extends LinkRenderer {
        private String concordionNamespacePrefix;

        public ConcordionLinkRenderer(String concordionNamespacePrefix) {
            this.concordionNamespacePrefix = concordionNamespacePrefix;
        }

        @Override
        public Rendering render(ExpLinkNode node, String text) {
            Rendering rendering = super.render(node, text);
            
            renderConcordionChildren(node, rendering);
            return rendering;
        }
        
        private void renderConcordionChildren(Node node, Rendering rendering) {
            for (Node child : node.getChildren()) {
                if (child instanceof ConcordionRunNode) {
                    rendering.withAttribute(concordionNamespacePrefix + ":" + "run", "concordion");
                }
                renderConcordionChildren(child, rendering);
            }
        }
    }
}

package org.concordion.markdown;

import java.util.List;

import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.StrikeNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TableCellNode;
import org.pegdown.ast.TableColumnNode;
import org.pegdown.ast.TableHeaderNode;
import org.pegdown.ast.TableNode;
import org.pegdown.ast.TableRowNode;

public class ConcordionHtmlSerializer extends ToHtmlSerializer {
    private static final String URL_FOR_CONCORDION = "-";
    private static final String SOURCE_CONCORDION_NAMESPACE_PREFIX = "c";
    private final ConcordionCommandParser concordionCommandParser; 
    private final String targetConcordionNamespacePrefix;
    
    private ConcordionCommand pendingCommand = null;
    private boolean inHeaderNode;
    private boolean inExample;
    private String currentExampleHeading;
    private int currentExampleLevel;
    
    public ConcordionHtmlSerializer(String targetConcordionNamespacePrefix) {
        super(new RunCommandLinkRenderer(SOURCE_CONCORDION_NAMESPACE_PREFIX, targetConcordionNamespacePrefix));
        this.targetConcordionNamespacePrefix = targetConcordionNamespacePrefix;
        concordionCommandParser = new ConcordionCommandParser(SOURCE_CONCORDION_NAMESPACE_PREFIX, targetConcordionNamespacePrefix);
    }
   
//=======================================================================================================================
// 
    @Override
    public void visit(ExpLinkNode node) {
        if (URL_FOR_CONCORDION.equals(node.url)) {
            String text = printChildrenToString(node);
            // Some commands only require an expression and don't need a text value to be passed. However, Markdown links always require text for the URL.
            // Any URL that is written in italics will be set to an empty text value.
            if (text.startsWith("<em>")) {
                text = "";
            };
            if (inHeaderNode || inTableHeader) {
                printer.printEncoded(text);
            } else {
                ConcordionCommand command = concordionCommandParser.getCommandFor(node, text);
                printConcordionCommandElement(command);
            }
        } else {
            super.visit(node);
        }
    }
    
//=======================================================================================================================
// concordion:execute on a table and concordion:verifyRows
// The concordion:execute command is in the first (and only) cell of the first header row.
// The header row containing the command has to be removed, and the command needs to be printed on the table row.    
    
    @Override
    public void visit(TableNode tableNode) {
        if (firstChildIsInstanceOf(tableNode, TableHeaderNode.class)) {
            Node header = firstChildOf(tableNode);
            if (firstChildIsInstanceOf(header, TableRowNode.class)) {
                Node row = firstChildOf(header);
                if (hasExactlyOneChild(row) && firstChildIsInstanceOf(row, TableCellNode.class)) {
                    Node cell = firstChildOf(row);
                    if (firstChildIsInstanceOf(cell, ExpLinkNode.class)) {
                        ExpLinkNode linkNode = (ExpLinkNode) firstChildOf(cell);
                        String text = printChildrenToString(linkNode);
                        pendingCommand = concordionCommandParser.getCommandFor(linkNode, text);
                        header.getChildren().remove(row);
                    }
                }
            }
        }
        // Call the super visit(TableNode) method and override printIndentedTag() below, so that the concordion command is added to the tag.
        super.visit(tableNode);
    }

    @Override
    protected void printIndentedTag(SuperNode node, String tag) {
        printer.println().print('<').print(tag);
        printPendingConcordionCommand();
        printer.print('>').indent(+2);
        visitChildren(node);
        printer.indent(-2).println().print('<').print('/').print(tag).print('>');
    }
    
//-----------------------------------------------------------------------------------------------------------------------
// The concordion:set and concordion:assertEquals commands have to be on the TableColumnNode <th> tags, rather than child ExpLinkNodes.     
    @Override
    public void visit(TableCellNode node) {
        if (inTableHeader) {
            for (Node child : node.getChildren()) {
                if (child instanceof ExpLinkNode) {
                    pendingCommand = concordionCommandParser.getCommandFor((ExpLinkNode) child, "");
                }
            }
        }
        
        // Call the super visit(TableCellNode) method and override visit(TableColumnNode) below, so that the concordion commands are added to the <th> tag
        super.visit(node);
    }

    public void visit(TableColumnNode node) {
        super.visit(node);
        printPendingConcordionCommand();
    }

//=======================================================================================================================
// concordion:example support
    public void visit(HeaderNode node) {
        inHeaderNode = true;
        boolean printHeaderNode = true;
        for (Node child : node.getChildren()) {
            if (child instanceof ExpLinkNode) {
                if (URL_FOR_CONCORDION.equals(((ExpLinkNode) child).url)) {
                    closeExampleIfNeedeed();
                    String expression = ((ExpLinkNode)child).title;
                    currentExampleHeading = printChildrenToString(node);
                    currentExampleLevel = node.getLevel();
                    ConcordionCommand command = concordionCommandParser.splitAttributes(expression, "example", currentExampleHeading);
                    printer.println();
                    printer.print("<div");
                    printConcordionCommand(command);
                    printer.print(">");
                    inExample = true;
                }
            } if (inExample) {
                if (child instanceof StrikeNode) {
                    String exampleHeading = printChildrenToString(node).replace("<del>", "").replace("</del>", "");
                    if (currentExampleHeading != null && currentExampleHeading.equals(exampleHeading)) {
                        closeExample();
                        printHeaderNode = false;
                    }
                } else {
                    if (node.getLevel() < currentExampleLevel) {
                        closeExample();
                    }
                }
            }
        }
        if (printHeaderNode) {
            super.visit(node);
        }
        inHeaderNode = false;
    }

    public void visit(RootNode node) {
        super.visit(node);
        closeExampleIfNeedeed();
    }

    private void closeExampleIfNeedeed() {
        if (inExample) {
            closeExample();
        }
    }

    private void closeExample() {
        printer.print("</div>");
        inExample = false;
        currentExampleHeading = "";
        currentExampleLevel = 0;
    }

//=======================================================================================================================
// support methods    
    private void printConcordionCommandElement(ConcordionCommand command) {
        printer.print('<').print("span");
        printConcordionCommand(command);
        printer.print('>');
        printer.printEncoded(command.text);
        printer.print('<').print('/').print("span").print('>');
    }

    private void printPendingConcordionCommand() {
        if (pendingCommand != null) {
            printConcordionCommand(pendingCommand);
            pendingCommand = null;
        }
    }

    private void printConcordionCommand(ConcordionCommand command) {
        printAttribute(namespaced(command.command.name), command.command.value);
        
        List<Attribute> attributes = command.attributes;
        for (Attribute attribute : attributes) {
            printAttribute(attribute.name, attribute.value);
        }
    }
    
    private String namespaced(String command) {
        return targetConcordionNamespacePrefix + ":" + command;
    }

    private boolean hasExactlyOneChild(Node node) {
        return node.getChildren().size() == 1;
    }

    private Node firstChildOf(Node node) {
        return node.getChildren().get(0);
    }

    private boolean firstChildIsInstanceOf(Node node, Class<?> clazz) {
        return node.getChildren().size() > 0 && clazz.isAssignableFrom(firstChildOf(node).getClass());
    }
}
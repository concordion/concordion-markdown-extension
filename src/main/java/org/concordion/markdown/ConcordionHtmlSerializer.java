package org.concordion.markdown;

import java.util.List;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.StrikeNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TableCaptionNode;
import org.pegdown.ast.TableCellNode;
import org.pegdown.ast.TableColumnNode;
import org.pegdown.ast.TableNode;

public class ConcordionHtmlSerializer extends ToHtmlSerializer {
    public static class Attribute {
        public final String name;
        public final String value;
        
        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
    
    private ConcordionCommand pendingCommand = null;
    private String concordionNamespacePrefix;
    private boolean inHeaderNode;
    private boolean inExample;
    private String currentExampleHeading;
    
    public ConcordionHtmlSerializer(String concordionNamespacePrefix) {
        super(new ConcordionLinkRenderer(concordionNamespacePrefix));
        this.concordionNamespacePrefix = concordionNamespacePrefix;
    }
   
//-----------------------------------------------------------------------------------------------------------------------
// For execute on a table and verify rows, the command is on the TableCaptionNode and has to be moved to the TableNode    
    
    @Override
    public void visit(TableNode node) {
        for (Node child : node.getChildren()) {
            if (child instanceof TableCaptionNode) {
                for (Node captionChild : child.getChildren()) {
                      if (isConcordionCodeNode(captionChild)) {
                          pendingCommand = getConcordionCommand((CodeNode) captionChild);
                      }
                } 
            }
        }
        // Call the super visit(TableNode) method and override printIndentedTag() below, so that the concordion:execute attribute is added to the tag
        super.visit(node);
    }

    private ConcordionCommand getConcordionCommand(CodeNode node) {
        String text = node.getText();
        if (!text.startsWith("c:")) {
            throw new IllegalStateException(String.format("Expected Concordion command '%s' to start with 'c:'", text));
        }

        int commandEnd = text.indexOf(' ', 2);
        String command;
        if (commandEnd > 0) {
            command = text.substring(2, commandEnd);
        } else {
            throw new IllegalArgumentException(String.format("Expected a space character in Concordion command '%s'", text));
        }
        String value = text.substring(commandEnd+1);
        return new ConcordionCommand(command, value);
    }

    @Override
    public void visit(TableCaptionNode node) {
        if (hasNonConcordionCodeChildren(node)) {
            printer.println().print("<caption>");
            visitNonConcordionCodeChildren(node);
            printer.print("</caption>");
        }
    }
    
    @Override
    protected void printIndentedTag(SuperNode node, String tag) {
        printer.println().print('<').print(tag);
        printCommandIfPending();
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
                if (child instanceof ExpLinkNode) {
                    pendingCommand = getCommandFor((ExpLinkNode) child, "");
                }
            }
        }
        
        // Call the super visit(TableCellNode) method and override visit(TableColumnNode) below, so that the concordion commands are added to the <th> tag
        super.visit(node);
    }

    public void visit(TableColumnNode node) {
        super.visit(node);
        printCommandIfPending();
    }

    private void printCommandIfPending() {
        if (pendingCommand != null) {
            printCommand();
            pendingCommand = null;
        }
    }

    private void printCommand() {
        printConcordionCommandAttributes(pendingCommand);
    }

  //-----------------------------------------------------------------------------------------------------------------------
    @Override
    public void visit(ExpLinkNode node) {
        if ("-".equals(node.url)) {
            String text = printChildrenToString(node);
            if (text.startsWith("<em>")) {
                text = "";
            };
            if (inHeaderNode) {
                printer.printEncoded(text);
//                pendingExample = node.title;
            } else if (inTableHeader) {
                printer.printEncoded(text);
            } else {
                ConcordionCommand command = getCommandFor(node, text);
                printConcordionCommand(command);
            }
        } else {
            super.visit(node);
        }
    }

    private ConcordionCommand getCommandFor(ExpLinkNode node, String text) {
        String title = node.title;
        if (title.startsWith("#") && !(title.contains("="))) {
            return new ConcordionCommand("set", title, text);
        } else if (title.startsWith("?=")) {
            return new ConcordionCommand("assertEquals", title.substring(2), text);
        } else if (title.startsWith("c:")) {
            return parseCommandString(title, text);
        } else {
            return new ConcordionCommand("execute", title, text);
        }
    }
//-----------------------------------------------------------------------------------------------------------------------

    /**
     * <code>c:command=expression param=x param2='y' param3="z"</code>
     * @param commandString 
     * @param text
     * @return
     */
    private ConcordionCommand parseCommandString(String commandString, String text) {
        String[] components = commandString.split("=", 2);
        String command = components[0].substring(2);
        
        String expression = "";
        if (components.length > 1) {
            expression = components[1];
            String[] parts = expression.split("\\s+");
            expression = unquote(parts[0]);
            ConcordionCommand concordionCommand = new ConcordionCommand(command, expression, text);
            for (int i = 1; i < parts.length; i++) {
                String[] attributes = parts[i].split("=");
                concordionCommand.addAttribute(attributes[0], attributes[1] != null ? unquote(attributes[1]) : "");
            }
            return concordionCommand;
        } else {
            return new ConcordionCommand(command, "", text);
        }
    }

    private String unquote(String expression) {
        if ((expression.startsWith("'") && expression.endsWith("'")) || 
            (expression.startsWith("\"") && expression.endsWith("\""))) {
            expression = expression.substring(1, expression.length() - 1);
        }
        return expression;
    }
    
    public void visit(HeaderNode node) {
        inHeaderNode = true;
        boolean closingExample = false;
        for (Node child : node.getChildren()) {
            if (child instanceof StrikeNode) {
                if (inExample) {
                    String exampleHeading = printChildrenToString(node).replace("<del>", "").replace("</del>", "");
                    if (currentExampleHeading != null && currentExampleHeading.equals(exampleHeading)) {
                        closeExample();
                        closingExample = true;
                    }
                }
            }
            if (child instanceof ExpLinkNode) {
                if ("-".equals(((ExpLinkNode) child).url)) {
                    closeExampleIfNeedeed();
                    String exampleName = ((ExpLinkNode)child).title;
                    currentExampleHeading = printChildrenToString(node);
                    printer.println();
                    printer.print("<div");
                    printAttribute(namespaced("example"), exampleName);
                    printer.print(">");
                    inExample = true;
                }
            }
        }
        if (!closingExample) {
            super.visit(node);
        }
        closingExample = false;
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
    }

    private void printConcordionCommand(ConcordionCommand command) {
        printer.print('<').print("span");
        printConcordionCommandAttributes(command);
        printer.print('>');
        printer.printEncoded(command.text);
        printer.print('<').print('/').print("span").print('>');
    }

    private void printConcordionCommandAttributes(ConcordionCommand command) {
        printAttribute(namespaced(command.command.name), command.command.value);
        
        List<Attribute> attributes = command.attributes;
        for (Attribute attribute : attributes) {
            printAttribute(attribute.name, attribute.value);
        }
    }
    
    private boolean hasNonConcordionCodeChildren(Node node) {
        for (Node child : node.getChildren()) {
            if (!isConcordionCodeNode(child)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConcordionCodeNode(Node child) {
        return child instanceof CodeNode && ((CodeNode)child).getText().startsWith("c:");
    }
    
    private void visitNonConcordionCodeChildren(Node node) {
        for (Node child : node.getChildren()) {
            if (!(isConcordionCodeNode(child))) {
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
            if (node.title.startsWith("c:run")) {
                Rendering rendering = new Rendering(node.url, text);
                String suffix = node.title.substring("c:run".length()).trim();
                String runner;
                if (suffix.startsWith("=")) {
                    runner = suffix.substring(1).trim();
                    if (runner.startsWith("'") || runner.startsWith("\"")) {
                        runner = runner.substring(1, runner.length() - 1);
                    }
                } else {
                    runner = "concordion";
                }
                
                return rendering.withAttribute(concordionNamespacePrefix + ":" + "run", runner);
            } 
            return super.render(node, text);
        }
    }
}

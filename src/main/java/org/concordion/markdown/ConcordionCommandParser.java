package org.concordion.markdown;

import org.pegdown.ast.ExpLinkNode;

public class ConcordionCommandParser {

    private String targetConcordionNamespacePrefix;
    private final String sourceConcordionNamespacePrefix;

    public ConcordionCommandParser(String sourceConcordionNamespacePrefix, String targetConcordionNamespacePrefix) {
        this.sourceConcordionNamespacePrefix = sourceConcordionNamespacePrefix;
        this.targetConcordionNamespacePrefix = targetConcordionNamespacePrefix;
    }

    ConcordionCommand getCommandFor(ExpLinkNode node, String text) {
        String title = node.title;
        if (title.startsWith("#") && !(title.contains("="))) {
            return new ConcordionCommand("set", title, text);
        } else if (title.startsWith("?=")) {
            return new ConcordionCommand("assertEquals", title.substring(2), text);
        } else {
            if (title.startsWith(sourceConcordionNamespacePrefix + ":")) {
                return parseCommandString(title, text);
            } else {
                return new ConcordionCommand("execute", title, text);
            }
        }
    }

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
                String[] attributes = parts[i].split("=", 2);
                String attributeName = attributes[0];
                if (attributeName.startsWith(sourceConcordionNamespacePrefix + ":")) {
                    attributeName = targetConcordionNamespacePrefix + ":" + attributeName.substring(2);
                }
                concordionCommand.addAttribute(attributeName, attributes.length > 1 ? unquote(attributes[1]) : "");
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
}

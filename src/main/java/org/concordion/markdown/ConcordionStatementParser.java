package org.concordion.markdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConcordionStatementParser {

    private String targetPrefix;
    private final String sourcePrefix;

    public ConcordionStatementParser(String sourceConcordionNamespacePrefix, String targetConcordionNamespacePrefix) {
        this.sourcePrefix = sourceConcordionNamespacePrefix + ":";
        this.targetPrefix = targetConcordionNamespacePrefix + ":";
    }

    public ConcordionStatement parse(String expression) {
        return parse(expression, "");
    }

    public ConcordionStatement parse(String expression, String text) {
        if (expression.startsWith("#") && !(expression.contains("="))) {
            return new ConcordionStatement(targetPrefix + "set", expression).withText(text);
        } else if (expression.startsWith("?=")) {
            return new ConcordionStatement(targetPrefix + "assertEquals", expression.substring(2)).withText(text);
        } else {
            if (expression.startsWith(sourcePrefix)) {
                return parseStatement(expression.substring(sourcePrefix.length())).withText(text);
            } else {
                return new ConcordionStatement(targetPrefix + "execute", expression).withText(text);
            }
        }
    }

    private ConcordionStatement parseStatement(String statement) {
        String[] components = statement.split("=", 2);
        String commandName = components[0];
        String valueAndAttributes = components[1];
        
        return parseCommandValueAndAttributes(commandName, valueAndAttributes);
    }

    public ConcordionStatement parseCommandValueAndAttributes(String commandName, String commandValueAndAttributes) {
        Pattern pattern = Pattern.compile("(.*?)(?:\\s+\\S+\\=\\S+\\s*)*");
        Matcher matcher = pattern.matcher(commandValueAndAttributes);
        if (!matcher.matches()) {
            throw new IllegalStateException(String.format("Unexpected match failure for ''", commandValueAndAttributes));
        }
        
        String match = matcher.group(1);
        String commandValue = unquote(match);
        ConcordionStatement statement = new ConcordionStatement(targetPrefix + commandName, commandValue);
        
        if (match.length() < commandValueAndAttributes.length()) {
            String attributesStr = commandValueAndAttributes.substring(match.length());
            String[] attributes = attributesStr.trim().split("\\s+");
            
            for (String attribute : attributes) {
                String[] parts = attribute.split("=", 2);
                String attributeName = parts[0];
                if (attributeName.startsWith(sourcePrefix)) {
                    attributeName = targetPrefix + attributeName.substring(sourcePrefix.length());
                }
                statement.withAttribute(attributeName, parts.length > 1 ? unquote(parts[1]) : "");
            }
        }
        return statement;
    }

    private String unquote(String expression) {
        if ((expression.startsWith("'") && expression.endsWith("'")) || 
            (expression.startsWith("\"") && expression.endsWith("\""))) {
            expression = expression.substring(1, expression.length() - 1);
        }
        return expression;
    }
}

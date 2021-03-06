package org.concordion.markdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConcordionStatementParser {

    private static final Pattern COMMAND_VALUE_PATTERN = Pattern.compile("(.*?)(?:\\s+\\S+\\=\\S+\\s*)*");
    private final String sourcePrefix;
    private final String targetPrefix;

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
        Matcher commandValueMatcher = COMMAND_VALUE_PATTERN.matcher(commandValueAndAttributes);
        if (!commandValueMatcher.matches()) {
            throw new IllegalStateException(String.format("Unexpected match failure for ''", commandValueAndAttributes));
        }
        
        String match = commandValueMatcher.group(1);
        String commandValue = match;
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
                statement.withAttribute(attributeName, parts.length > 1 ? parts[1] : "");
            }
        }
        return statement;
    }
}

package org.concordion.markdown;

public class ConcordionExecuteNode extends ConcordionNode {

    private final String expression;

    public ConcordionExecuteNode(String expression, String text) {
        super(Type.Execute, text);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}

package org.concordion.markdown;

public class ConcordionVerifyRowsNode extends ConcordionNode {

    private final String expression;

    public ConcordionVerifyRowsNode(String expression, String text) {
        super(Type.VerifyRows, text);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}

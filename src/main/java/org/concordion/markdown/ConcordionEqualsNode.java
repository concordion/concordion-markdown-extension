package org.concordion.markdown;

public class ConcordionEqualsNode extends ConcordionNode {

    private String expression;

    public ConcordionEqualsNode(String expression, String text) {
        super(Type.AssertEquals, text);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}
package org.concordion.markdown;

import org.pegdown.ast.Node;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.Visitor;

public class ConcordionEqualsNode extends TextNode {

    private String expression;

    public ConcordionEqualsNode(String expression, String text) {
        super(text.trim());
        this.expression = expression.trim();
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit((Node) this);
    }
}
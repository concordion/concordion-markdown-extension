package org.concordion.markdown;

import org.pegdown.ast.Node;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.Visitor;

public class ConcordionEqualsNode extends TextNode {
    public ConcordionEqualsNode(String text) {
        super(text.trim());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit((Node) this);
    }
}
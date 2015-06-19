package org.concordion.markdown;

import org.pegdown.ast.Node;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.Visitor;

public class ConcordionSetNode extends TextNode {
    public ConcordionSetNode(String text) {
        super(text.trim());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit((Node) this);
    }
}
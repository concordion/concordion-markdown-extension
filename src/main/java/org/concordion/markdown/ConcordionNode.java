package org.concordion.markdown;

import org.pegdown.ast.Node;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.Visitor;

public class ConcordionNode extends TextNode {

    public enum Type { Set, AssertEquals, Execute, VerifyRows }

    private final Type type;

    public ConcordionNode(Type type, String text) {
        super(text);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit((Node) this);
    }
}
package org.concordion.markdown;

import org.pegdown.ast.Node;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.Visitor;

public class ConcordionSetNode extends TextNode {

    private String varName;

    public ConcordionSetNode(String varName, String text) {
        super(text.trim());
        this.varName = varName.trim();
    }

    public String getVarName() {
        return varName;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit((Node) this);
    }
}
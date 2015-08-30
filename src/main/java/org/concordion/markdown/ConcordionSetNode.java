package org.concordion.markdown;

public class ConcordionSetNode extends ConcordionNode {

    private final String varName;

    public ConcordionSetNode(String varName, String text) {
        super(Type.Set, text);
        this.varName = "#" + varName;
    }

    public String getVarName() {
        return varName;
    }
}
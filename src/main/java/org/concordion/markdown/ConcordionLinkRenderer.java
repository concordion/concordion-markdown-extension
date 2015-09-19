package org.concordion.markdown;

import org.pegdown.LinkRenderer;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.Node;

public class ConcordionLinkRenderer extends LinkRenderer {
    @Override
    public Rendering render(ExpLinkNode node, String text) {
        Rendering rendering = super.render(node, text);
        
        renderConcordionChildren(node, rendering);
        return rendering;
    }

    private void renderConcordionChildren(Node node, Rendering rendering) {
        for (Node child : node.getChildren()) {
            if (child instanceof ConcordionRunNode) {
                rendering.withAttribute("concordion:" + "run", "concordion");
            }
            renderConcordionChildren(child, rendering);
        }
    }
}

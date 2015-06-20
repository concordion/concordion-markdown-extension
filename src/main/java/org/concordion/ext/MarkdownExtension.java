package org.concordion.ext;

import org.concordion.api.Source;
import org.concordion.api.SpecificationLocator;
import org.concordion.api.Target;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.internal.FileTargetWithSuffix;

public class MarkdownExtension implements ConcordionExtension {

    public static final String EXTENSION_NAMESPACE = "urn:concordion-extensions:2013";
    private SpecificationLocator locator = new MarkdownLocator();
    private Source source = new MarkdownClassPathSource();
    private Target target = new FileTargetWithSuffix("html");
    
    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationLocator(locator).withSource(source).withTarget(target);
    }
}

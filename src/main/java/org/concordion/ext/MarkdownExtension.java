package org.concordion.ext;

import org.concordion.api.SpecificationLocator;
import org.concordion.api.Target;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.internal.FileTargetWithSuffix;

public class MarkdownExtension implements ConcordionExtension {

    public static final String EXTENSION_NAMESPACE = "urn:concordion-extensions:2010";
    private final SpecificationLocator locator = new MarkdownLocator();
    private final MarkdownClassPathSource source = new MarkdownClassPathSource();
    private final Target target = new FileTargetWithSuffix("html");

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationLocator(locator).withSource(source).withTarget(target);
    }

    public MarkdownExtension withInterimHtmlSavedTo(Target target) {
        source.setInterimHtmlTarget(target);
        return this;
    }

    public MarkdownExtension withConcordionNamespacePrefix(String prefix) {
        source.setConcordionNamespacePrefix(prefix);
        return this;
    }
}

package org.concordion.ext;

import org.concordion.api.SpecificationLocator;
import org.concordion.api.Target;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class MarkdownExtension implements ConcordionExtension {

    public static final String EXTENSION_NAMESPACE = "urn:concordion-extensions:2010";
    public static final String MARKDOWN_FILE_EXTENSION = "md";
    private final SpecificationLocator locator = new MarkdownLocator();
    private final MarkdownClassPathSource source = new MarkdownClassPathSource();
    private final Target target = new MarkdownSuffixRenamingTarget();

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationLocator(locator).withSource(source).withTarget(target);
    }

    public MarkdownExtension withSourceHtmlSavedTo(Target target) {
        source.setSourceHtmlTarget(target);
        return this;
    }

    public MarkdownExtension withPegdownExtensions(int extensions) {
        source.withPegdownExtensions(extensions);
        return this;
    }
}

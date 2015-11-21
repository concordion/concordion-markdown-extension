package spec.concordion.markdown;

import java.io.File;

import org.concordion.api.extension.Extension;
import org.concordion.api.extension.Extensions;
import org.concordion.ext.MarkdownExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.concordion.internal.FileTarget;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(MarkdownExtension.class)
public class Markdown {
    @Extension
    public MarkdownExtension extension = new MarkdownExtension().withSourceHtmlSavedTo(new FileTarget(new File("/tmp")));
}

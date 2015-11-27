package spec.concordion.markdown;

import java.io.File;

import org.concordion.api.extension.Extension;
import org.concordion.ext.MarkdownExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.concordion.internal.FileTarget;
import org.concordion.markdown.MarkdownParser;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class Grammar {
    MarkdownParser markdownParser = new MarkdownParser();

    @Extension
    public MarkdownExtension extension = new MarkdownExtension().withSourceHtmlSavedTo(new FileTarget(new File("/tmp")));
    
    public String translate(String markdown) {
        String html = markdownParser.markdownToHtml(markdown, "concordion");
        if (html.startsWith("<p>") && html.endsWith("</p>")) {
            html = html.substring(3, html.length()-4);
        }
        return html;
    }
}

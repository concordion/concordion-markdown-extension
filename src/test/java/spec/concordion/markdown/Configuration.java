package spec.concordion.markdown;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.MarkdownExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.concordion.markdown.MarkdownParser;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(MarkdownExtension.class)
public class Configuration {
    private int pegdownExtensions;
    
    public String translate(String markdown) {
        MarkdownParser markdownParser = new MarkdownParser(pegdownExtensions);
        String html = markdownParser.markdownToHtml(markdown, "concordion");
        if (html.startsWith("<p>") && html.endsWith("</p>")) {
            html = html.substring(3, html.length()-4);
        }
        return html;
    }
    
    public void withWikilinkAndQuotes() {
        pegdownExtensions = org.pegdown.Extensions.WIKILINKS | org.pegdown.Extensions.QUOTES;
    }
}

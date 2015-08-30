package org.concordion.markdown;

import org.parboiled.Parboiled;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.Parser;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;

public class MarkdownParser {
    private PegDownProcessor processor = new PegDownProcessor(Extensions.TABLES);
    private ConcordionPluginParser mpp = Parboiled.createParser(ConcordionPluginParser.class);
//    private PegDownPlugins plugins = new PegDownPlugins.Builder().withInlinePluginRules(mpp.concordionSetRule(), mpp.concordionEqualsRule()).build(); //Plugin(MyPluginParser.class, null).build();
//    private PegDownPlugins plugins = new PegDownPlugins.Builder().withInlinePluginRules(mpp.concordionRules()).build(); //Plugin(MyPluginParser.class, null).build();
    private PegDownPlugins plugins = new PegDownPlugins.Builder().withInlinePluginRules(mpp.concordionStatement()).build(); //Plugin(MyPluginParser.class, null).build();
    private Parser parser = Parboiled.createParser(Parser.class, Extensions.TABLES, 5000L, Parser.DefaultParseRunnerProvider, plugins);

    public String markdownToHtml(String markdown) {
        RootNode root = parser.parse(processor.prepareSource(markdown.toCharArray()));
        ToHtmlSerializer serializer = new ConcordionHtmlSerializer(new LinkRenderer());
        String html = serializer.toHtml(root);
        return html;
    }
}

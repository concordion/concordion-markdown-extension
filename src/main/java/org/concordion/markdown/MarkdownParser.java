package org.concordion.markdown;

import org.parboiled.Parboiled;
import org.pegdown.Extensions;
import org.pegdown.Parser;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;

public class MarkdownParser {
    private PegDownProcessor processor = new PegDownProcessor(Extensions.TABLES);
//    private ConcordionPluginParser mpp = Parboiled.createParser(ConcordionPluginParser.class);
//    private PegDownPlugins plugins = new PegDownPlugins.Builder().withInlinePluginRules(mpp.concordionSetRule(), mpp.concordionEqualsRule()).build(); //Plugin(MyPluginParser.class, null).build();
//    private PegDownPlugins plugins = new PegDownPlugins.Builder().withInlinePluginRules(mpp.concordionRules()).build(); //Plugin(MyPluginParser.class, null).build();
//    private PegDownPlugins plugins = new PegDownPlugins.Builder().withBlockPluginRules(mpp.concordionStatement()).build(); //Plugin(MyPluginParser.class, null).build();
    private Parser parser = Parboiled.createParser(Parser.class, Extensions.TABLES | Extensions.STRIKETHROUGH, 5000L, Parser.DefaultParseRunnerProvider); //, plugins);

    public String markdownToHtml(String markdown, String concordionNamespacePrefix) {
        RootNode root = parser.parse(processor.prepareSource(markdown.toCharArray()));
        ToHtmlSerializer serializer = new ConcordionHtmlSerializer(concordionNamespacePrefix);
        String html = serializer.toHtml(root);
        return html;
    }
}

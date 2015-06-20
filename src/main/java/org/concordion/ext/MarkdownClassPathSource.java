package org.concordion.ext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.concordion.api.Resource;
import org.concordion.api.Source;
import org.concordion.internal.ClassPathSource;
import org.concordion.markdown.ConcordionHtmlSerializer;
import org.concordion.markdown.ConcordionPluginParser;
import org.parboiled.Parboiled;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.Parser;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;

//import com.github.rjeschke.txtmark.Processor;

public class MarkdownClassPathSource implements Source {

    Source classPathSource = new ClassPathSource();
    PegDownProcessor processor = new PegDownProcessor(Extensions.TABLES);
    ConcordionPluginParser mpp = Parboiled.createParser(ConcordionPluginParser.class);
    PegDownPlugins plugins = new PegDownPlugins.Builder().withInlinePluginRules(mpp.concordionEqualsRule(), mpp.concordionSetRule()).build(); //Plugin(MyPluginParser.class, null).build();
    Parser parser = Parboiled.createParser(Parser.class, Extensions.TABLES, 5000L, Parser.DefaultParseRunnerProvider, plugins);
    
    
    @Override
    public InputStream createInputStream(Resource resource) throws IOException {
        InputStream inputStream = classPathSource.createInputStream(resource);
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputStream,"UTF-8");
            String markdown = scanner.useDelimiter("\\A").next();
            RootNode root = parser.parse(processor.prepareSource(markdown.toCharArray()));
            ToHtmlSerializer serializer = new ConcordionHtmlSerializer(new LinkRenderer());
            String html = serializer.toHtml(root);        
            
            System.out.println(html);
            html = wrapBody(html);
            return new ByteArrayInputStream(html.getBytes());
        } finally {
            scanner.close();
        }
    }

    private String wrapBody(String body) {
        return "<html xmlns:concordion='http://www.concordion.org/2007/concordion'><body>" + body + "</body></html>";
    }

    @Override
    public boolean canFind(Resource resource) {
        return classPathSource.canFind(resource);
    }
}

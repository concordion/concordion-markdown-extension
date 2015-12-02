package org.concordion.markdown;

import org.parboiled.Parboiled;
import org.pegdown.Extensions;
import org.pegdown.Parser;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;

public class MarkdownParser {
    private int pegdownExtensions;

    private boolean useLegacyExamples;
    
    public MarkdownParser() {
        this(0);
    }
    
    public MarkdownParser(int pegdownExtensions) {
        this(pegdownExtensions, missingExampleCommand());
    }
    
    private static boolean missingExampleCommand(){
        //look for the name of the ExampleCommand class in concordion
        //which was added in Concordion 2.0
        try{
            Class.forName("org.concordion.internal.command.ExampleCommand");
            return false;
        }catch(ClassNotFoundException e){
            return true;
        }
    }
    
    public MarkdownParser(int pegdownExtensions, boolean useLegacyExamples) {
        this.pegdownExtensions = pegdownExtensions;
        this.useLegacyExamples = useLegacyExamples;
    }

    public String markdownToHtml(String markdown, String concordionNamespacePrefix) {
        Parser parser = Parboiled.createParser(Parser.class, Extensions.TABLES | Extensions.STRIKETHROUGH | pegdownExtensions, 5000L, Parser.DefaultParseRunnerProvider);
        PegDownProcessor processor = new PegDownProcessor();
        RootNode root = parser.parse(processor.prepareSource(markdown.toCharArray()));
        ToHtmlSerializer serializer = new ConcordionHtmlSerializer(concordionNamespacePrefix, useLegacyExamples);
        return serializer.toHtml(root);
    }
}

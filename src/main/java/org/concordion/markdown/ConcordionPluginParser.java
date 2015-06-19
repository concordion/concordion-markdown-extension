package org.concordion.markdown;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;

public class ConcordionPluginParser extends Parser {

    public ConcordionPluginParser() {
        super(ALL, 1000l, DefaultParseRunnerProvider);
    }

    public Rule ConcordionEqualsPlugin() {
        StringBuilderVar text = new StringBuilderVar();
        return NodeSequence(
                "{.is",
                OneOrMore(TestNot("}"), BaseParser.ANY, text.append(matchedChar())),
                push(new ConcordionEqualsNode(text.getString())),
                "}"
                );
    }

    public Rule ConcordionSetPlugin() {
        StringBuilderVar text = new StringBuilderVar();
        return NodeSequence(
                "{.set",
                OneOrMore(TestNot("}"), BaseParser.ANY, text.append(matchedChar())),
                push(new ConcordionSetNode(text.getString())),
                "}"
        );
    }
}

package org.concordion.markdown;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;

public class ConcordionPluginParser extends Parser {

    public ConcordionPluginParser() {
        super(ALL, 1000l, DefaultParseRunnerProvider);
    }
    
    public Rule concordionStatement() {
        StringBuilderVar text = new StringBuilderVar();
        return NodeSequence(
            "{",
            OneOrMore(TestNot(" `"), ANY, text.append(matchedChar())),
            " `",
            FirstOf(setCommand(text), assertEqualsCommand(text)),
            "`}"
        );
    }
    
    public Rule setCommand(StringBuilderVar text) {
        StringBuilderVar varName = new StringBuilderVar();
        return NodeSequence(
            "#",
            OneOrMore(TestNot("`}"), BaseParser.ANY, varName.append(matchedChar())),
            push(new ConcordionSetNode(varName.getString(), text.getString()))
        );
    }

    public Rule assertEqualsCommand(StringBuilderVar text) {
        StringBuilderVar expression = new StringBuilderVar();
        return NodeSequence(
                "?=",
                OneOrMore(TestNot("`}"), BaseParser.ANY, expression.append(matchedChar())),
                push(new ConcordionEqualsNode(expression.getString(), text.getString()))
        );
    }
    
    // c:set: {#x="1"}  {#x="1\"}"}
    // c:assertEquals: {add(#x,#y)=="3"}   {#result=="7"} {greeting=="Hello"}  {getGreeting()=="Hello"}
    // c:execute: {foo()} {#x=foo()} {foo(#TEXT)} {#x=foo(#TEXT)} {foo(#x, #y)} {#z=foo(#x, #y)} {#x=greeting} {foo(#x, "one")}
    // c:verifyRows: {#detail:getDetails()} {#detail: #details} {#detail: details}
}

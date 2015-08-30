package org.concordion.markdown;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;

import java.util.regex.Pattern;

public class ConcordionPluginParser extends Parser {

    public ConcordionPluginParser() {
        super(ALL, 1000l, DefaultParseRunnerProvider);
    }

    public Rule concordionEqualsRule() {
        StringBuilderVar expression = new StringBuilderVar();
        StringBuilderVar text = new StringBuilderVar();
        return NodeSequence(
                "{",
                OneOrMore(TestNot(AnyOf("=}")), BaseParser.ANY, expression.append(matchedChar())),
                "==\"",
                OneOrMore(TestNot("\"}"), BaseParser.ANY, text.append(matchedChar())),
                push(new ConcordionEqualsNode(expression.getString(), text.getString())),
                "\"}"
                );
    }

    public Rule concordionSetRule() {
        StringBuilderVar varName = new StringBuilderVar();
        StringBuilderVar text = new StringBuilderVar();
        return NodeSequence(
                "{#",
                OneOrMore(TestNot(AnyOf("=}")), BaseParser.ANY, varName.append(matchedChar())),
                "=\"",
                OneOrMore(TestNot("\"}"), BaseParser.ANY, text.append(matchedChar())),
                push(new ConcordionSetNode(varName.getString(), text.getString())),
                "\"}"
        );
    }

    public Rule concordionExecuteRule() {
        StringBuilderVar expression = new StringBuilderVar();
        return NodeSequence(
                "{",
                OneOrMore(TestNot("}"), BaseParser.ANY, expression.append(matchedChar())),
                push(new ConcordionExecuteNode(expression.getString())),
                "}"
        );
    }

    // c:set: {#x="1"}  {#x="1\"}"}
    // c:assertEquals: {add(#x,#y)=="3"}   {#result=="7"} {greeting=="Hello"}  {getGreeting()=="Hello"}
    // c:execute: {foo()} {#x=foo()} {foo(#TEXT)} {#x=foo(#TEXT)} {foo(#x, #y)} {#z=foo(#x, #y)} {#x=greeting} {foo(#x, "one")}
    // c:verifyRows: {#detail:getDetails()} {#detail: #details} {#detail: details}
}

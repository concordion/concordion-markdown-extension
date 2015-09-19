# Concordion Markdown

The Concordion Markdown extension allows you to write your Concordion input in the Markdown format.

While Markdown allows you to embed HTML, this extension also provides a simplified grammar for adding Concordion commands that fits better with the idioms of Markdown.

## concordion:set

The `concordion:set` command is expressed using the syntax: ``{value `#varname`}``

which sets the variable named `varname` to the value `value`.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>{1 `#x`}</td>
      <td>&lt;span concordion:set="#x"&gt;1&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{Bob Smith `#name`}</td>
      <td>&lt;span concordion:set="#name"&gt;Bob Smith&lt;/span&gt;</td>
    </tr>
<!-- TODO escape    
    <tr>
      <td>{`code snippet` `#snippet`}</td>
      <td>&lt;span concordion:set="#snippet"&gt;`code snippet`&lt;/span&gt;</td>
    </tr>
-->    
  </table>
</div>

## concordion:assertEquals

The `concordion:assertEquals` command is expressed using the syntax: ``{value `?=expression`}``

which asserts that the result of evaluating _expression_ equals the value _value_.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>{1 `?=#x`}</td>
      <td>&lt;span concordion:assertEquals="#x"&gt;1&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{Bob Smith `?=#name`}</td>
      <td>&lt;span concordion:assertEquals="#name"&gt;Bob Smith&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{3 `?=add(#x, #y)`}</td>
      <td>&lt;span concordion:assertEquals="add(#x, #y)"&gt;3&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{Hello `?=getGreeting()`}</td>
      <td>&lt;span concordion:assertEquals="getGreeting()"&gt;Hello&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{Hello `?=greeting`}</td>
      <td>&lt;span concordion:assertEquals="greeting"&gt;Hello&lt;/span&gt;</td>
    </tr>
  </table>
</div>

## concordion:execute

The `concordion:execute` command is expressed using the syntax: ``{`expression`}``

which executes the _expression_.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>{`foo()`}</td>
      <td>&lt;span concordion:execute="foo()"&gt;&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{`#x=foo()`}</td>
      <td>&lt;span concordion:execute="#x=foo()"&gt;&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{sometext `foo(#TEXT)`}</td>
      <td>&lt;span concordion:execute="foo(#TEXT)"&gt;sometext&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{`foo(#x, #y)`}</td>
      <td>&lt;span concordion:execute="foo(#x, #y)"&gt;&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{`#x=greeting`}</td>
      <td>&lt;span concordion:execute="#x=greeting"&gt;&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{`foo(#x, "one")`}</td>
      <td>&lt;span concordion:execute="foo(#x, "one")"&gt;&lt;/span&gt;</td>
    </tr>
  </table>
</div>


## Edge cases
The following command generates identical results with either `concordion:set` or `concordion:execute`.

This grammer uses `concordion:set`.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>{sometext `#x=foo(#TEXT)`}</td>
      <td>&lt;span concordion:set="#x=foo(#TEXT)"&gt;sometext&lt;/span&gt;</td>
    </tr>
  </table>
</div>  

<!--
## Brackets before the Concordion expression 

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>{Other stuff in brackets}{2 `#x`}</td>
      <td>{Other stuff in brackets}&lt;span concordion:set="#x"&gt;2&lt;/span&gt;</td>
    </tr>
  </table>
</div>  
-->

## Multiple commands on a single line

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>{1 `#x`} + {2 `#y`} = {3 `?=add(#x,#y)`}</td>
      <td>&lt;span concordion:set="#x"&gt;1&lt;/span&gt; + &lt;span concordion:set="#y"&gt;2&lt;/span&gt; = &lt;span concordion:assertEquals="add(#x,#y)"&gt;3&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{3 `?=three()`}. {Fred `#name`}.</td>
      <td>&lt;span concordion:assertEquals="three()"&gt;3&lt;/span&gt;. &lt;span concordion:set="#name"&gt;Fred&lt;/span&gt;.</td>
    </tr>
  </table>
</div>

##Execute on a table

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>
<pre>      
| {Number 1 `#x`}| {Number 2 `#y`}| {Result `?=#z`}|<br/>
| -------------: | -------------: | -------------: |<br/>
|               1|               0|               1|<br/>
|               1|              -3|              -2|<br/>
[{`#z=add(#x, #y)`}]</pre>
      </td>
      <td>
<![CDATA[<table concordion:execute="#z=add(#x, #y)">
  <thead>
    <tr>
      <th align="right" concordion:set="#x">Number 1</th>
      <th align="right" concordion:set="#y">Number 2</th>
      <th align="right" concordion:assertEquals="#z">Result</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="right">1</td>
      <td align="right">0</td>
      <td align="right">1</td>
    </tr>
    <tr>
      <td align="right">1</td>
      <td align="right">-3</td>
      <td align="right">-2</td>
    </tr>
  </tbody>
</table>]]>     
      </td>
    </tr>
  </table>
</div>

<div class="example">
  <h3>Example on a table with a caption</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>
<pre>      
| {Number 1 `#x`}| {Number 2 `#y`}| {Result `?=#z`}|<br/>
| ---------------| -------------- | -------------- |<br/>
|               1|               0|               1|<br/>
|               1|              -3|              -2|<br/>
[This table has a caption too. {`#z=add(#x, #y)`}]</pre>
      </td>
      <td>
<![CDATA[<table concordion:execute="#z=add(#x, #y)">
  <thead>
    <tr>
      <th concordion:set="#x">Number 1</th>
      <th concordion:set="#y">Number 2</th>
      <th concordion:assertEquals="#z">Result</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>1</td>
      <td>0</td>
      <td>1</td>
    </tr>
    <tr>
      <td>1</td>
      <td>-3</td>
      <td>-2</td>
    </tr>
  </tbody>
  <caption>This table has a caption too. </caption>  
</table>]]>     
      </td>
    </tr>
  </table>
</div>


    // c:execute: {foo()} {#x=foo()} {foo(#TEXT)} {#x=foo(#TEXT)} {foo(#x, #y)} {#z=foo(#x, #y)} {#x=greeting} {foo(#x, "one")}

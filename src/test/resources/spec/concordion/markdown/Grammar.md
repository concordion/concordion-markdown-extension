# Concordion Markdown

## concordion:set

The `concordion:set` command is expressed using the syntax: `[value](- "#varname")` or `[value](- '#varname')`

which sets the variable named `varname` to the value `value`.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[1](- "#x")</td>
      <td>&lt;span concordion:set="#x"&gt;1&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[Bob Smith](- '#name')</td>
      <td>&lt;span concordion:set="#name"&gt;Bob Smith&lt;/span&gt;</td>
    </tr>
  </table>
</div>

## concordion:assertEquals

The `concordion:assertEquals` command is expressed using the syntax: `[value](- "?=expression")` or `[value](- '?=expression')`

which asserts that the result of evaluating _expression_ equals the value _value_.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[1](- "?=#x")</td>
      <td>&lt;span concordion:assertEquals="#x"&gt;1&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[Bob Smith](- '?=#name')</td>
      <td>&lt;span concordion:assertEquals="#name"&gt;Bob Smith&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[3](- "?=add(#x, #y)")</td>
      <td>&lt;span concordion:assertEquals="add(#x, #y)"&gt;3&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[Hello](- "?=getGreeting()")</td>
      <td>&lt;span concordion:assertEquals="getGreeting()"&gt;Hello&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[Hello](- "?=greeting")</td>
      <td>&lt;span concordion:assertEquals="greeting"&gt;Hello&lt;/span&gt;</td>
    </tr>
  </table>
</div>

## concordion:execute

The `concordion:execute` command is expressed using the syntax: `[value](- "expression")` or `[value](- 'expression')`

which executes the _expression_. 

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[When I apply](- "apply()")</td>
      <td>&lt;span concordion:execute="apply()"&gt;When I apply&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[the time is](- "setTime(#date, #time)")</td>
      <td>&lt;span concordion:execute="setTime(#date, #time)"&gt;the time is&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[The greeting for](- "#msg=getGreeting()")</td>
      <td>&lt;span concordion:execute="#msg=getGreeting()"&gt;The greeting for&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[The greeting for](- "#msg=greeting")</td>
      <td>&lt;span concordion:execute="#msg=greeting"&gt;The greeting for&lt;/span&gt;</td>
    </tr>
  </table>
</div>

If the special variable `#TEXT` is used as a parameter within the _expression_, it is replaced by the _value_.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[09:00AM](- "setCurrentTime(#TEXT)")</td>
      <td>&lt;span concordion:execute="setCurrentTime(#TEXT)"&gt;09:00AM&lt;/span&gt;</td>
    </tr>
  </table>
</div>

## Multiple commands on a single line

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[1](- "#x") + [2](- "#y") = [3](- "?=add(#x,#y)")</td>
      <td>&lt;span concordion:set="#x"&gt;1&lt;/span&gt; + &lt;span concordion:set="#y"&gt;2&lt;/span&gt; = &lt;span concordion:assertEquals="add(#x,#y)"&gt;3&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[3](- "?=three()"). [Fred](- "#name").</td>
      <td>&lt;span concordion:assertEquals="three()"&gt;3&lt;/span&gt;. &lt;span concordion:set="#name"&gt;Fred&lt;/span&gt;.</td>
    </tr>
  </table>
</div>


## Non-Concordion links
The set, assertEquals and execute commands require the link URL to be set to -. Links with other URLs are not modified. For example:

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[John](- "#a")</td>
      <td>&lt;span concordion:set="#a"&gt;John&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>[John](john.html)</td>
      <td>&lt;a href="john.html"&gt;John&lt;/a&gt;</td>
    </tr>
    <tr>
      <td>[John](john.html "Details about John")</td>
      <td>&lt;a href="john.html" title="Details about John"&gt;John&lt;/a&gt;</td>
    </tr>
    <tr>
      <td>[John](john.html "#More about John")</td>
      <td>&lt;a href="john.html" title="#More about John"&gt;John&lt;/a&gt;</td>
    </tr>
    <tr>
      <td>[John](-.html "Weird URL")</td>
      <td>&lt;a href="-.html" title="Weird URL"&gt;John&lt;/a&gt;</td>
    </tr>
    <tr>
      <td>[John](.)</td>
      <td>&lt;a href="."&gt;John&lt;/a&gt;</td>
    </tr>
    <tr>
      <td>[John](#)</td>
      <td>&lt;a href="#"&gt;John&lt;/a&gt;</td>
    </tr>
  </table>
</div>

## Expression-only commands 
Some commands only require an expression and don't need a text value to be passed. However, Markdown links always require text for the URL.

Any URL that is written in italics will be set to an empty text value. 

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[_set time_](- "setCurrentTime(#time)")</td>
      <td>&lt;span concordion:execute="setCurrentTime(#time)"&gt;&lt;/span&gt;</td>
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
|[_add_](- "#z=add(#x, #y)")|
|[Number 1](- "#x")|[Number 2](- "#y")|[Result](- "?=#z")|<br/>
| ---------------: | ---------------: | ---------------: |<br/>
|                 1|                 0|                 1|<br/>
|                 1|                -3|                -2|<br/>
</pre>
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

##Verify Rows

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
|[_check GST_](- "c:verifyRows=#detail:getInvoiceDetails()") |<br/>
|[Sub Total](- "?=#detail.subTotal")|[GST](- "?=#detail.gst")|<br/>
| --------------------------------- | ---------------------: |<br/>
|                                100|                      15|<br/>
|                                500|                      75|<br/>
|                                 20|                       2|<br/>
        </pre>
      </td>
      <td>
<![CDATA[<table concordion:verifyRows="#detail:getInvoiceDetails()">
<thead>
    <tr>
      <th concordion:assertEquals="#detail.subTotal">Sub Total</th>
      <th align="right" concordion:assertEquals="#detail.gst">GST</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>100</td>
      <td align="right">15</td>
    </tr>
    <tr>
      <td>500</td>
      <td align="right">75</td>
    </tr>
    <tr>
      <td>20</td>
      <td align="right">2</td>
    </tr>
  </tbody>
</table>]]>     
      </td>
    </tr>
  </table>
</div>

## Run

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
[Whatever](whatever.html "c:run")
        </pre>
      </td>
      <td>
<![CDATA[<a href="whatever.html" concordion:run="concordion">Whatever</a>]]>     
      </td>
    </tr>
    <tr>
      <td>
        <pre>      
[Whatever](whatever.html "c:run=exampleRunner")
        </pre>
      </td>
      <td>
<![CDATA[<a href="whatever.html" concordion:run="exampleRunner">Whatever</a>]]>     
      </td>
    </tr>
    <tr>
      <td>
        <pre>      
[Whatever](whatever.html "c:run='exampleRunner'")
        </pre>
      </td>
      <td>
<![CDATA[<a href="whatever.html" concordion:run="exampleRunner">Whatever</a>]]>     
      </td>
    </tr>
    <tr>
      <td>
        <pre>      
[Whatever](whatever.html 'c:run="exampleRunner"')
        </pre>
      </td>
      <td>
<![CDATA[<a href="whatever.html" concordion:run="exampleRunner">Whatever</a>]]>     
      </td>
    </tr>
  </table>
</div>

## Examples

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th>Description</th>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>

    <tr>
      <td>h4 example using atx-style syntax</td>
      <td>
        <pre>      
#### [Example 1](- "calculator")
x
        </pre>
      </td>
      <td>
<![CDATA[<div concordion:example="calculator"> <h4>Example 1</h4> <p>x</p>]]>&lt;/div>   
      </td>
    </tr>

    <tr>
      <td>h1 example using setext-style syntax</td>
      <td>
        <pre>      
[Example 3](- "setext")
=====================================================
x
        </pre>
      </td>
      <td>
<![CDATA[<div concordion:example="setext"> <h1>Example 3</h1> <p>x</p>]]>&lt;/div>   
      </td>
    </tr>

    <tr>
      <td>Example automatically ended by start of another example</td>
      <td>
        <pre>      
#### [Example 1](- "calculator")
x
#### [Example 2](- "another")
        </pre>
      </td>
      <td>
<![CDATA[<div concordion:example="calculator"> <h4>Example 1</h4> <p>x</p>]]>&lt;/div>
<![CDATA[<div concordion:example="another"> <h4>Example 2</h4>]]>&lt;/div>    
      </td>
    </tr>

    <tr>
      <td>Example is not automatically ended by a lower-level heading</td>
      <td>
        <pre>      
#### [Example 1](- "calculator")
x
##### Subheading
        </pre>
      </td>
      <td>
<![CDATA[<div concordion:example="calculator"> <h4>Example 1</h4> <p>x</p> <h5>Subheading</h5>]]>&lt;/div> 
      </td>
    </tr>

    <tr>
      <td>Example is automatically ended by a higher-level heading</td>
      <td>
        <pre>      
### [Example on h3](- "ex3")
My example
## head2
        </pre>
      </td>
      <td>
<![CDATA[<div concordion:example="ex3"> <h3>Example on h3</h3>]]>
&lt;p>My example&lt;/p>&lt;/div>
&lt;h2>head2&lt;/h2>
      </td>
    </tr>

    <tr>
      <td>Example ended by a strikethrough heading with the same title as the example</td>
      <td>
        <pre>      
# [Example 1](- "calculator")
x
# ~~Example 1~~
y
        </pre>
      </td>
      <td>
<![CDATA[<div concordion:example="calculator"> <h1>Example 1</h1> <p>x</p>]]>&lt;/div>
&lt;p>y&lt;/p>     
      </td>
    </tr>

    <tr>
      <td>Example with `ExpectedToFail` status</td>
      <td>
        <pre>      
# [Example 1](- "calculator" status="ExpectedToFail")
        </pre>
      </td>
      <td>
<![CDATA[<div concordion:example="calculator" status="ExpectedToFail"> <h1>Example 1</h1>]]>&lt;/div>
      </td>
    </tr>

  </table>
</div>

## Arbitrary Commands

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
[value](- "c:command=expression")              
        </pre>
      </td>
      <td>
<![CDATA[<span concordion:command="expression">value</span>]]>
      </td>
    </tr>
    <tr>
      <td>
        <pre>
[value](- "c:command='expression'")              
        </pre>
      </td>
      <td>
<![CDATA[<span concordion:command="expression">value</span>]]>
      </td>
    </tr>
    <tr>
      <td>
        <pre>
[value](- 'c:command="expression"')              
        </pre>
      </td>
      <td>
<![CDATA[<span concordion:command="expression">value</span>]]>
      </td>
    </tr>
    <tr>
      <td>
        <pre>
[value](- "c:command=expression param=x")              
        </pre>
      </td>
      <td>
<![CDATA[<span concordion:command="expression" param="x">value</span>]]>
      </td>
    </tr>
    <tr>
      <td>
        <pre>
[value](- "c:command='expression' param1=x param2='y'")              
        </pre>
      </td>
      <td>
<![CDATA[<span concordion:command="expression" param1="x" param2="y">value</span>]]>
      </td>
    </tr>
    <tr>
      <td>
        <pre>
[value](- 'c:command="expression" param1=x param2="y"')              
        </pre>
      </td>
      <td>
<![CDATA[<span concordion:command="expression" param1="x" param2="y">value</span>]]>
      </td>
    </tr>
  </table>
</div>

    
## Complex examples


## Encode HTML



    
# Concordion Command Examples

### Basic example

Demonstrates the usage of `set` and `assertEquals` commands.

[1](- "#x") + [2](- "#y") = [3](- "?=add(#x,#y)")

### Set and assert in same statement

Occasionally it is useful to be able to set and assert a value in the same statement, for example selecting a user name and then checking that the user name is displayed.
This can be achieved using the special variable `#TEXT`, which contains the text of the current element.

My name is [Michael Caine](- "?=setAndReturn(#TEXT)")

### Example with execute

The `execute` command can return void, a primitive, a POJO (plain old Java object) or a map.

An `execute` with a `void` result often indicates a "bad smell".

See [executeVoid](http://concordion.org/Tutorial.html#executeVoid).

This example uses the `execute` commands for instructions with `void` results and primitive results.

This example also shows the use of the special variable `#TEXT`, which contains the text of the current element.
`#TEXT` can also be used with assert commands.

[3](- "setMemory(#TEXT)") + 
[4](- "#result=addToMemory(#TEXT)") = 
[7](- "?=#result")

### Example with execute returning a POJO

Subsequent commands can access public fields and methods of the object. When using a property (dot) notation,
Concordion will first check for a public field with the property name, then for a corresponding getter.
In the following example, `#detail.gst` resolves to a call to the `getGst()` method.

[The invoice](- "#detail = getInvoiceDetail()") shows a sub-total of
$[100](- "?=#detail.subTotal") + GST of
$[15](- "?=#detail.gst") giving a total of
$[115](- "?=#detail.calculateTotal()").

### Example with execute returning a map

[The invoice](- "#detail = getInvoiceDetailAsMap()") shows a sub-total of
$[100](- "?=#detail.subTotal") + GST of
$[15](- "?=#detail.gst").

### Unusual sentences

Unusual sentences can be resolved by using an `execute` command as a parent element.
(However it may be preferable to just rephrase the sentence!).

The `set` commands of child elements are called first,
followed by its own `execute` command,
followed by the `execute` commands of child elements,
then the `assertEquals` commands of child elements.
See [executeUnusualSentences](http://concordion.org/Tutorial.html#executeUnusualSentences).

<div class="example">
<span concordion:execute="#z3=add(#x3,#y3)">
<span concordion:assertEquals="#z3">11</span> = <span concordion:set="#x3">6</span> + <span concordion:set="#y3">5</span>.
</span>

### Execute on a table

When you place an execute command on a `<table>` element the commands on the header row
(the row containing `<th>` elements) are copied to each detail row (rows containing `<td>` elements)
and the execute  command is run on each detail row.

Example: Adding _Number 1_ to _Number 2_ equals the _Result_

| [Number 1](- "#x") | [Number 2](- "#y") | [Result](- "?=#z") |
| --------------: | --------------: | -------------: |
|               1 |               0 |              1 |
|               1 |              -3 |             -2 |
[c:execute](- "#z=add(#x, #y)")
[c:execute (- "#z=add(#x, #y)")]
[`#z=add(#x, #y)`]
[{`c:execute #z=add(#x, #y)`}]

### Verify Rows

Checks a collection of results returned from the fixture.
The collection must be sorted and implement `java.lang.Iterable`.

It may be necessary to sort the collection in the fixture if it is not already sorted.

| {Sub Total `?=#detail.subTotal`} | {GST `?=#detail.gst`} |
| ------------------------------: | -------------------: |
|                             100 |                   15 |
|                             500 |                   75 |
|                              20 |                    3 |
[{`for #detail:getInvoiceDetails()`}]
[{`for #detail:getInvoiceDetails()`}]

[{`c:verifyRows #detail:getInvoiceDetails() matchStrategy="BestMatch"`}]

[{`c:verifyRows="#detail:getInvoiceDetails()"`}]

### Echo

Normally used for adding information about a test run:

<div class="example">
Tests executed using <span concordion:echo="getBrowserDetails()"></span>.

Tests executed using {`echo getBrowserDetails()`}.
</div>
Tests executed using {`c:echo getBrowserDetails()`}.


### Run

Runs another test from this test. See [Run command](http://concordion.org/dist/1.3.1/test-output/concordion/spec/concordion/command/run/Run.html).

[Calc](Calc.html "c:run")

### Accessing contents of an HREF

Allows you to put test data in a separate file and access it from a specification.
This may indicate a "test smell".
See [Access to a Link's HREF](http://concordion.org/dist/1.3.1/test-output/concordion/spec/concordion/command/execute/AccessToLinkHref.html).

### Examples

#### [Example 1](- "calculator status='ExpectedToFail'")

~~[Example 1](- "example1")~~

<del>Example 1</del>

Other Text
[Example 2](- "example2")


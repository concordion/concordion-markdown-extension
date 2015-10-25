# Concordion Markdown

## Philosophy
Markdown provides an easy-to-read and easy-to-write syntax for converting plain text to structured XHTML.

This Concordion Markdown extension allows you to write your Concordion input in the Markdown format. It adds support for embedding Concordion commands within Markdown and running the resultant XHTML as a Concordion specification. 

While Markdown allows you to embed HTML, this extension provides a simplified grammar for adding Concordion commands that fits better with the idioms of Markdown. 

### Use of inline links 

In order to keep the grammar readable, we have used Markdown's inline links to embed the Concordion commands. This maintains a clean separation of Concordion commands from the original text.  

As an example:

> `When Jane logs on, she is greeted with Hello Jane!`

could be marked up as:

> `When [Jane](- "#name") logs on, she is greeted with [Hello Jane!](- "?=greetingFor(#name)")`
 
where:

 * `[Jane](- '#name')` sets the `#name` variable to `Jane`.
 * `[Hello Jane!](- '?=greetingFor(#name)')` asserts that the `greetingFor(#name)` method returns `Hello Jane!`

When viewed on Github or in the preview pane of a Markdown editor, the original text is displayed as a link, and the Concordion command shown when you hover over the link:

> ![Github Markdown Example](img/IntroGithub.png)

When the specification is run with a fixture that implements the `greetingFor(String)` method, the links are converted to spans and the output is shown as:

> ![Markdown Example Output](img/IntroOutput.png)

## Installation
TODO

## Basic Grammar

For the full Grammar see the [Grammar Specification](TODO).

### Set Command
    [value](- "#varname")

### AssertEquals Command
    [value](- "?=expression")

### Execute Command
    [value](- "expression")

### Example Command
You can use either the Atx-style or Setext-style headers. :

    ## [Example 1](- "exampleName")

or 

    [Example 1](- "exampleName")
    ----------------------------------

will create an example named `exampleName` with the H2 heading `Example 1`.


#### Closing an example
An example is implicitly closed when another example starts or the end of file is reached.

To explicitly close an example, create a header with the example heading struck-through. For example:  

    ## ~~Example 1~~
    
will close the example with the heading `Example 1`    

Note that the example command requires Concordion 2.0.0 or later.

### Table Commands
The command to be run on the table is specified in the first table header row, with the commands for each column of the table specified in the second table header row.

The first table header row is not shown on the output HTML.

#### Execute on a table

    |[_add_](- "#z=add(#x, #y)")|
    |[Number 1](- "#x")|[Number 2](- "#y")|[Result](- "?=#z")|
    | ---------------: | ---------------: | ---------------: |
    |                 1|                 0|                 1|
    |                 1|                -3|                -2|
    [`c:execute #z=add(#x, #y)`]


#### Verify Rows

    |[_check GST_](- "c:verifyRows=#detail:getInvoiceDetails()") |
    |[Sub Total](- "?=#detail.subTotal")|[GST](- "?=#detail.gst")|
    | --------------------------------- | ---------------------: |
    |                                100|                      15|
    |                                 20|                       2|

### Run Command
    [Whatever](whatever.html "c:run")







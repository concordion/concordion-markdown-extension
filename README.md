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

## Grammar

### Set Command
`[value](- "#varname")` or `[value](- '#varname')`.

### AssertEquals Command
`[value](- "?=expression")` or `[value](- '?=expression')`.

### Execute Command
`[value](- "expression")` or `[value](- 'expression')`

### Run Command
`[Whatever](whatever.html "c:run")`

To specify a custom runner:
`[Whatever](whatever.html "c:run=customRunner")` or `[Whatever](whatever.html "c:run='customRunner'")` or `[Whatever](whatever.html 'c:run="customRunner"')`

### Example Command
Examples are specified using a link on a header. 

You can use either the Atx-style or Setext-style headers. For example:

`#### [Example Heading](- "exampleName")`

creates an example named `exampleName` with the H4 heading `Example Heading`.

or


    [Example 3](- "setextExample")
    =====================================================


creates an example named `setextExample` with the H1 heading `Example 3`.


You can apply a status to the example by appending an attribute containing the status, for example

`#### [Example 1](- "example1 status='ExpectedToFail'")`

or 

`## [Example 2](- "anotherExample status=Unimplemented")`

or 

    [Example 3](- 'exampleSetext status="Unimplemented"')
    =====================================================

#### Ending an example
An example is implicitly ended when another example is encountered or the end of file is reached.

To explicitly close an example, create a header with the example heading struck-through. For example:  

    #### ~~Example 1~~
    
will close the example with the heading `Example 1`    

Note that the example command requires Concordion 2.0.0 or later.






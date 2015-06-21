# Concordion Markdown

The Concordion Markdown extension allows you to write your Concordion input in the Markdown format.

While Markdown allows you to embed HTML, this extension also provides a simplified grammar for adding Concordion commands that fits better with the idioms of Markdown.

## concordion:set

The `concordion:set` command is expressed using the syntax: `#varname="value"`

which sets the variable named `varname` to the value `value`.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>{#x="1"}</td>
      <td>&lt;span concordion:set='#x'&gt;1&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{#name="Bob"}</td>
      <td>&lt;span concordion:set='#name'&gt;Bob&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{#z="1\"}"}</td>
      <td>&lt;span concordion:set='#z'&gt;1"}&lt;/span&gt;</td>
    </tr>
  </table>
</div>

## concordion:assertEquals

The `concordion:assertEquals` command is expressed using the syntax: `#expression=="value"`

which asserts that the result of evaluating _expression_ equals the value _value_.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>{#x=="1"}</td>
      <td>&lt;span concordion:assertEquals='#x'&gt;1&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{#name=="Bob"}</td>
      <td>&lt;span concordion:assertEquals='#name'&gt;Bob&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{add(#x, #y)=="3"}</td>
      <td>&lt;span concordion:assertEquals='add(#x, #y)'&gt;3&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{getGreeting()=="Hello"}</td>
      <td>&lt;span concordion:assertEquals='getGreeting()'&gt;Hello&lt;/span&gt;</td>
    </tr>
    <tr>
      <td>{greeting=="Hello"}</td>
      <td>&lt;span concordion:assertEquals='greeting'&gt;Hello&lt;/span&gt;</td>
    </tr>
  </table>
</div>
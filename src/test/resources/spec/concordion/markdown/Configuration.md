# Concordion Markdown Configuration

## Markdown Syntax Extensions
The underlying Markdown parser is [Pegdown](https://github.com/sirthias/pegdown), which allows for an [extended Markdown syntax](https://github.com/sirthias/pegdown/blob/master/README.markdown#introduction).

We are already using the TABLES and STRIKETHRU extensions to enable our syntax.

Should you wish to add further Pegdown extensions, call the method `withPegdownExtensions(int options)` where `options` is a bitmask of the required [extensions](http://www.decodified.com/pegdown/api/org/pegdown/Extensions.html).

### [No extra extensions](- "no-extra-extensions")

Without additional extensions, the following are translated as-is.

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[[not a wikilink]]</td>
      <td>[[not a wikilink]]</td>
    </tr>
    <tr>
      <td>"pretty quotes"</td>
      <td>"pretty quotes"</td>
    </tr>
  </table>
</div>
 
### [With WIKILINK, SMARTS and QUOTES extensions](- "extra-extensions")

Given the method `MarkdownExtension.withPegdownExtensions(int options)` has been [called](- "withWikilinkAndQuotes()") with `options` set to `org.pegdown.Extensions.WIKILINKS | org.pegdown.Extensions.QUOTES`, the following are now translated as shown:

<div class="example">
  <h3>Example</h3>
  <table concordion:execute="#html=translate(#md)">
    <tr>
      <th concordion:set="#md">Markdown</th>
      <th concordion:assertEquals="#html">Resulting HTML</th>
    </tr>
    <tr>
      <td>[[wikilink]]</td>
      <td>&lt;a href="./wikilink.html">wikilink&lt;/a></td>
    </tr>
    <tr>
      <td>"pretty quotes"</td>
      <td>&amp;ldquo;pretty quotes&amp;rdquo;</td>
    </tr>
  </table>
</div>



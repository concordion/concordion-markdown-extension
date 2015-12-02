# Support for Legacy Examples
The Concordion Example Command wasn't added until Concordion 2.0.  Before that, examples were placed in a `div` with the special class name `example` which the Concordion CSS knew how to style correctly.

Whichever version of Example, your version of Concordion supports, the Markdown syntax is exactly the same.However, when using the older version, the example name in the link is ignored.

`### [Header text](- "ignored")`

which creates:

```
<div class="example">
    <h3>Header text</h3>
```    

where `###` can be any level of [header](https://daringfireball.net/projects/markdown/syntax#header), using either atx or setext syntax.

<div class="example">
  <h3>Examples</h3>
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
      <![CDATA[
<div class="example"> <h4>Example 1</h4> <p>x</p></div>  
]]> 
      </td>
    </tr>
    
    <tr>
      <td>h1 example using setext-style syntax</td>
      <td>
        <pre>      
[Example 2](- "setext")
=====================================================
x
        </pre>
      </td>
      <td>
 <![CDATA[
<div class="example"> <h1>Example 2</h1> <p>x</p></div>  
]]> 
      </td>
    </tr>
    
  </table>
</div> 
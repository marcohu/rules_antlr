# ANTLR 2

## Build Rule Reference

[](ANTLR2START)
<!-- Generated with Stardoc: http://skydoc.bazel.build -->

<a name="#antlr"></a>

## antlr

<pre>
antlr(<a href="#antlr-name">name</a>, <a href="#antlr-debug">debug</a>, <a href="#antlr-deps">deps</a>, <a href="#antlr-diagnostic">diagnostic</a>, <a href="#antlr-docbook">docbook</a>, <a href="#antlr-html">html</a>, <a href="#antlr-imports">imports</a>, <a href="#antlr-language">language</a>, <a href="#antlr-package">package</a>,
      <a href="#antlr-srcs">srcs</a>, <a href="#antlr-trace">trace</a>, <a href="#antlr-traceLexer">traceLexer</a>, <a href="#antlr-traceParser">traceParser</a>, <a href="#antlr-traceTreeParser">traceTreeParser</a>)
</pre>


Runs [ANTLR 2](https://www.antlr2.org//) on a set of grammars.
    

### Attributes

<table class="params-table">
  <colgroup>
    <col class="col-param" />
    <col class="col-description" />
  </colgroup>
  <tbody>
    <tr id="antlr-name">
      <td><code>name</code></td>
      <td>
        <a href="https://bazel.build/docs/build-ref.html#name">Name</a>; required
        <p>
          A unique name for this target.
        </p>
      </td>
    </tr>
    <tr id="antlr-debug">
      <td><code>debug</code></td>
      <td>
        Boolean; optional
        <p>
          Launch the ParseView debugger upon parser invocation. Unless you have
downloaded and unzipped the debugger over the top of the standard ANTLR
distribution, the code emanating from ANTLR with this option will not
compile.
        </p>
      </td>
    </tr>
    <tr id="antlr-deps">
      <td><code>deps</code></td>
      <td>
        <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a>; optional
        <p>
          The dependencies to use. Defaults to the final ANTLR 2 release, but if you need to use a different version, you can specify the dependencies here.
        </p>
      </td>
    </tr>
    <tr id="antlr-diagnostic">
      <td><code>diagnostic</code></td>
      <td>
        Boolean; optional
        <p>
          Generate a text file from your grammar with a lot of debugging info.
        </p>
      </td>
    </tr>
    <tr id="antlr-docbook">
      <td><code>docbook</code></td>
      <td>
        Boolean; optional
        <p>
          Generate a docbook SGML file from your grammar without actions and so on. It only works for parsers, not lexers or tree parsers.
        </p>
      </td>
    </tr>
    <tr id="antlr-html">
      <td><code>html</code></td>
      <td>
        Boolean; optional
        <p>
          Generate a HTML file from your grammar without actions and so on. It only works for parsers, not lexers or tree parsers.
        </p>
      </td>
    </tr>
    <tr id="antlr-imports">
      <td><code>imports</code></td>
      <td>
        <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a>; optional
        <p>
          The grammar file to import.
        </p>
      </td>
    </tr>
    <tr id="antlr-language">
      <td><code>language</code></td>
      <td>
        String; optional
        <p>
          The code generation target language. Either Cpp, CSharp, Java or Python (case-sensitive).
        </p>
      </td>
    </tr>
    <tr id="antlr-package">
      <td><code>package</code></td>
      <td>
        String; optional
        <p>
          The enclosing namespace to use for C++.
        </p>
      </td>
    </tr>
    <tr id="antlr-srcs">
      <td><code>srcs</code></td>
      <td>
        <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a>; optional
        <p>
          The grammar files to process.
        </p>
      </td>
    </tr>
    <tr id="antlr-trace">
      <td><code>trace</code></td>
      <td>
        Boolean; optional
        <p>
          Have all rules call traceIn/traceOut.
        </p>
      </td>
    </tr>
    <tr id="antlr-traceLexer">
      <td><code>traceLexer</code></td>
      <td>
        Boolean; optional
        <p>
          Have lexer rules call traceIn/traceOut.
        </p>
      </td>
    </tr>
    <tr id="antlr-traceParser">
      <td><code>traceParser</code></td>
      <td>
        Boolean; optional
        <p>
          Have parser rules call traceIn/traceOut.
        </p>
      </td>
    </tr>
    <tr id="antlr-traceTreeParser">
      <td><code>traceTreeParser</code></td>
      <td>
        Boolean; optional
        <p>
          Have tree walker rules call traceIn/traceOut.
        </p>
      </td>
    </tr>
  </tbody>
</table>




[](ANTLR2END)

# ANTLR 4

## Build Rule Reference

[](ANTLR4START)
<!-- Generated with Stardoc: http://skydoc.bazel.build -->

<a name="#antlr"></a>

## antlr

<pre>
antlr(<a href="#antlr-name">name</a>, <a href="#antlr-atn">atn</a>, <a href="#antlr-depend">depend</a>, <a href="#antlr-deps">deps</a>, <a href="#antlr-encoding">encoding</a>, <a href="#antlr-error">error</a>, <a href="#antlr-force_atn">force_atn</a>, <a href="#antlr-imports">imports</a>, <a href="#antlr-language">language</a>,
      <a href="#antlr-layout">layout</a>, <a href="#antlr-listener">listener</a>, <a href="#antlr-log">log</a>, <a href="#antlr-long_messages">long_messages</a>, <a href="#antlr-message_format">message_format</a>, <a href="#antlr-no_listener">no_listener</a>,
      <a href="#antlr-no_visitor">no_visitor</a>, <a href="#antlr-options">options</a>, <a href="#antlr-package">package</a>, <a href="#antlr-srcs">srcs</a>, <a href="#antlr-visitor">visitor</a>)
</pre>


Runs [ANTLR 4](https://www.antlr.org//) on a set of grammars.
    

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
    <tr id="antlr-atn">
      <td><code>atn</code></td>
      <td>
        Boolean; optional
        <p>
          Generate rule augmented transition network diagrams.
        </p>
      </td>
    </tr>
    <tr id="antlr-depend">
      <td><code>depend</code></td>
      <td>
        Boolean; optional
        <p>
          Generate a list of file dependencies instead of parser and/or lexer.
        </p>
      </td>
    </tr>
    <tr id="antlr-deps">
      <td><code>deps</code></td>
      <td>
        <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a>; optional
        <p>
          The dependencies to use. Defaults to the official ANTLR 4 release, but if
you need to use a different version, you can specify the dependencies here.
        </p>
      </td>
    </tr>
    <tr id="antlr-encoding">
      <td><code>encoding</code></td>
      <td>
        String; optional
        <p>
          The grammar file encoding, e.g. euc-jp.
        </p>
      </td>
    </tr>
    <tr id="antlr-error">
      <td><code>error</code></td>
      <td>
        Boolean; optional
        <p>
          Treat warnings as errors.
        </p>
      </td>
    </tr>
    <tr id="antlr-force_atn">
      <td><code>force_atn</code></td>
      <td>
        Boolean; optional
        <p>
          Use the ATN simulator for all predictions.
        </p>
      </td>
    </tr>
    <tr id="antlr-imports">
      <td><code>imports</code></td>
      <td>
        <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a>; optional
        <p>
          The grammar and .tokens files to import. Must be all in the same directory.
        </p>
      </td>
    </tr>
    <tr id="antlr-language">
      <td><code>language</code></td>
      <td>
        String; optional
        <p>
          The code generation target language. Either Cpp, CSharp, Go, JavaScript, Java, Python2, Python3 or Swift (case-sensitive).
        </p>
      </td>
    </tr>
    <tr id="antlr-layout">
      <td><code>layout</code></td>
      <td>
        String; optional
      </td>
    </tr>
    <tr id="antlr-listener">
      <td><code>listener</code></td>
      <td>
        Boolean; optional
        <p>
          Generate parse tree listener.
        </p>
      </td>
    </tr>
    <tr id="antlr-log">
      <td><code>log</code></td>
      <td>
        Boolean; optional
        <p>
          Dump lots of logging info to antlr-timestamp.log.
        </p>
      </td>
    </tr>
    <tr id="antlr-long_messages">
      <td><code>long_messages</code></td>
      <td>
        Boolean; optional
        <p>
          Show exception details when available for errors and warnings.
        </p>
      </td>
    </tr>
    <tr id="antlr-message_format">
      <td><code>message_format</code></td>
      <td>
        String; optional
        <p>
          The output style for messages. Either antlr, gnu or vs2005.
        </p>
      </td>
    </tr>
    <tr id="antlr-no_listener">
      <td><code>no_listener</code></td>
      <td>
        Boolean; optional
        <p>
          Do not generate parse tree listener.
        </p>
      </td>
    </tr>
    <tr id="antlr-no_visitor">
      <td><code>no_visitor</code></td>
      <td>
        Boolean; optional
        <p>
          Do not generate parse tree visitor.
        </p>
      </td>
    </tr>
    <tr id="antlr-options">
      <td><code>options</code></td>
      <td>
        <a href="https://bazel.build/docs/skylark/lib/dict.html">Dictionary: String -> String</a>; optional
        <p>
          Set/override grammar-level options.
        </p>
      </td>
    </tr>
    <tr id="antlr-package">
      <td><code>package</code></td>
      <td>
        String; optional
        <p>
          The package/namespace for the generated code.
        </p>
      </td>
    </tr>
    <tr id="antlr-srcs">
      <td><code>srcs</code></td>
      <td>
        <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a>; required
        <p>
          The grammar files to process.
        </p>
      </td>
    </tr>
    <tr id="antlr-visitor">
      <td><code>visitor</code></td>
      <td>
        Boolean; optional
        <p>
          Generate parse tree visitor.
        </p>
      </td>
    </tr>
  </tbody>
</table>


<a name="#imports"></a>

## imports

<pre>
imports(<a href="#imports-folder">folder</a>)
</pre>

 Returns the grammar and token files found below the given lib directory. 

### Parameters

<table class="params-table">
  <colgroup>
    <col class="col-param" />
    <col class="col-description" />
  </colgroup>
  <tbody>
    <tr id="imports-folder">
      <td><code>folder</code></td>
      <td>
        required.
      </td>
    </tr>
  </tbody>
</table>




[](ANTLR4END)

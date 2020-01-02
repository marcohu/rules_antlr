<!-- Generated with Stardoc: http://skydoc.bazel.build -->

<a name="#antlr"></a>

## antlr

<pre>
antlr(<a href="#antlr-name">name</a>, <a href="#antlr-atn">atn</a>, <a href="#antlr-depend">depend</a>, <a href="#antlr-deps">deps</a>, <a href="#antlr-encoding">encoding</a>, <a href="#antlr-error">error</a>, <a href="#antlr-force_atn">force_atn</a>, <a href="#antlr-imports">imports</a>, <a href="#antlr-language">language</a>, <a href="#antlr-layout">layout</a>, <a href="#antlr-listener">listener</a>, <a href="#antlr-log">log</a>,
      <a href="#antlr-long_messages">long_messages</a>, <a href="#antlr-message_format">message_format</a>, <a href="#antlr-no_listener">no_listener</a>, <a href="#antlr-no_visitor">no_visitor</a>, <a href="#antlr-options">options</a>, <a href="#antlr-package">package</a>, <a href="#antlr-srcs">srcs</a>, <a href="#antlr-visitor">visitor</a>)
</pre>


Runs [ANTLR 4](https://www.antlr.org//) on a set of grammars.
    

**ATTRIBUTES**


| Name  | Description | Type | Mandatory | Default |
| --------------- | --------------- | --------------- | --------------- | --------------- |
| <a name="antlr-name"></a>name |  A unique name for this target.   | <a href="https://bazel.build/docs/build-ref.html#name">Name</a> | required |  |
| <a name="antlr-atn"></a>atn |  Generate rule augmented transition network diagrams.   | Boolean | optional | False |
| <a name="antlr-depend"></a>depend |  Generate a list of file dependencies instead of parser and/or lexer.   | Boolean | optional | False |
| <a name="antlr-deps"></a>deps |  The dependencies to use. Defaults to the official ANTLR 4 release, but if you need to use a different version, you can specify the dependencies here.   | <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a> | optional | [Label("@antlr4_tool//jar:jar"), Label("@antlr4_runtime//jar:jar"), Label("@antlr3_runtime//jar:jar"), Label("@stringtemplate4//jar:jar"), Label("@javax_json//jar:jar")] |
| <a name="antlr-encoding"></a>encoding |  The grammar file encoding, e.g. euc-jp.   | String | optional | "UTF-8" |
| <a name="antlr-error"></a>error |  Treat warnings as errors.   | Boolean | optional | False |
| <a name="antlr-force_atn"></a>force_atn |  Use the ATN simulator for all predictions.   | Boolean | optional | False |
| <a name="antlr-imports"></a>imports |  The grammar and .tokens files to import. Must be all in the same directory.   | <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a> | optional | [] |
| <a name="antlr-language"></a>language |  The code generation target language. Either Cpp, CSharp, Go, JavaScript, Java, Python2, Python3 or Swift (case-sensitive).   | String | optional | "" |
| <a name="antlr-layout"></a>layout |  -   | String | optional | "" |
| <a name="antlr-listener"></a>listener |  Generate parse tree listener.   | Boolean | optional | True |
| <a name="antlr-log"></a>log |  Dump lots of logging info to antlr-timestamp.log.   | Boolean | optional | False |
| <a name="antlr-long_messages"></a>long_messages |  Show exception details when available for errors and warnings.   | Boolean | optional | False |
| <a name="antlr-message_format"></a>message_format |  The output style for messages. Either antlr, gnu or vs2005.   | String | optional | "antlr" |
| <a name="antlr-no_listener"></a>no_listener |  Do not generate parse tree listener.   | Boolean | optional | False |
| <a name="antlr-no_visitor"></a>no_visitor |  Do not generate parse tree visitor.   | Boolean | optional | True |
| <a name="antlr-options"></a>options |  Set/override grammar-level options.   | <a href="https://bazel.build/docs/skylark/lib/dict.html">Dictionary: String -> String</a> | optional | {} |
| <a name="antlr-package"></a>package |  The package/namespace for the generated code.   | String | optional | "" |
| <a name="antlr-srcs"></a>srcs |  The grammar files to process.   | <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a> | required |  |
| <a name="antlr-visitor"></a>visitor |  Generate parse tree visitor.   | Boolean | optional | False |


<a name="#imports"></a>

## imports

<pre>
imports(<a href="#imports-folder">folder</a>)
</pre>

Returns the grammar and token files found below the given lib directory.

**PARAMETERS**


| Name  | Description | Default Value |
| --------------- | --------------- | --------------- |
| <a name="imports-folder"></a>folder |  <p align="center"> - </p>   |  none |



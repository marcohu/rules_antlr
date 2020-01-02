<!-- Generated with Stardoc: http://skydoc.bazel.build -->

<a name="#antlr"></a>

## antlr

<pre>
antlr(<a href="#antlr-name">name</a>, <a href="#antlr-debug">debug</a>, <a href="#antlr-deps">deps</a>, <a href="#antlr-diagnostic">diagnostic</a>, <a href="#antlr-docbook">docbook</a>, <a href="#antlr-html">html</a>, <a href="#antlr-imports">imports</a>, <a href="#antlr-language">language</a>, <a href="#antlr-package">package</a>, <a href="#antlr-srcs">srcs</a>, <a href="#antlr-trace">trace</a>,
      <a href="#antlr-traceLexer">traceLexer</a>, <a href="#antlr-traceParser">traceParser</a>, <a href="#antlr-traceTreeParser">traceTreeParser</a>)
</pre>


Runs [ANTLR 2](https://www.antlr2.org//) on a set of grammars.
    

**ATTRIBUTES**


| Name  | Description | Type | Mandatory | Default |
| --------------- | --------------- | --------------- | --------------- | --------------- |
| <a name="antlr-name"></a>name |  A unique name for this target.   | <a href="https://bazel.build/docs/build-ref.html#name">Name</a> | required |  |
| <a name="antlr-debug"></a>debug |  Launch the ParseView debugger upon parser invocation. Unless you have downloaded and unzipped the debugger over the top of the standard ANTLR distribution, the code emanating from ANTLR with this option will not compile.   | Boolean | optional | False |
| <a name="antlr-deps"></a>deps |  The dependencies to use. Defaults to the final ANTLR 2 release, but if you need to use a different version, you can specify the dependencies here.   | <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a> | optional | [Label("@antlr2//jar:jar")] |
| <a name="antlr-diagnostic"></a>diagnostic |  Generate a text file from your grammar with a lot of debugging info.   | Boolean | optional | False |
| <a name="antlr-docbook"></a>docbook |  Generate a docbook SGML file from your grammar without actions and so on. It only works for parsers, not lexers or tree parsers.   | Boolean | optional | False |
| <a name="antlr-html"></a>html |  Generate a HTML file from your grammar without actions and so on. It only works for parsers, not lexers or tree parsers.   | Boolean | optional | False |
| <a name="antlr-imports"></a>imports |  The grammar file to import.   | <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a> | optional | [] |
| <a name="antlr-language"></a>language |  The code generation target language. Either Cpp, CSharp, Java or Python (case-sensitive).   | String | optional | "" |
| <a name="antlr-package"></a>package |  The package/namespace for the generated code.   | String | optional | "" |
| <a name="antlr-srcs"></a>srcs |  The grammar files to process.   | <a href="https://bazel.build/docs/build-ref.html#labels">List of labels</a> | optional | [] |
| <a name="antlr-trace"></a>trace |  Have all rules call traceIn/traceOut.   | Boolean | optional | False |
| <a name="antlr-traceLexer"></a>traceLexer |  Have lexer rules call traceIn/traceOut.   | Boolean | optional | False |
| <a name="antlr-traceParser"></a>traceParser |  Have parser rules call traceIn/traceOut.   | Boolean | optional | False |
| <a name="antlr-traceTreeParser"></a>traceTreeParser |  Have tree walker rules call traceIn/traceOut.   | Boolean | optional | False |



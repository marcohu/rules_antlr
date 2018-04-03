# ANTLR Rules for Bazel

These build rules are used for processing [ANTLR](https://www.antlr.org)
grammars with [Bazel](https://bazel.build/).

<a name="toc"></a>
### Rules
Different rules are available that align with the ANTLR release streams.
  * <a href="#antlr4">antlr4</a>
  * <a href="#antlr3">antlr3</a>
  * <a href="#antlr2">antlr2</a>

<a name="setup"></a>
## Quick Setup

Add the following to your [`WORKSPACE`](https://docs.bazel.build/versions/master/build-ref.html#workspace)
file to include the external repository and load the external dependencies necessary for
the [`antlr4`](#antlr4) rule:

```python
http_archive(
    name = "rules_antlr",
    sha256 = "66e1fcf1f8b5f2daa7c09268e5a10ab136834d73f0d0a94724100958ae560763",
    strip_prefix = "rules_antlr-0.1.0",
    urls = ["https://github.com/marcohu/rules_antlr/archive/0.1.0.tar.gz"],
)

load("@rules_antlr//antlr:deps.bzl", "antlr_dependencies")
antlr_dependencies()
```

More detailed instructions can be found in the
[Setup](https://github.com/marcohu/rules_antlr/tree/master/docs/setup.md) section.

<a name="basic-example"></a>
## Basic Example

Suppose you have the following directory structure for a simple ANTLR project:

```
[workspace]/
  WORKSPACE
  HelloWorld/
    BUILD
    src/
      main/
        antlr4/
          Hello.g4
```

`HelloWorld/src/main/antlr4/Hello.g4`

```
grammar Hello;
r  : 'hello' ID;
ID : [a-z]+;
WS : [ \t\r\n]+ -> skip;
```

`HelloWorld/BUILD`

```python
package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr4")

antlr4(
    name = "generated",
    srcs = ["src/main/antlr4/Hello.g4"],
    package = "hello.world",
)

java_library(
    name = "HelloWorld",
    srcs = [":generated"],
)
```

Compiling the project generates the lexer/parser files:

```
$ bazel build //HelloWorld
INFO: Analysed target //HelloWorld:HelloWorld (0 packages loaded).
INFO: Found 1 target...
Target //HelloWorld:HelloWorld up-to-date:
  bazel-bin/HelloWorld/libHelloWorld.jar
INFO: Elapsed time: 0.940s, Critical Path: 0.76s
INFO: Build completed successfully, 4 total actions
```

The generated source files can be found in the `generated.srcjar` archive below your workspace `bazel-bin/HelloWorld` directory.

To just generate the source files you would use:

    $ bazel build //HelloWorld:generated

Refer to the [examples](https://github.com/marcohu/rules_antlr/tree/master/examples)
directory for further samples.

## Project Layout

ANTLR rules will store all generated source files in a `target-name.srcjar` zip archive below your workspace [`bazel-bin`](https://docs.bazel.build/versions/master/output_directories.html#documentation-of-the-current-bazel-output-directory-layout) folder.
Depending on the ANTLR version, there are three ways to control namespacing and directory structure for generated code, all with their pros and cons.

1. The [`package`](#antlr4.package) rule attribute ([`antlr4`](#antlr4) only). Setting the namespace via the [`package`](#antlr4.package) attribute will generate the corresponding target language specific namespacing code (where applicable) and puts the generated source files below a corresponding directory structure. To not create the directory structure, set the [`layout`](#antlr4.layout) attribute to `flat`.<br>Very expressive and allows language independent grammars, but only available with ANTLR 4, requires several runs for different namespaces, might complicate refactoring and can conflict with language specific code in `@header {...}` sections as they are mutually exclusive.

2. Language specific application code in grammar `@header {...}` section. To not create the corresponding directory structure, set the [`layout`](#antlr4.layout) attribute to `flat`.<br>Allows different namespaces to be processed in a single run and will not require changes to build files upon refactoring, but ties grammars to a specific language and can conflict with the [`package`](#antlr4.package) attribute as they are mutually exclusive.

3. The project layout ([`antlr4`](#antlr4) only). Putting your grammars below a common project directory will determine namespace and corresponding directory structure for the generated source files from the relative project path. ANTLR rules uses different defaults for the different target languages (see below), but you can define the root directory yourself via the [`layout`](#antlr4.layout) attribute.<br>Allows different namespaces to be processed in a single run without language coupling, but requires conformity to a specific (albeit configurable) project layout and the [`layout`](#antlr4.layout) attribute for certain languages.


### Common Project Directories

The [`antlr4`](#antlr4) rule supports a common directory layout to figure out namespacing from the relative directory structure. The table below lists the default paths for the different target languages. The version number at the end is optional.

| Language                 | Default Directory<span style="display:inline-block;width:4em"/>|
|--------------------------|------------------|
| C                        | `src/antlr4`     |
| Cpp                      | `src/antlr4`     |
| CSharp, CSharp2, CSharp3 | `src/antlr4`     |
| Go                       | &nbsp;           |
| Java                     | `src/main/antlr4`|
| JavaScript               | `src/antlr4`     |
| Python, Python2, Python3 | `src/antlr4`     |
| Swift                    |  &nbsp;          |

 For languages with no default, you have to set your preference with the [`layout`](#antlr4.layout) attribute.

<a name="reference"></a>
## Build Rule Reference

<a name="antlr4"></a>
## antlr4

```python
antlr4(name, deps=[], srcs=[], atn, depend, encoding, error,
       force_atn, imports=[], language, layout, listener, log,
       long_messages, message_format, no_listener, no_visitor,
       options={}, package, visitor)
```

Runs ANTLR 4 on the given grammar files.

<a name="antlr4_args"></a>
### Attributes

[](ANTLR4START)
<table class="params-table">
  <colgroup>
    <col class="col-param" />
    <col class="col-description" />
  </colgroup>
  <tbody>
    <tr id="antlr4.name">
      <td><code>name</code></td>
      <td>
        <p><code><a href="https://bazel.build/docs/build-ref.html#name">Name</a>; Required</code></p>
        <p>A unique name for this rule.</p>
      </td>
    </tr>
    <tr id="antlr4.deps">
      <td><code>deps</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Optional; Default is ['@antlr4_tool//jar', '@antlr4_runtime//jar', '@antlr3_runtime//jar', '@stringtemplate4//jar', '@javax_json//jar']</code></p>
        <p>The dependencies to use. Defaults to the official ANTLR 4 release, but if
you need to use a different version, you can specify the dependencies here.</p>
      </td>
    </tr>
    <tr id="antlr4.srcs">
      <td><code>srcs</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Required</code></p>
        <p>The grammar files to process.</p>
      </td>
    </tr>
    <tr id="antlr4.atn">
      <td><code>atn</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate rule augmented transition network diagrams.</p>
      </td>
    </tr>
    <tr id="antlr4.depend">
      <td><code>depend</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate a list of file dependencies instead of parser and/or lexer.</p>
      </td>
    </tr>
    <tr id="antlr4.encoding">
      <td><code>encoding</code></td>
      <td>
        <p><code>String; Optional; Default is 'UTF-8'</code></p>
        <p>The grammar file encoding, e.g. euc-jp.</p>
      </td>
    </tr>
    <tr id="antlr4.error">
      <td><code>error</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Treat warnings as errors.</p>
      </td>
    </tr>
    <tr id="antlr4.force_atn">
      <td><code>force_atn</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Use the ATN simulator for all predictions.</p>
      </td>
    </tr>
    <tr id="antlr4.imports">
      <td><code>imports</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Optional; Default is []</code></p>
        <p>The grammar and .tokens files to import. Must be all in the same directory.</p>
      </td>
    </tr>
    <tr id="antlr4.language">
      <td><code>language</code></td>
      <td>
        <p><code>String; Optional; Default is 'Java'</code></p>
        <p>The code generation target language. Either Cpp, CSharp, Go, JavaScript,
Java, Python2, Python3 or Swift (case-sensitive).</p>
      </td>
    </tr>
    <tr id="antlr4.layout">
      <td><code>layout</code></td>
      <td>
        <p><code>String; Optional; Default is ''</code></p>
        <p>The directory layout to match file paths against for package/namespace
detection by convention. The default depends on the target language.</p>
      </td>
    </tr>
    <tr id="antlr4.listener">
      <td><code>listener</code></td>
      <td>
        <p><code>Boolean; Optional; Default is True</code></p>
        <p>Generate parse tree listener.</p>
      </td>
    </tr>
    <tr id="antlr4.log">
      <td><code>log</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Dump lots of logging info to antlr-timestamp.log.</p>
      </td>
    </tr>
    <tr id="antlr4.long_messages">
      <td><code>long_messages</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Show exception details when available for errors and warnings.</p>
      </td>
    </tr>
    <tr id="antlr4.message_format">
      <td><code>message_format</code></td>
      <td>
        <p><code>String; Optional; Default is 'antlr'</code></p>
        <p>The output style for messages. Either antlr, gnu or vs2005.</p>
      </td>
    </tr>
    <tr id="antlr4.no_listener">
      <td><code>no_listener</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Do not generate parse tree listener.</p>
      </td>
    </tr>
    <tr id="antlr4.no_visitor">
      <td><code>no_visitor</code></td>
      <td>
        <p><code>Boolean; Optional; Default is True</code></p>
        <p>Do not generate parse tree visitor.</p>
      </td>
    </tr>
    <tr id="antlr4.options">
      <td><code>options</code></td>
      <td>
        <p><code>Dictionary mapping strings to string; Optional; Default is {}</code></p>
        <p>Set/override grammar-level options.</p>
      </td>
    </tr>
    <tr id="antlr4.package">
      <td><code>package</code></td>
      <td>
        <p><code>String; Optional; Default is ''</code></p>
        <p>The package/namespace for the generated code.</p>
      </td>
    </tr>
    <tr id="antlr4.visitor">
      <td><code>visitor</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate parse tree visitor.</p>
      </td>
    </tr>
  </tbody>
</table>

[](ANTLR4END)


<a name="antlr3"></a>
## antlr3

```python
antlr3(name, deps=[], srcs=[], debug, depend, dfa, dump, imports=[],
       message_format, nfa, profile, report, trace,
       Xconversiontimeout,  Xdbgst, Xdbgconversion, Xdfa, Xdfaverbose,
       Xgrtree, Xm,  Xmaxdfaedges, Xmaxinlinedfastates, Xmultithreaded,
       Xnfastates, Xnocollapse, Xnomergestopstates, Xnoprune,
       Xwatchconversion)
```

Runs ANTLR 3 on the given grammar files.

<a name="antlr3_args"></a>
### Attributes

[](ANTLR3START)
<table class="params-table">
  <colgroup>
    <col class="col-param" />
    <col class="col-description" />
  </colgroup>
  <tbody>
    <tr id="antlr3.name">
      <td><code>name</code></td>
      <td>
        <p><code><a href="https://bazel.build/docs/build-ref.html#name">Name</a>; Required</code></p>
        <p>A unique name for this rule.</p>
      </td>
    </tr>
    <tr id="antlr3.deps">
      <td><code>deps</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Optional; Default is ['@antlr3_runtime//jar', '@antlr3_tool//jar', '@stringtemplate4//jar']</code></p>
        <p>The dependencies to use. Defaults to the most recent ANTLR 3 release,
but if you need to use a different version, you can specify the
dependencies here.</p>
      </td>
    </tr>
    <tr id="antlr3.srcs">
      <td><code>srcs</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Required</code></p>
        <p>The grammar files to process.</p>
      </td>
    </tr>
    <tr id="antlr3.Xconversiontimeout">
      <td><code>Xconversiontimeout</code></td>
      <td>
        <p><code>Integer; Optional; Default is 0</code></p>
        <p>Set NFA conversion timeout for each decision.</p>
      </td>
    </tr>
    <tr id="antlr3.Xdbgconversion">
      <td><code>Xdbgconversion</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Dump lots of info during NFA conversion.</p>
      </td>
    </tr>
    <tr id="antlr3.Xdbgst">
      <td><code>Xdbgst</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Put tags at start/stop of all templates in output.</p>
      </td>
    </tr>
    <tr id="antlr3.Xdfa">
      <td><code>Xdfa</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Print DFA as text.</p>
      </td>
    </tr>
    <tr id="antlr3.Xdfaverbose">
      <td><code>Xdfaverbose</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate DFA states in DOT with NFA configs.</p>
      </td>
    </tr>
    <tr id="antlr3.Xgrtree">
      <td><code>Xgrtree</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Print the grammar AST.</p>
      </td>
    </tr>
    <tr id="antlr3.Xm">
      <td><code>Xm</code></td>
      <td>
        <p><code>Integer; Optional; Default is 0</code></p>
        <p>Max number of rule invocations during conversion.</p>
      </td>
    </tr>
    <tr id="antlr3.Xmaxdfaedges">
      <td><code>Xmaxdfaedges</code></td>
      <td>
        <p><code>Integer; Optional; Default is 0</code></p>
        <p>Max "comfortable" number of edges for single DFA state.</p>
      </td>
    </tr>
    <tr id="antlr3.Xmaxinlinedfastates">
      <td><code>Xmaxinlinedfastates</code></td>
      <td>
        <p><code>Integer; Optional; Default is 0</code></p>
        <p>Max DFA states before table used rather than inlining.</p>
      </td>
    </tr>
    <tr id="antlr3.Xmultithreaded">
      <td><code>Xmultithreaded</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Run the analysis in 2 threads.</p>
      </td>
    </tr>
    <tr id="antlr3.Xnfastates">
      <td><code>Xnfastates</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>For nondeterminisms, list NFA states for each path.</p>
      </td>
    </tr>
    <tr id="antlr3.Xnocollapse">
      <td><code>Xnocollapse</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Collapse incident edges into DFA states.</p>
      </td>
    </tr>
    <tr id="antlr3.Xnomergestopstates">
      <td><code>Xnomergestopstates</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Do not merge stop states.</p>
      </td>
    </tr>
    <tr id="antlr3.Xnoprune">
      <td><code>Xnoprune</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Do not test EBNF block exit branches.</p>
      </td>
    </tr>
    <tr id="antlr3.Xwatchconversion">
      <td><code>Xwatchconversion</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Print a message for each NFA before converting.</p>
      </td>
    </tr>
    <tr id="antlr3.debug">
      <td><code>debug</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate a parser that emits debugging events.</p>
      </td>
    </tr>
    <tr id="antlr3.depend">
      <td><code>depend</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate file dependencies; don't actually run antlr.</p>
      </td>
    </tr>
    <tr id="antlr3.dfa">
      <td><code>dfa</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate a DFA for each decision point.</p>
      </td>
    </tr>
    <tr id="antlr3.dump">
      <td><code>dump</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Print out the grammar without actions.</p>
      </td>
    </tr>
    <tr id="antlr3.imports">
      <td><code>imports</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Optional; Default is []</code></p>
        <p>The grammar and .tokens files to import. Must be all in the same directory.</p>
      </td>
    </tr>
    <tr id="antlr3.message_format">
      <td><code>message_format</code></td>
      <td>
        <p><code>String; Optional; Default is ''</code></p>
        <p>Specify output style for messages.</p>
      </td>
    </tr>
    <tr id="antlr3.nfa">
      <td><code>nfa</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate an NFA for each rule.</p>
      </td>
    </tr>
    <tr id="antlr3.profile">
      <td><code>profile</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate a parser that computes profiling information.</p>
      </td>
    </tr>
    <tr id="antlr3.report">
      <td><code>report</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Print out a report about the grammar(s) processed.</p>
      </td>
    </tr>
    <tr id="antlr3.trace">
      <td><code>trace</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate a parser with trace output. If the default output is not
enough, you can override the traceIn and traceOut methods.</p>
      </td>
    </tr>
  </tbody>
</table>

[](ANTLR3END)


<a name="antlr2"></a>
## antlr2

```python
antlr2(name, deps=[], srcs=[], debug, diagnostic, docbook, html,
       imports=[], traceLexer, traceParser, traceTreeParser)
```

Runs ANTLR 2 on the given grammar files.

<a name="antlr2_args"></a>
### Attributes

[](ANTLR2START)
<table class="params-table">
  <colgroup>
    <col class="col-param" />
    <col class="col-description" />
  </colgroup>
  <tbody>
    <tr id="antlr2.name">
      <td><code>name</code></td>
      <td>
        <p><code><a href="https://bazel.build/docs/build-ref.html#name">Name</a>; Required</code></p>
        <p>A unique name for this rule.</p>
      </td>
    </tr>
    <tr id="antlr2.deps">
      <td><code>deps</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Optional; Default is ['@antlr2//jar']</code></p>
        <p>The dependencies to use. Defaults to the final ANTLR 2 release, but if you
need to use a different version, you can specify the dependencies here.</p>
      </td>
    </tr>
    <tr id="antlr2.srcs">
      <td><code>srcs</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Optional; Default is []</code></p>
        <p>The grammar files to process.</p>
      </td>
    </tr>
    <tr id="antlr2.debug">
      <td><code>debug</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Launch the ParseView debugger upon parser invocation. Unless you have
downloaded and unzipped the debugger over the top of the standard ANTLR
distribution, the code emanating from ANTLR with this option will not
compile.</p>
      </td>
    </tr>
    <tr id="antlr2.diagnostic">
      <td><code>diagnostic</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate a text file from your grammar with a lot of debugging info.</p>
      </td>
    </tr>
    <tr id="antlr2.docbook">
      <td><code>docbook</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate a docbook SGML file from your grammar without actions and so
on. It only works for parsers, not lexers or tree parsers.</p>
      </td>
    </tr>
    <tr id="antlr2.html">
      <td><code>html</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Generate a HTML file from your grammar without actions and so on. It only
works for parsers, not lexers or tree parsers.</p>
      </td>
    </tr>
    <tr id="antlr2.imports">
      <td><code>imports</code></td>
      <td>
        <p><code>List of <a href="https://bazel.build/docs/build-ref.html#labels">labels</a>; Optional; Default is []</code></p>
        <p>The grammar file to import.</p>
      </td>
    </tr>
    <tr id="antlr2.trace">
      <td><code>trace</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Have all rules call traceIn/traceOut.</p>
      </td>
    </tr>
    <tr id="antlr2.traceLexer">
      <td><code>traceLexer</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Have lexer rules call traceIn/traceOut.</p>
      </td>
    </tr>
    <tr id="antlr2.traceParser">
      <td><code>traceParser</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Have parser rules call traceIn/traceOut.</p>
      </td>
    </tr>
    <tr id="antlr2.traceTreeParser">
      <td><code>traceTreeParser</code></td>
      <td>
        <p><code>Boolean; Optional; Default is False</code></p>
        <p>Have tree walker rules call traceIn/traceOut.</p>
      </td>
    </tr>
  </tbody>
</table>

[](ANTLR2END)

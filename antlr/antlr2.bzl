"""Rules for ANTLR 2."""

def _generate(ctx):
    """ Generates the source files. """

    if not ctx.files.srcs:
        fail("No grammars provided, either add the grammars attribute or check your filespec", attr="srcs")

    args = ctx.actions.args()

    if ctx.attr.debug:
        args.add("-debug")

    if ctx.attr.html:
        args.add("-html")

    if ctx.attr.docbook:
        args.add("-docbook")

    if ctx.attr.diagnostic:
        args.add("-diagnostic")

    if ctx.attr.imports:
        args.add("-glib")
        args.add(";".join([x.path for x in ctx.files.imports]))

    output_dir = ctx.configuration.genfiles_dir.path
    args.add("-o")
    args.add(output_dir)

    if ctx.attr.trace:
        args.add("-trace")

    if ctx.attr.traceParser:
        args.add("-traceParser")

    if ctx.attr.traceLexer:
        args.add("-traceLexer")

    if ctx.attr.traceTreeParser:
        args.add("-traceTreeParser")

    srcjar = ctx.outputs.src_jar
    tool_inputs, _, input_manifests=ctx.resolve_command(tools=ctx.attr.deps + [ctx.attr._tool])

    ctx.actions.run(
        arguments = [args],
        inputs = ctx.files.srcs + ctx.files.imports + tool_inputs,
        outputs = [srcjar],
        mnemonic = "ANTLR2",
        executable = ctx.executable._tool,
        env = {
            "ANTLR_VERSION": "2",
            "GRAMMARS": ",".join([f.path for f in ctx.files.srcs]),
            "OUTPUT_DIRECTORY": output_dir,
            "SRC_JAR": srcjar.path,
            "TOOL_CLASSPATH": ",".join([f.path for f in tool_inputs]),
        },
        input_manifests = input_manifests,
        progress_message = "Processing ANTLR 2 grammars",
    )


antlr2 = rule(
    implementation = _generate,
    attrs = {
        "debug":            attr.bool(default=False),
        "deps":             attr.label_list(default=[Label("@antlr2//jar")]),
        "diagnostic":       attr.bool(default=False),
        "docbook":          attr.bool(default=False),
        "html":             attr.bool(default=False),
        "imports":          attr.label_list(allow_files=True),
        "srcs":             attr.label_list(allow_files=True),
        "trace":            attr.bool(default=False),
        "traceLexer":       attr.bool(default=False),
        "traceParser":      attr.bool(default=False),
        "traceTreeParser":  attr.bool(default=False),
        "_tool":            attr.label(
                              executable=True,
                              cfg="host",
                              default=Label("@rules_antlr//src/main/java/org/antlr/bazel")),
    },
    outputs = {
        "src_jar": "%{name}.srcjar",
    },
)
"""Runs [ANTLR 2](http://www.antlr2.org//) on a set of grammars.

Args:
    debug:              Launch the ParseView debugger upon parser invocation. Unless you have
                        downloaded and unzipped the debugger over the top of the standard ANTLR
                        distribution, the code emanating from ANTLR with this option will not
                        compile.
    deps:               The dependencies to use. Defaults to the final ANTLR 2 release, but if you
                        need to use a different version, you can specify the dependencies here.
    diagnostic:         Generate a text file from your grammar with a lot of debugging info.
    docbook:            Generate a docbook SGML file from your grammar without actions and so
                        on. It only works for parsers, not lexers or tree parsers.
    html:               Generate a HTML file from your grammar without actions and so on. It only
                        works for parsers, not lexers or tree parsers.
    imports:            The grammar file to import.
    srcs:               The grammar files to process.
    trace:              Have all rules call traceIn/traceOut.
    traceLexer:         Have lexer rules call traceIn/traceOut.
    traceParser:        Have parser rules call traceIn/traceOut.
    traceTreeParser:    Have tree walker rules call traceIn/traceOut.

Outputs:
    name.srcjar:        The .srcjar with the generated files.
"""

"""Rules for ANTLR 2."""

load(":impl.bzl",
    _antlr = "antlr",
    _headers = "headers",
    _sources = "sources")

sources = _sources
headers = _headers


def _generate(ctx):
    return _antlr("2", ctx, _args)


def _args(ctx, output_dir):
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

    return args


antlr = rule(
    implementation = _generate,
    doc = """
Runs [ANTLR 2](https://www.antlr2.org//) on a set of grammars.
    """,
    attrs = {
        "debug":            attr.bool(default=False, doc="""
Launch the ParseView debugger upon parser invocation. Unless you have
downloaded and unzipped the debugger over the top of the standard ANTLR
distribution, the code emanating from ANTLR with this option will not
compile.
"""),
        "deps":             attr.label_list(default=[Label("@antlr2//jar")], doc="The dependencies to use. Defaults to the final ANTLR 2 release, but if you need to use a different version, you can specify the dependencies here."),
        "diagnostic":       attr.bool(default=False, doc="Generate a text file from your grammar with a lot of debugging info."),
        "docbook":          attr.bool(default=False, doc="Generate a docbook SGML file from your grammar without actions and so on. It only works for parsers, not lexers or tree parsers."),
        "html":             attr.bool(default=False, doc="Generate a HTML file from your grammar without actions and so on. It only works for parsers, not lexers or tree parsers."),
        "imports":          attr.label_list(allow_files=True, doc="The grammar file to import."),
        "language":         attr.string(doc="The code generation target language. Either Cpp, CSharp, Java or Python (case-sensitive)."),
        "package":          attr.string(doc="The package/namespace for the generated code."),
        "srcs":             attr.label_list(allow_files=True, doc="The grammar files to process."),
        "trace":            attr.bool(default=False, doc="Have all rules call traceIn/traceOut."),
        "traceLexer":       attr.bool(default=False, doc="Have lexer rules call traceIn/traceOut."),
        "traceParser":      attr.bool(default=False, doc="Have parser rules call traceIn/traceOut."),
        "traceTreeParser":  attr.bool(default=False, doc="Have tree walker rules call traceIn/traceOut."),
        "_tool":            attr.label(
                              executable=True,
                              cfg="host",
                              default=Label("@rules_antlr//src/main/java/org/antlr/bazel")),
    },
)


"""Rules for ANTLR 4."""

load(":impl.bzl",
    _antlr = "antlr",
    _headers = "headers",
    _sources = "sources",
    _lib_dir = "lib_dir")

sources = _sources
headers = _headers


def imports(folder):
    """ Returns the grammar and token files found below the given lib directory. """
    return (native.glob(["{0}/*.g4".format(folder)]) +
        native.glob(["{0}/*.tokens".format(folder)]))


def _generate(ctx):
    return _antlr("4", ctx, _args)


def _args(ctx, output_dir):
    args = ctx.actions.args()

    if ctx.attr.atn:
        args.add("-atn")

    if ctx.attr.depend:
        args.add("-depend")

    args.add("-encoding")
    args.add(ctx.attr.encoding)

    if ctx.attr.force_atn:
        args.add("-force-atn")

    if ctx.attr.error:
        args.add("-Werror")

    lib = _lib_dir(ctx.files.imports)
    if lib:
        args.add("-lib")
        args.add(lib)

    if ctx.attr.listener:
        args.add("-listener")

    if ctx.attr.log:
        args.add("-Xlog")

    if ctx.attr.language:
        args.add("-Dlanguage={0}".format(ctx.attr.language))

    if ctx.attr.long_messages:
        args.add("-long-messages")

    args.add("-message-format")
    args.add(ctx.attr.message_format)

    if ctx.attr.no_listener:
        args.add("-no-listener")

    if ctx.attr.no_visitor:
        args.add("-no-visitor")

    args.add("-o")
    args.add(output_dir)

    for key in ctx.attr.options:
        args.add("-D{0}={1}".format(key, ctx.attr.options[key]))

    if ctx.attr.package:
        args.add("-package")
        args.add(ctx.attr.package)

    if ctx.attr.visitor:
        args.add("-visitor")

    return args


antlr = rule(
    implementation = _generate,
    doc = """
Runs [ANTLR 4](https://www.antlr.org//) on a set of grammars.
    """,
    attrs = {
        "atn":            attr.bool(default=False, doc="Generate rule augmented transition network diagrams."),
        "depend":         attr.bool(default=False, doc="Generate a list of file dependencies instead of parser and/or lexer."),
        "deps":           attr.label_list(default=[
                              Label("@antlr4_tool//jar"),
                              Label("@antlr4_runtime//jar"),
                              Label("@antlr3_runtime//jar"),
                              Label("@stringtemplate4//jar"),
                              Label("@javax_json//jar"),
                          ], doc="""
The dependencies to use. Defaults to the official ANTLR 4 release, but if
you need to use a different version, you can specify the dependencies here.
                        """),
        "encoding":       attr.string(default="UTF-8", doc="The grammar file encoding, e.g. euc-jp."),
        "error":          attr.bool(default=False, doc="Treat warnings as errors."),
        "force_atn":      attr.bool(default=False, doc="Use the ATN simulator for all predictions."),
        "imports":        attr.label_list(allow_files=True, doc="The grammar and .tokens files to import. Must be all in the same directory."),
        "language":       attr.string(doc="The code generation target language. Either Cpp, CSharp, Go, JavaScript, Java, Python2, Python3 or Swift (case-sensitive)."),
        "layout":         attr.string(doc=""),
        "listener":       attr.bool(default=True, doc="Generate parse tree listener."),
        "log":            attr.bool(default=False, doc="Dump lots of logging info to antlr-timestamp.log."),
        "long_messages":  attr.bool(default=False, doc="Show exception details when available for errors and warnings."),
        "message_format": attr.string(default="antlr", doc="The output style for messages. Either antlr, gnu or vs2005."),
        "no_listener":    attr.bool(default=False, doc="Do not generate parse tree listener."),
        "no_visitor":     attr.bool(default=True, doc="Do not generate parse tree visitor."),
        "options":        attr.string_dict(doc="Set/override grammar-level options."),
        "package":        attr.string(doc="The package/namespace for the generated code."),
        "srcs":           attr.label_list(allow_files=True, mandatory=True, doc="The grammar files to process."),
        "visitor":        attr.bool(default=False, doc="Generate parse tree visitor."),
        "_tool":          attr.label(
                              executable=True,
                              cfg="host",
                              default=Label("@rules_antlr//src/main/java/org/antlr/bazel")),
    },
)
""" Runs [ANTLR 4](https://www.antlr.org//) on a set of grammars.

Args:
    atn:                Generate rule augmented transition network diagrams.
    depend:             Generate a list of file dependencies instead of parser and/or lexer.
    deps:               The dependencies to use. Defaults to the official ANTLR 4 release, but if
                        you need to use a different version, you can specify the dependencies here.
    encoding:           The grammar file encoding, e.g. euc-jp.
    error:              Treat warnings as errors.
    force_atn:          Use the ATN simulator for all predictions.
    imports:            The grammar and .tokens files to import. Must be all in the same directory.
    language:           The code generation target language. Either Cpp, CSharp, Go, JavaScript,
                        Java, Python2, Python3 or Swift (case-sensitive).
    layout:             The directory layout to match file paths against for package/namespace
                        detection by convention. The default depends on the target language.
    listener:           Generate parse tree listener.
    log:                Dump lots of logging info to antlr-timestamp.log.
    long_messages:      Show exception details when available for errors and warnings.
    message_format:     The output style for messages. Either antlr, gnu or vs2005.
    no_listener:        Do not generate parse tree listener.
    no_visitor:         Do not generate parse tree visitor.
    options:            Set/override grammar-level options.
    package:            The package/namespace for the generated code.
    srcs:               The grammar files to process.
    visitor:            Generate parse tree visitor.

Outputs:
    name.srcjar:        The .srcjar with the generated files.
"""


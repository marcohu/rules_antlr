"""Rules for ANTLR 3."""

load(":impl.bzl",
    _antlr = "antlr",
    _headers = "headers",
    _sources = "sources",
    _lib_dir = "lib_dir")

sources = _sources
headers = _headers


def imports(folder):
    """ Returns the grammar and token files found below the given lib directory. """
    return (native.glob(["{0}/*.g".format(folder)]) +
        native.glob(["{0}/*.g3".format(folder)]) +
        native.glob(["{0}/*.tokens".format(folder)]))


def _generate(ctx):
    return _antlr("3", ctx, _args)


def _args(ctx, output_dir):
    args = ctx.actions.args()

    if ctx.attr.debug:
        args.add("-debug")

    if ctx.attr.depend:
        args.add("-depend")

    if ctx.attr.dfa:
        args.add("-dfa")

    if ctx.attr.dump:
        args.add("-dump")

    if ctx.attr.language:
        args.add("-language")
        args.add(ctx.attr.language)

    lib = _lib_dir(ctx.files.imports)
    if lib:
        args.add("-lib")
        args.add(lib)

    args.add("-make")

    if ctx.attr.message_format:
        args.add("-message-format")
        args.add(ctx.attr.message_format)

    if ctx.attr.nfa:
        args.add("-nfa")

    args.add("-o")
    args.add(output_dir)

    if ctx.attr.profile:
        args.add("-profile")

    if ctx.attr.report:
        args.add("-report")

    if ctx.attr.trace:
        args.add("-trace")

    if ctx.attr.Xconversiontimeout:
        args.add("-Xconversiontimeout")
        args.add(ctx.attr.Xconversiontimeout)

    if ctx.attr.Xdbgconversion:
        args.add("-Xdbgconversion")

    if ctx.attr.Xdbgst:
        args.add("-XdbgST")

    if ctx.attr.Xdfa:
        args.add("-Xdfa")

    if ctx.attr.Xdfaverbose:
        args.add("-Xdfaverbose")

    if ctx.attr.Xgrtree:
        args.add("-Xgrtree")

    if ctx.attr.Xm:
        args.add("-Xm")
        args.add(ctx.attr.Xm)

    if ctx.attr.Xmaxdfaedges:
        args.add("-Xmaxdfaedges")
        args.add(ctx.attr.Xmaxdfaedges)

    if ctx.attr.Xmaxinlinedfastates:
        args.add("-Xmaxinlinedfastates")
        args.add(ctx.attr.Xmaxinlinedfastates)

    if ctx.attr.Xmultithreaded:
        args.add("-Xmultithreaded")

    if ctx.attr.Xnfastates:
        args.add("-Xnfastates")

    if ctx.attr.Xnocollapse:
        args.add("-Xnocollapse")

    if ctx.attr.Xnomergestopstates:
        args.add("-Xnomergestopstates")

    if ctx.attr.Xnoprune:
        args.add("-Xnoprune")

    if ctx.attr.XsaveLexer:
        args.add("-XsaveLexer")

    if ctx.attr.Xwatchconversion:
        args.add("-Xwatchconversion")

    return args


antlr = rule(
    implementation = _generate,
    doc = "Runs [ANTLR 3](https://www.antlr3.org//) on a set of grammars.",
    attrs = {
        "debug":                attr.bool(default=False, doc="Generate a parser that emits debugging events."),
        "depend":               attr.bool(default=False, doc="Generate file dependencies; don't actually run antlr."),
        "deps":                 attr.label_list(default=[
                                    Label("@antlr3_runtime//jar"),
                                    Label("@antlr3_tool//jar"),
                                    Label("@stringtemplate4//jar"),
                                ], doc="""
The dependencies to use. Defaults to the most recent ANTLR 3 release,
but if you need to use a different version, you can specify the
dependencies here.
"""),
        "dfa":                  attr.bool(default=False, doc="Generate a DFA for each decision point."),
        "dump":                 attr.bool(default=False, doc="Print out the grammar without actions."),
        "imports":              attr.label_list(allow_files=True, doc="The grammar and .tokens files to import. Must be all in the same directory."),
        "language":             attr.string(doc="The code generation target language. Either C, Cpp, CSharp2, CSharp3, JavaScript, Java, ObjC, Python, Python3 or Ruby (case-sensitive)."),
        "message_format":       attr.string(doc="Specify output style for messages."),
        "nfa":                  attr.bool(default=False, doc="Generate an NFA for each rule."),
        "package":              attr.string(doc="The package/namespace for the generated code."),
        "profile":              attr.bool(default=False, doc="Generate a parser that computes profiling information."),
        "report":               attr.bool(default=False, doc="Print out a report about the grammar(s) processed."),
        "srcs":                 attr.label_list(allow_files=True, mandatory=True, doc="The grammar files to process."),
        "trace":                attr.bool(default=False, doc="Generate a parser with trace output. If the default output is not enough, you can override the traceIn and traceOut methods."),
        "Xconversiontimeout":   attr.int(doc="Set NFA conversion timeout for each decision."),
        "Xdbgconversion":       attr.bool(default=False, doc="Dump lots of info during NFA conversion."),
        "Xdbgst":               attr.bool(default=False, doc="Put tags at start/stop of all templates in output."),
        "Xdfa":                 attr.bool(default=False, doc="Print DFA as text."),
        "Xdfaverbose":          attr.bool(default=False, doc="Generate DFA states in DOT with NFA configs."),
        "Xgrtree":              attr.bool(default=False, doc="Print the grammar AST."),
        "Xm":                   attr.int(doc="Max number of rule invocations during conversion."),
        "Xmaxdfaedges":         attr.int(doc="Max &quot;comfortable&quot; number of edges for single DFA state."),
        "Xmaxinlinedfastates":  attr.int(doc="Max DFA states before table used rather than inlining."),
        "Xminswitchalts":       attr.int(doc="Don't generate switch() statements for dfas smaller than given number."),
        "Xmultithreaded":       attr.bool(default=False, doc="Run the analysis in 2 threads."),
        "Xnfastates":           attr.bool(default=False, doc="For nondeterminisms, list NFA states for each path."),
        "Xnocollapse":          attr.bool(default=False, doc="Collapse incident edges into DFA states."),
        "Xnoprune":             attr.bool(default=False, doc="Do not test EBNF block exit branches."),
        "Xnomergestopstates":   attr.bool(default=False, doc="Max DFA states before table used rather than inlining."),
        "XsaveLexer":           attr.bool(default=False, doc="For nondeterminisms, list NFA states for each path."),
        "Xwatchconversion":     attr.bool(default=False, doc="Don't delete temporary lexers generated from combined grammars."),
        "_tool":          attr.label(
                              executable=True,
                              cfg="host",
                              default=Label("@rules_antlr//src/main/java/org/antlr/bazel")),
    },
)


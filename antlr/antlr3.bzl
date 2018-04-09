"""Rules for ANTLR 3."""

def imports(folder):
    """ Returns the grammar and token files found below the given lib directory. """
    return (native.glob(["{0}/*.g".format(folder)]) +
        native.glob(["{0}/*.g3".format(folder)]) +
        native.glob(["{0}/*.tokens".format(folder)]))


def _get_lib_dir(imports):
    """ Determines the directory that contains the given imports. """
    lib = {}
    for resource in imports:
        lib[resource.path.replace("/" + resource.basename, "")] = None
    count = len(lib)
    # the lib directory does not allow nested directories
    if count > 1:
        fail("All imports must be located in the same directory, but found {}".format(lib))
    return lib.keys()[0] if count == 1 else None;


def _generate(ctx):
    """ Generates the source files. """

    if not ctx.files.srcs:
        fail("No grammars provided, either add the srcs attribute or check your filespec", attr="srcs")

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

    lib = _get_lib_dir(ctx.files.imports)
    if lib:
        args.add("-lib")
        args.add(lib)

    args.add("-make")

    if ctx.attr.message_format:
        args.add("-message-format")
        args.add(ctx.attr.message_format)

    if ctx.attr.nfa:
        args.add("-nfa")

    output_dir = ctx.configuration.genfiles_dir.path
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

    if ctx.attr.Xwatchconversion:
        args.add("-Xwatchconversion")

    srcjar = ctx.outputs.src_jar
    tool_inputs, _, input_manifests=ctx.resolve_command(tools=ctx.attr.deps + [ctx.attr._tool])

    ctx.actions.run(
        arguments = [args],
        inputs = ctx.files.srcs + ctx.files.imports + tool_inputs,
        outputs = [srcjar],
        mnemonic = "ANTLR3",
        executable = ctx.executable._tool,
        env = {
            "ANTLR_VERSION": "3",
            "GRAMMARS": ",".join([f.path for f in ctx.files.srcs]),
            "OUTPUT_DIRECTORY": output_dir,
            "SRC_JAR": srcjar.path,
            "TOOL_CLASSPATH": ",".join([f.path for f in tool_inputs]),
        },
        input_manifests = input_manifests,
        progress_message = "Processing ANTLR 3 grammars",
    )


antlr3 = rule(
    implementation = _generate,
    attrs = {
        "debug":                attr.bool(default=False),
        "depend":               attr.bool(default=False),
        "deps":                 attr.label_list(default=[
                                    Label("@antlr3_runtime//jar"),
                                    Label("@antlr3_tool//jar"),
                                    Label("@stringtemplate4//jar"),
                                ]),
        "dfa":                  attr.bool(default=False),
        "dump":                 attr.bool(default=False),
        "imports":              attr.label_list(allow_files=True),
        "language":             attr.string(default="Java"),
        "message_format":       attr.string(),
        "nfa":                  attr.bool(default=False),
        "profile":              attr.bool(default=False),
        "report":               attr.bool(default=False),
        "srcs":                 attr.label_list(allow_files=True, mandatory=True),
        "trace":                attr.bool(default=False),
        "Xconversiontimeout":   attr.int(),
        "Xdbgconversion":       attr.bool(default=False),
        "Xdbgst":               attr.bool(default=False),
        "Xdfa":                 attr.bool(default=False),
        "Xdfaverbose":          attr.bool(default=False),
        "Xgrtree":              attr.bool(default=False),
        "Xm":                   attr.int(),
        "Xmaxdfaedges":         attr.int(),
        "Xmaxinlinedfastates":  attr.int(),
        "Xmultithreaded":       attr.bool(default=False),
        "Xnfastates":           attr.bool(default=False),
        "Xnocollapse":          attr.bool(default=False),
        "Xnoprune":             attr.bool(default=False),
        "Xnomergestopstates":   attr.bool(default=False),
        "Xwatchconversion":     attr.bool(default=False),
        "_tool":          attr.label(
                              executable=True,
                              cfg="host",
                              default=Label("@rules_antlr//src/main/java/org/antlr/bazel")),
    },
    outputs = {
        "src_jar": "%{name}.srcjar",
    },
)
""" Runs [ANTLR 3](https://www.antlr.org//) on a set of grammars.

Args:
    debug:                  Generate a parser that emits debugging events.
    depend:                 Generate file dependencies; don't actually run antlr.
    deps:                   The dependencies to use. Defaults to the most recent ANTLR 3 release,
                            but if you need to use a different version, you can specify the
                            dependencies here.
    dfa:                    Generate a DFA for each decision point.
    imports:                The grammar and .tokens files to import. Must be all in the same directory.
    language:               The code generation target language. Either C, Cpp, CSharp2, CSharp3,
                            JavaScript, Java, ObjC, Python, Python3 or Ruby (case-sensitive).
    message_format:         Specify output style for messages.
    nfa:                    Generate an NFA for each rule.
    dump:                   Print out the grammar without actions.
    profile:                Generate a parser that computes profiling information.
    report:                 Print out a report about the grammar(s) processed.
    srcs:                   The grammar files to process.
    trace:                  Generate a parser with trace output. If the default output is not
                            enough, you can override the traceIn and traceOut methods.
    Xgrtree:                Print the grammar AST.
    Xdfa:                   Print DFA as text.
    Xnoprune:               Do not test EBNF block exit branches.
    Xnocollapse:            Collapse incident edges into DFA states.
    Xdbgconversion:         Dump lots of info during NFA conversion.
    Xmultithreaded:         Run the analysis in 2 threads.
    Xnomergestopstates:     Do not merge stop states.
    Xdfaverbose:            Generate DFA states in DOT with NFA configs.
    Xwatchconversion:       Print a message for each NFA before converting.
    Xdbgst:                 Put tags at start/stop of all templates in output.
    Xm:                     Max number of rule invocations during conversion.
    Xmaxdfaedges:           Max "comfortable" number of edges for single DFA state.
    Xconversiontimeout:     Set NFA conversion timeout for each decision.
    Xmaxinlinedfastates:    Max DFA states before table used rather than inlining.
    Xnfastates:             For nondeterminisms, list NFA states for each path.

Outputs:
    name.srcjar:            The .srcjar with the generated files.
"""

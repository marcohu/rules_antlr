"""Rules for ANTLR 4."""

def imports(folder):
    """ Returns the grammar and token files found below the given lib directory. """
    return (native.glob(["{0}/*.g4".format(folder)]) +
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

    lib = _get_lib_dir(ctx.files.imports)
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

    output_dir = ctx.configuration.genfiles_dir.path
    args.add("-o")
    args.add(output_dir)

    for key in ctx.attr.options:
        args.add("-D{0}={1}".format(key, ctx.attr.options[key]))

    if ctx.attr.package:
        args.add("-package")
        args.add(ctx.attr.package)

    if ctx.attr.visitor:
        args.add("-visitor")

    srcjar = ctx.outputs.src_jar
    tool_inputs, _, input_manifests=ctx.resolve_command(tools=ctx.attr.deps + [ctx.attr._tool])

    ctx.actions.run(
        arguments = [args],
        inputs = ctx.files.srcs + ctx.files.imports,
        outputs = [srcjar],
        mnemonic = "ANTLR4",
        executable = ctx.executable._tool,
        env = {
            "ANTLR_VERSION": "4",
            "ENCODING": ctx.attr.encoding,
            "GRAMMARS": ",".join([f.path for f in ctx.files.srcs]),
            "OUTPUT_DIRECTORY": output_dir,
            "PACKAGE_NAME": ctx.attr.package,
            "DIRECTORY_LAYOUT": ctx.attr.layout,
            "SRC_JAR": srcjar.path,
            "TARGET_LANGUAGE": ctx.attr.language,
            "TOOL_CLASSPATH": ",".join([f.path for f in tool_inputs]),
        },
        input_manifests = input_manifests,
        progress_message = "Processing ANTLR 4 grammars",
        tools = tool_inputs
    )


antlr4 = rule(
    implementation = _generate,
    attrs = {
        "atn":            attr.bool(default=False),
        "depend":         attr.bool(default=False),
        "deps":           attr.label_list(default=[
                              Label("@antlr4_tool//jar"),
                              Label("@antlr4_runtime//jar"),
                              Label("@antlr3_runtime//jar"),
                              Label("@stringtemplate4//jar"),
                              Label("@javax_json//jar"),
                          ]),
        "encoding":       attr.string(default="UTF-8"),
        "error":          attr.bool(default=False),
        "force_atn":      attr.bool(default=False),
        "imports":        attr.label_list(allow_files=True),
        "language":       attr.string(),
        "layout":         attr.string(),
        "listener":       attr.bool(default=True),
        "log":            attr.bool(default=False),
        "long_messages":  attr.bool(default=False),
        "message_format": attr.string(default="antlr"),
        "no_listener":    attr.bool(default=False),
        "no_visitor":     attr.bool(default=True),
        "options":        attr.string_dict(),
        "package":        attr.string(),
        "srcs":           attr.label_list(allow_files=True, mandatory=True),
        "visitor":        attr.bool(default=False),
        "_tool":          attr.label(
                              executable=True,
                              cfg="host",
                              default=Label("@rules_antlr//src/main/java/org/antlr/bazel")),
    },
    outputs = {
        "src_jar": "%{name}.srcjar",
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

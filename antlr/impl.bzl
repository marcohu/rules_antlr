AntlrInfo = provider(fields = {
        "sources": "The generated source files.",
        "headers": "For C/C++ the generated header files.",
        "data": "Additional ANTLR data files",
    },
    doc = "A provider containing information about ANTLR code generation.",
)


def antlr(version, ctx, args):
    """ Generates the source files. """

    if not ctx.files.srcs:
        fail("No grammars provided, either add the srcs attribute or check your filespec", attr="srcs")

    srcjar = None
    sources = []
    headers = []

    output_type = "dir" if ctx.attr.language and ctx.attr.language != "Java" else "srcjar"

    if output_type == "srcjar":
        # the Java rules are special in that the output is a .jar file
        srcjar = ctx.actions.declare_file(ctx.attr.name + "." + output_type)
        output_dir = ctx.configuration.bin_dir.path + "/rules_antlr"
        outputs = [srcjar]
    else:
        # for all other languages we use directories
        sources = ctx.actions.declare_directory(ctx.attr.name + extension(ctx.attr.language))
        output_dir = sources.path
        # for C/C++ we must split headers from sources
        if (ctx.attr.language == "Cpp" or ctx.attr.language == "C"):
            headers = ctx.actions.declare_directory(ctx.attr.name + ".inc")
            outputs = [sources, headers]
        else:
            outputs = [sources]

    tool_inputs, _, input_manifests=ctx.resolve_command(tools=ctx.attr.deps + [ctx.attr._tool])

    ctx.actions.run(
        arguments = [args(ctx, output_dir)],
        inputs = ctx.files.srcs + ctx.files.imports,
        outputs = outputs,
        mnemonic = "ANTLR" + version,
        executable = ctx.executable._tool,
        env = {
            "ANTLR_VERSION": version,
            "DIRECTORY_LAYOUT": ctx.attr.layout if hasattr(ctx.attr, "layout") else "",
            "ENCODING": ctx.attr.encoding if hasattr(ctx.attr, "encoding") else "",
            "GRAMMARS": ",".join([f.path for f in ctx.files.srcs]),
            "OUTPUT_DIRECTORY": output_dir,
            "PACKAGE_NAME": ctx.attr.package,
            "SRC_JAR": srcjar.path if srcjar else "",
            "TARGET_LANGUAGE": ctx.attr.language,
            "TOOL_CLASSPATH": ",".join([f.path for f in tool_inputs]),
        },
        input_manifests = input_manifests,
        progress_message = "Processing ANTLR {} grammars".format(version),
        tools = tool_inputs,
    )

    return [
        AntlrInfo(
            sources = sources,
            headers = headers,
            data = [ctx.attr.name + ".antlr"],
        ),
        platform_common.TemplateVariableInfo({
            "INCLUDES": ctx.attr.name + ".inc/" + ctx.attr.package,
        }),
        DefaultInfo(files=depset(outputs)),
    ]


def _headers(ctx):
    return [DefaultInfo(files=depset([ctx.attr.rule[AntlrInfo].headers]))]

headers = rule(
    implementation = _headers,
    doc = "Filters the generated C/C++ header files from the generated files.",
    attrs = {
        "rule": attr.label(
            allow_files=True,
            mandatory=True,
            doc="The name of the antlr() rule that generated the files.",
        ),
    },
)


def _sources(ctx):
    return [DefaultInfo(files=depset([ctx.attr.rule[AntlrInfo].sources]))]

sources = rule(
    implementation = _sources,
    doc = "Filters the generated C/C++ source files from the generated files.",
    attrs = {
        "rule": attr.label(
            allow_files=True,
            mandatory=True,
            doc="The name of the antlr() rule that generated the files.",
        ),
    },
)


def extension(language):
    """ Determines the extension to use for tree artifact output. """
    if language == "Cpp" or language == "C":
        return ".cc"
    if language == "Python":
        return ".py"
    return ""


def lib_dir(imports):
    """ Determines the directory that contains the given imports. """
    lib = {}
    for resource in imports:
        lib[resource.path.replace("/" + resource.basename, "")] = None
    count = len(lib)
    # the lib directory does not allow nested directories
    if count > 1:
        fail("All imports must be located in the same directory, but found {}".format(lib))
    return lib.keys()[0] if count == 1 else None;


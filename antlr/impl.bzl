"""The common ANTLR rule implementation."""

load(":lang.bzl", "C", "CPP", "GO", "OBJC", "PYTHON", "PYTHON2", "PYTHON3")

AntlrInfo = provider(
    fields = {
        "sources": "The generated source files.",
        "headers": "The generated header files (for C/C++/ObjC).",
        "data": "Additional ANTLR data files.",
    },
    doc = "A provider containing information about ANTLR code generation.",
)

_NullInfo = provider()

def antlr(version, ctx, args):
    """Generates the source files.

    Args:
      version: the ANTLR release to use.
      ctx: the rule context.
      args: the ANTLR tool arguments.
    Returns:
      the generated files.
    """

    if not ctx.files.srcs:
        fail("No grammars provided, either add the srcs attribute or check your filespec", attr = "srcs")

    srcjar = None
    data = []
    sources = []
    headers = []
    cc = ctx.attr.language == CPP or ctx.attr.language == C or ctx.attr.language == OBJC
    output_type = "dir" if ctx.attr.language and ctx.attr.language != "Java" else "srcjar"

    if output_type == "srcjar":
        # the Java rules are special in that the output is a .jar file
        srcjar = ctx.actions.declare_file(ctx.attr.name + "." + output_type)
        output_dir = ctx.configuration.bin_dir.path + "/rules_antlr/" + ctx.attr.name
        outputs = [srcjar]
    else:
        # for all other languages we use directories
        sources = ctx.actions.declare_directory(ctx.attr.name + extension(ctx.attr.language))
        output_dir = sources.path

        # for C/C++ we must split headers from sources
        if cc:
            data = [ctx.actions.declare_directory(ctx.attr.name + ".antlr")]
            headers = ctx.actions.declare_directory(ctx.attr.name + ".inc")
            outputs = [sources, headers]
        elif ctx.attr.language == GO:
            data = [ctx.actions.declare_directory(ctx.attr.name + ".antlr")]
            outputs = [sources]
        else:
            outputs = [sources]

    tool_inputs, _, input_manifests = ctx.resolve_command(tools = ctx.attr.deps + [ctx.attr._tool])

    ctx.actions.run(
        arguments = [args(ctx, output_dir)],
        inputs = ctx.files.srcs + ctx.files.imports,
        outputs = outputs + data,
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
            "TARGET": ctx.attr.name,
            "TARGET_LANGUAGE": ctx.attr.language,
            "TOOL_CLASSPATH": ",".join([f.path for f in tool_inputs]),
        },
        input_manifests = input_manifests,
        progress_message = "Processing ANTLR {} grammars".format(version),
        tools = tool_inputs,
    )

    # for C/C++ we add the generated headers to the compilation context
    if cc:
        compilation_context = cc_common.create_compilation_context(headers = depset([headers]), system_includes = depset([headers.path + "/" + ctx.attr.package]))

    return [
        AntlrInfo(
            sources = sources,
            headers = headers,
            data = [ctx.attr.name + ".antlr"],
        ),
        CcInfo(compilation_context = compilation_context) if cc else _NullInfo(),
        DefaultInfo(files = depset(outputs)),
    ]

def extension(language):
    """Determines the extension to use for tree artifact output.

    Args:
      language: the programming language abbreviation.
    Returns:
      the extension to use, might be empty.
    """
    if language == CPP or language == C:
        return ".cc"
    if language == GO:
        return ".go"
    if language == OBJC:
        return ".objc"
    if language == PYTHON or language == PYTHON2 or language == PYTHON3:
        return ".py"
    return ""

def lib_dir(imports):
    """Determines the directory that contains the given imports.

    Args:
      imports: the directories where to find the grammars to import.
    Returns:
      the directory that contains all imports (if are located in the same directory).
    """
    lib = {}
    for resource in imports:
        if resource.path.endswith(".srcjar"):
            lib[resource.path] = None
        else:
            lib[resource.path.replace("/" + resource.basename, "")] = None
    count = len(lib)

    # the lib directory does not allow nested directories
    if count > 1:
        fail("All imports must be located in the same directory, but found {}".format(lib))
    return lib.keys()[0] if count == 1 else None

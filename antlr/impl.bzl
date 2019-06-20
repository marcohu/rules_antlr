
def antlr(version, ctx, args):
    """ Generates the source files. """

    if not ctx.files.srcs:
        fail("No grammars provided, either add the srcs attribute or check your filespec", attr="srcs")

    output_dir = ctx.configuration.genfiles_dir.path + "/rules_antlr"
    srcjar = ctx.outputs.src_jar
    tool_inputs, _, input_manifests=ctx.resolve_command(tools=ctx.attr.deps + [ctx.attr._tool])

    ctx.actions.run(
        arguments = [args(ctx, output_dir)],
        inputs = ctx.files.srcs + ctx.files.imports,
        outputs = [srcjar],
        mnemonic = "ANTLR" + version,
        executable = ctx.executable._tool,
        env = {
            "ANTLR_VERSION": version,
            "DIRECTORY_LAYOUT": ctx.attr.layout if hasattr(ctx.attr, "layout") else "",
            "ENCODING": ctx.attr.encoding if hasattr(ctx.attr, "encoding") else "",
            "GRAMMARS": ",".join([f.path for f in ctx.files.srcs]),
            "OUTPUT_DIRECTORY": output_dir,
            "PACKAGE_NAME": ctx.attr.package,
            "SRC_JAR": srcjar.path,
            "TARGET_LANGUAGE": ctx.attr.language,
            "TOOL_CLASSPATH": ",".join([f.path for f in tool_inputs]),
        },
        input_manifests = input_manifests,
        progress_message = "Processing ANTLR {} grammars".format(version),
        tools = tool_inputs
    )

    return [
        DefaultInfo(files=depset([srcjar])),
    ]


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


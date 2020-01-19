load("@rules_antlr//antlr:antlr3.bzl", "antlr")

antlr(
    name = "codegen",
    srcs = glob(["*.g"]),
    imports = ["//antlr3/ImportGenerated/src/parse:parser"],
)

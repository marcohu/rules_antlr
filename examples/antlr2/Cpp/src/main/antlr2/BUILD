load("@rules_antlr//antlr:antlr2.bzl", "antlr")

filegroup(
    name = "srcs",
    srcs = ["lexer.g", ":parser",],
)

filegroup(
    name = "parser",
    srcs = ["tinyc.g"],
)

antlr(
    name = "generated",
    srcs = [":srcs"],
    language = "Cpp",
)


package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr2.bzl", "antlr2")

filegroup(
    name = "srcs",
    srcs = [":parser", "src/main/antlr2/lexer.g"],
)

filegroup(
    name = "parser",
    srcs = ["src/main/antlr2/tinyc.g"],
)

antlr2(
    name = "generated",
    srcs = [":srcs"],
)

java_library(
    name = "TinyC",
    srcs = [":generated"],
    deps = ["@antlr2//jar"],
)

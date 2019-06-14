package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr2.bzl", "antlr")

filegroup(
    name = "srcs",
    srcs = ["src/main/antlr2/lexer.g", ":parser",],
)

filegroup(
    name = "parser",
    srcs = ["src/main/antlr2/tinyc.g"],
)

antlr(
    name = "generated",
    srcs = [":srcs"],
)

java_library(
    name = "TinyC",
    srcs = [":generated"],
    deps = ["@antlr2//jar"],
)

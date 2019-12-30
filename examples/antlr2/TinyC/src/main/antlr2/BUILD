load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr2.bzl", "antlr")

filegroup(
    name = "srcs",
    srcs = [
        "lexer.g",
        ":parser",
    ],
)

filegroup(
    name = "parser",
    srcs = ["tinyc.g"],
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

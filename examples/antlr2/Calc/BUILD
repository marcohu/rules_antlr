package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr2.bzl", "antlr2")

filegroup(
    name = "grammars",
    srcs = glob(["src/main/antlr2/calc.g"]),
)

antlr2(
    name = "generated",
    srcs = [":grammars"],
)

java_library(
    name = "Calc",
    srcs = [":generated"],
    deps = ["@antlr2//jar"],
)

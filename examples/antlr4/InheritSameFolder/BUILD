package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "generated",
    srcs = glob(["src/main/antlr4/*.g4"]),
    package = "inherit",
)

java_library(
    name = "InheritSameFolder",
    srcs = [":generated"],
    deps = ["@antlr4_runtime//jar"],
)

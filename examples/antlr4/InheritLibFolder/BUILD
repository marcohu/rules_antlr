package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr4", "imports")

antlr4(
    name = "generated",
    srcs = glob(["src/main/antlr4/*.g4"]),
    imports = imports("src/main/antlr4/imports"),
)

java_library(
    name = "InheritLibFolder",
    srcs = [":generated"],
    deps = ["@antlr4_runtime//jar"],
)

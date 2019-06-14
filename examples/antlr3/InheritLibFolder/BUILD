package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr3.bzl", "antlr", "imports")

antlr(
    name = "generated",
    srcs = ["src/main/antlr3/Simple.g"],
    imports = imports("src/main/antlr3/lib"),
)

java_library(
    name = "InheritLibFolder",
    srcs = [":generated"],
    deps = ["@antlr3_runtime//jar"],
)

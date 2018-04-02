package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr3.bzl", "antlr3", "imports")

antlr3(
    name = "generated",
    srcs = glob(["src/main/antlr3/*.g"]),
    imports = imports("src/main/antlr3/lib"),
)

java_library(
    name = "InheritLibFolder",
    srcs = [":generated"],
    deps = ["@antlr3_runtime//jar"],
)

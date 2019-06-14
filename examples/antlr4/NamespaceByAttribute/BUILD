package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "generated",
    srcs = ["src/main/antlr4/foo/bar/Hello.g4"],
    package = "hello.world",
)

java_library(
    name = "NamespaceByAttribute",
    srcs = [":generated"],
    deps = ["@antlr4_runtime//jar"],
)

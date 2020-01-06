load("@rules_antlr//antlr:antlr4.bzl", "antlr")
load("@io_bazel_rules_go//go:def.bzl", "go_library")

antlr(
    name = "generated",
    srcs = glob(["*.g4"]),
    language = "Go",
)

go_library(
    name = "parser",
    srcs = [
        ":generated",
    ],
    importpath = "json.parser",
    visibility = ["//visibility:public"],
    deps = [
        "@antlr4_runtimes//:go",
    ],
)

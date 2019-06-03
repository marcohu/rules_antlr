package(default_visibility = ["//visibility:public"])

load("@io_bazel_skydoc//stardoc:stardoc.bzl", "stardoc")
load("@bazel_skylib//:bzl_library.bzl", "bzl_library")

filegroup(
    name = "srcs",
    srcs = glob(["*"]),
)

bzl_library(
    name = "rules",
    srcs = ["antlr4.bzl", "antlr3.bzl", "antlr2.bzl"],
)
stardoc(
    name = "multi-docs",
    input = "rules.bzl",
    out = "rules.md",
    symbol_names = ["antlr4", "antlr3", "antlr2"],
    deps = [":rules"],
)


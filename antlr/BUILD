package(default_visibility = ["//visibility:public"])

load("@io_bazel_skydoc//stardoc:stardoc.bzl", "stardoc")
load("@bazel_skylib//:bzl_library.bzl", "bzl_library")

filegroup(
    name = "srcs",
    srcs = glob(["*"]),
)

bzl_library(
    name = "shared",
    srcs = ["impl.bzl"],
)

stardoc(
    name = "antlr4",
    out = "antlr4.md",
    input = "antlr4.bzl",
    deps = [":shared"],
)

stardoc(
    name = "antlr3",
    out = "antlr3.md",
    input = "antlr3.bzl",
    deps = [":shared"],
)

stardoc(
    name = "antlr2",
    out = "antlr2.md",
    input = "antlr2.bzl",
    deps = [":shared"],
)

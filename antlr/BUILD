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
    input = "antlr4.bzl",
    out = "antlr4.md",
    deps = [":shared"],
)
stardoc(
    name = "antlr3",
    input = "antlr3.bzl",
    out = "antlr3.md",
    deps = [":shared"],
)
stardoc(
    name = "antlr2",
    input = "antlr2.bzl",
    out = "antlr2.md",
    deps = [":shared"],
)


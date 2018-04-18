package(default_visibility = ["//visibility:public"])

load("@io_bazel_skydoc//skylark:skylark.bzl", "skylark_doc")

filegroup(
    name = "srcs",
    srcs = glob(["*"]),
)

skylark_doc(
    name = "rules",
    srcs = ["antlr2.bzl", "antlr3.bzl", "antlr4.bzl"],
)

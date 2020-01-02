load("@rules_antlr//antlr:antlr2.bzl", "antlr")
load("@rules_python//python:defs.bzl", "py_binary", "py_library")

antlr(
    name = "generated",
    srcs = ["xml.g"],
    language = "Python",
)

py_library(
    name = "lib",
    srcs = [":generated"],
)

py_binary(
    name = "xml",
    srcs = ["xml.py"],
    deps = [
        ":lib",
        "@antlr2_runtimes//:python",
    ],
)

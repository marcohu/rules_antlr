load("@rules_python//python:defs.bzl", "py_binary", "py_library")
load("@rules_antlr//antlr:antlr3.bzl", "antlr")

antlr(
    name = "generated",
    srcs = ["Calculator.g"],
    language = "Python3",
)

py_library(
    name = "lib",
    srcs = [":generated"],
)

py_binary(
    name = "calc",
    srcs = ["calc.py"],
    deps = [
        ":lib",
        "@antlr3_runtimes//:python",
    ],
)

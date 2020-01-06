load("@rules_antlr//antlr:antlr4.bzl", "antlr")
load("@rules_python//python:defs.bzl", "py_binary", "py_library")

antlr(
    name = "generated",
    srcs = ["Hello.g4"],
    language = "Python2",
)

py_library(
    name = "lib",
    srcs = [":generated"],
)

py_binary(
    name = "hello",
    srcs = ["hello.py"],
    deps = [
        ":lib",
        "@antlr4_runtimes//:python2",
    ],
)

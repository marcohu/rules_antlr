load("@rules_antlr//antlr:antlr3.bzl", "antlr")


antlr(
    name = "generated",
    srcs = ["Calculator.g"],
    language = "Python",
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
        "@antlr3_runtimes//:py",
    ],
)

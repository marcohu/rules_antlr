load("@rules_antlr//antlr:antlr2.bzl", "antlr")


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
        "@antlr2_runtimes//:py",
    ],
)

load("@rules_antlr//antlr:antlr4.bzl", "antlr", "headers", "sources")

antlr(
    name = "generated",
    srcs = glob(["*.g4"]),
    language = "Cpp",
    package = "antlrcpptest",
)

headers(
    name = "headers",
    rule = ":generated",
)

sources(
    name = "sources",
    rule = ":generated",
)


"""
antlr_cc_library(
    name = "lib2",
    srcs = glob(["*.g4"]),
    language = "Cpp",
    package = "antlrcpptest",
)
"""

cc_library(
    name = "lib",
    hdrs = [":headers"],
    srcs = [":sources"],
    includes = ["$(INCLUDES)"],
    deps = ["@antlr4_runtimes//:cpp"],
    visibility = ["//visibility:public"],
    toolchains = [":generated"],
)

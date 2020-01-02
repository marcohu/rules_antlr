load("@rules_antlr//antlr:antlr3.bzl", "antlr")
load("@rules_cc//cc:defs.bzl", "cc_library")

antlr(
    name = "generated",
    srcs = glob(["T.g"]),
    language = "Cpp",
    package = "User",
)

cc_library(
    name = "lib",
    srcs = [":generated"],
    visibility = ["//visibility:public"],
    deps = [
        ":generated",
        "@antlr3_runtimes//:cpp",
    ],
)

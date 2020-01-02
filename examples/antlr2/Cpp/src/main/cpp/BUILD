load("@rules_antlr//antlr:antlr2.bzl", "antlr")

cc_library(
    name = "lib",
    srcs = ["//antlr2/Cpp/src/main/antlr2:generated"],
    visibility = ["//visibility:public"],
    deps = [
        "//antlr2/Cpp/src/main/antlr2:generated",
        "@antlr2_runtimes//:cpp",
    ],
)

cc_binary(
    name = "bin",
    srcs = ["Main.cpp"],
    visibility = ["//visibility:public"],
    deps = [":lib"],
)

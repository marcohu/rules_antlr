package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr3.bzl", "antlr")

antlr(
    name = "generated",
    srcs = ["src/main/antlr3/SimpleC.g"],
)

java_library(
    name = "SimpleC",
    srcs = [":generated"],
    deps = ["@antlr3_runtime//jar"],
)

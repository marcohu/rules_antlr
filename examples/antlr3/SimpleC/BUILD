package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr3.bzl", "antlr3")

antlr3(
    name = "generated",
    srcs = ["src/main/antlr3/SimpleC.g"],
)

java_library(
    name = "SimpleC",
    srcs = [":generated"],
    deps = ["@antlr3_runtime//jar"],
)

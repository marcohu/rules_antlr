package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr3.bzl", "antlr")

antlr(
    name = "generated",
    srcs = ["src/main/antlr3/SimpleC.g"],
    language = "ObjC",
)

java_library(
    name = "LanguageByAttribute",
    srcs = [":generated"],
    deps = ["@antlr3_runtime//jar"],
)

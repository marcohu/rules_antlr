package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "generated",
    srcs = ["src/main/antlr4/Hello.g4"],
    options = {"language": "Swift"},
)

java_library(
    name = "LanguageByOption",
    srcs = [":generated"],
    deps = ["@antlr4_runtime//jar"],
)

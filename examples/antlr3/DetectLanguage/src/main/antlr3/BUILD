load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr3.bzl", "antlr")

antlr(
    name = "csharp",
    srcs = ["T.g"],
)

java_library(
    name = "DetectLanguage",
    srcs = [":csharp"],
    deps = ["@antlr3_runtime//jar"],
)

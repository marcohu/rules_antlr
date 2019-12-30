load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr2.bzl", "antlr")

antlr(
    name = "generated",
    srcs = ["calc.g"],
)

java_library(
    name = "Calc",
    srcs = [":generated"],
    deps = ["@antlr2//jar"],
)

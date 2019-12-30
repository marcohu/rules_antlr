load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr3.bzl", "antlr")

antlr(
    name = "generated",
    srcs = glob(["*.g"]),
)

java_library(
    name = "InheritSameFolder",
    srcs = [":generated"],
    deps = ["@antlr3_runtime//jar"],
)

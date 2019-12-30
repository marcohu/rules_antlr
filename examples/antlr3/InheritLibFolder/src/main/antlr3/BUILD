load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr3.bzl", "antlr", "imports")

antlr(
    name = "generated",
    srcs = ["Simple.g"],
    imports = imports("lib"),
)

java_library(
    name = "InheritLibFolder",
    srcs = [":generated"],
    deps = ["@antlr3_runtime//jar"],
)

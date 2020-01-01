load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr2.bzl", "antlr")

antlr(
    name = "generated",
    srcs = ["subc.g"],
    # to reference an existing grammar, you have to provide both the source and generated files
    imports = [
        "//antlr2/TinyC/src/main/antlr2:generated",
        "//antlr2/TinyC/src/main/antlr2:parser",
    ],
)

java_library(
    name = "InheritTinyC",
    srcs = [":generated"],
    deps = ["@antlr2//jar"],
)

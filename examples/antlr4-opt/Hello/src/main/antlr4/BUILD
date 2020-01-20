load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "generated",
    srcs = ["Hello.g4"],
    package = "hello.world",
)

java_library(
    name = "HelloWorld",
    srcs = [":generated"],
    deps = ["@antlr4_runtime//jar"],
)

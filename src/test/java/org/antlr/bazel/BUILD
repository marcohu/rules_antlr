package(default_visibility = ["//visibility:public"])

load("//tools:gen_test_rules.bzl", "java_tests")

java_library(
    name = "tests",
    srcs = glob(["*.java"]),
    data = [
        "@examples//antlr2:srcs",
        "@examples//antlr3:srcs",
        "@examples//antlr4:srcs",
    ],
    deps = [
        "@guava//jar",
        "@jimfs//jar",
        "@junit//jar",
        "//src/main/java/org/antlr/bazel",
    ],
)

java_tests(
    name = "GenerateTests",
    test_files = glob(["*Test.java"]),
    deps = [":tests"],
)

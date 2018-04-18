load("//tools:gen_test_rules.bzl", "java_tests")

java_library(
    name = "tests",
    srcs = glob(["*.java"]),
    data = ["//src/test/resources/org/antlr/bazel:srcs"],
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

load("@rules_java//java:defs.bzl", "java_library")
load("//tools:gen_test_rules.bzl", "java_tests")

java_library(
    name = "antlr2_tests",
    srcs = glob(["*.java"]),
    data = [
        "//antlr:srcs",
        "//src/it/resources/antlr2:srcs",
        "//src/main/java/org/antlr/bazel:srcs",
        "@examples//antlr2:srcs",
        "@rules_antlr//:srcs",
    ],
    deps = [
        "//src/main/java/org/antlr/bazel",
        "//src/test/java/org/antlr/bazel:tests",
        "@antlr2//jar",
        "@guava//jar",
        "@jimfs//jar",
        "@junit//jar",
    ],
)

java_tests(
    name = "antlr2",
    default_test_size = "enormous",
    test_files = ["Antlr2Test.java"],
    deps = [":antlr2_tests"],
)

java_library(
    name = "antlr3_tests",
    srcs = glob(["*.java"]),
    data = [
        "//antlr:srcs",
        "//src/it/resources/antlr3:srcs",
        "//src/main/java/org/antlr/bazel:srcs",
        "@examples//antlr3:srcs",
        "@rules_antlr//:srcs",
    ],
    deps = [
        "//src/main/java/org/antlr/bazel",
        "//src/test/java/org/antlr/bazel:tests",
        "@antlr3_runtime//jar",
        "@antlr3_tool//jar",
        "@guava//jar",
        "@jimfs//jar",
        "@junit//jar",
        "@stringtemplate4//jar",
    ],
)

java_tests(
    name = "antlr3",
    default_test_size = "enormous",
    test_files = ["Antlr3Test.java"],
    deps = [":antlr3_tests"],
)

java_library(
    name = "antlr4_tests",
    srcs = glob(["*.java"]),
    data = [
        "//antlr:srcs",
        "//src/it/resources/antlr4:srcs",
        "//src/main/java/org/antlr/bazel:srcs",
        "@examples//antlr4:srcs",
        "@rules_antlr//:srcs",
    ],
    deps = [
        "//src/main/java/org/antlr/bazel",
        "//src/test/java/org/antlr/bazel:tests",
        "@antlr3_runtime//jar",
        "@antlr4_runtime//jar",
        "@antlr4_tool//jar",
        "@guava//jar",
        "@javax_json//jar",
        "@jimfs//jar",
        "@junit//jar",
        "@stringtemplate4//jar",
    ],
)

java_tests(
    name = "antlr4",
    default_test_size = "enormous",
    test_files = ["Antlr4Test.java"],
    deps = [":antlr4_tests"],
)

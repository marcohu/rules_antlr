load("//tools:gen_test_rules.bzl", "java_tests")

java_library(
    name = "antlr2_tests",
    srcs = glob(["*.java"]),
    data = [
        "//src/main/java/org/antlr/bazel:srcs",
        "//src/it/resources/antlr2:srcs",
        "//antlr:srcs",
        "@rules_antlr//:srcs",
        "@examples//antlr2:srcs",
    ],
    deps = [
        "@antlr2//jar",
        "@guava//jar",
        "@jimfs//jar",
        "@junit//jar",
        "//src/main/java/org/antlr/bazel",
        "//src/test/java/org/antlr/bazel:tests",
    ],
)

java_tests(
    name = "antlr2",
    test_files = glob(["Antlr2Test.java"]),
    default_test_size = "enormous",
    deps = [":antlr2_tests"],
)

java_library(
    name = "antlr3_tests",
    srcs = glob(["*.java"]),
    data = [
        "//src/main/java/org/antlr/bazel:srcs",
        "//src/it/resources/antlr3:srcs",
        "//antlr:srcs",
        "@rules_antlr//:srcs",
        "@examples//antlr3:srcs",
    ],
    deps = [
        "@antlr3_tool//jar",
        "@antlr3_runtime//jar",
        "@stringtemplate4//jar",
        "@guava//jar",
        "@jimfs//jar",
        "@junit//jar",
        "//src/main/java/org/antlr/bazel",
        "//src/test/java/org/antlr/bazel:tests",
    ],
)

java_tests(
    name = "antlr3",
    test_files = glob(["Antlr3Test.java"]),
    default_test_size = "enormous",
    deps = [":antlr3_tests"],
)

java_library(
    name = "antlr4_tests",
    srcs = glob(["*.java"]),
    data = [
        "//src/main/java/org/antlr/bazel:srcs",
        "//src/it/resources/antlr4:srcs",
        "//antlr:srcs",
        "@rules_antlr//:srcs",
        "@examples//antlr4:srcs",
    ],
    deps = [
        "@antlr4_tool//jar",
        "@antlr4_runtime//jar",
        "@stringtemplate4//jar",
        "@antlr3_runtime//jar",
        "@javax_json//jar",
        "@guava//jar",
        "@jimfs//jar",
        "@junit//jar",
        "//src/main/java/org/antlr/bazel",
        "//src/test/java/org/antlr/bazel:tests",
    ],
)

java_tests(
    name = "antlr4",
    test_files = glob(["Antlr4Test.java"]),
    default_test_size = "enormous",
    deps = [":antlr4_tests"],
)


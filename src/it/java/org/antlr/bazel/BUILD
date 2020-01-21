load("@rules_java//java:defs.bzl", "java_library")
load("//tools:gen_test_rules.bzl", "java_tests")

java_library(
    name = "support",
    srcs = glob(
        ["*.java"],
        exclude = ["*Test.java"],
    ),
    javacopts = ["--release 11"],
    deps = [
        "//src/test/java/org/antlr/bazel:tests",
        "@junit//jar",
    ],
)

java_library(
    name = "antlr2_tests",
    testonly = True,
    srcs = glob(["Antlr2Test.java"]),
    data = [
        "//:srcs",
        "//antlr:srcs",
        "//src/it/resources/antlr2:srcs",
        "//src/main/java/org/antlr/bazel:srcs",
        "//third_party:srcs",
        "@examples//antlr2:srcs",
    ],
    deps = [
        ":support",
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
    default_test_size = "small",
    test_files = ["Antlr2Test.java"],
    deps = [":antlr2_tests"],
)

java_library(
    name = "antlr3_tests",
    testonly = True,
    srcs = glob(["Antlr3Test.java"]),
    data = [
        "//:srcs",
        "//antlr:srcs",
        "//src/it/resources/antlr3:srcs",
        "//src/main/java/org/antlr/bazel:srcs",
        "@examples//antlr3:srcs",
    ],
    deps = [
        ":support",
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
    default_test_size = "small",
    test_files = ["Antlr3Test.java"],
    deps = [":antlr3_tests"],
)

java_library(
    name = "antlr4_tests",
    testonly = True,
    srcs = glob(["Antlr4Test.java"]),
    data = [
        "//:srcs",
        "//antlr:srcs",
        "//src/it/resources/antlr4:srcs",
        "//src/main/java/org/antlr/bazel:srcs",
        "@examples//antlr4:srcs",
    ],
    deps = [
        ":support",
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
    default_test_size = "small",
    test_files = ["Antlr4Test.java"],
    deps = [":antlr4_tests"],
)

java_library(
    name = "repository_tests",
    testonly = True,
    srcs = glob(["RepositoriesTest.java"]),
    data = [
        "//:srcs",
        "//antlr:srcs",
        "//src/it/resources/antlr2:srcs",
        "//src/main/java/org/antlr/bazel:srcs",
        "//third_party:srcs",
        "@examples//antlr2:srcs",
    ],
    deps = [
        ":support",
        "//src/main/java/org/antlr/bazel",
        "//src/test/java/org/antlr/bazel:tests",
        "@antlr2//jar",
        "@guava//jar",
        "@jimfs//jar",
        "@junit//jar",
    ],
)

java_tests(
    name = "repository",
    default_test_size = "medium",
    test_files = ["RepositoriesTest.java"],
    deps = [":repository_tests"],
)

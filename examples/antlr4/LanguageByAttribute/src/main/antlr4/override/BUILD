load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "cpp",
    srcs = glob(["**/*.g4"]),
    language = "Cpp",
    package = "a::b",
)

antlr(
    name = "csharp",
    srcs = glob(["**/*.g4"]),
    language = "CSharp",
    package = "override",
)

antlr(
    name = "go",
    srcs = glob(["**/*.g4"]),
    language = "Go",
    package = "override",
)

antlr(
    name = "javascript",
    srcs = glob(["**/*.g4"]),
    #    package = "override",
    language = "JavaScript",
)

antlr(
    name = "java",
    srcs = glob(["**/*.g4"]),
    language = "Java",
    package = "override",
)

antlr(
    name = "python2",
    srcs = glob(["**/*.g4"]),
    language = "Python2",
    package = "override",
)

antlr(
    name = "python3",
    srcs = glob(["**/*.g4"]),
    language = "Python3",
    package = "override",
)

antlr(
    name = "swift",
    srcs = glob(["**/*.g4"]),
    language = "Swift",
    package = "override",
)

java_library(
    name = "LanguageByAttribute",
    srcs = [
        ":cpp",
        ":javascript",
    ],
    deps = ["@antlr4_runtime//jar"],
)

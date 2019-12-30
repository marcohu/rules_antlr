load("@rules_java//java:defs.bzl", "java_library")

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "cpp",
    srcs = glob(["cpp/*.g4"]),
)

antlr(
    name = "csharp",
    srcs = glob(["csharp/*.g4"]),
)

antlr(
    name = "go",
    srcs = glob(["go/**/*.g4"]),
)

antlr(
    name = "javascript",
    srcs = glob(["javascript/*.g4"]),
)

antlr(
    name = "java",
    srcs = glob(["java/**/*.g4"]),
)

antlr(
    name = "python2",
    srcs = glob(["python2/*.g4"]),
)

antlr(
    name = "python3",
    srcs = glob(["python3/*.g4"]),
)

antlr(
    name = "swift",
    srcs = glob(["swift/*.g4"]),
)

java_library(
    name = "DetectLanguage",
    srcs = [
        ":cpp",
        ":csharp",
        ":go",
        ":java",
        ":javascript",
        ":python2",
        ":python3",
        ":swift",
    ],
    deps = ["@antlr4_runtime//jar"],
)

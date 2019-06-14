package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "cpp",
    srcs = glob(["src/main/antlr4/cpp/*.g4"]),
)

antlr(
    name = "csharp",
    srcs = glob(["src/main/antlr4/csharp/*.g4"]),
)

antlr(
    name = "go",
    srcs = glob(["src/main/antlr4/go/**/*.g4"]),
)

antlr(
    name = "javascript",
    srcs = glob(["src/main/antlr4/javascript/*.g4"]),
)

antlr(
    name = "java",
    srcs = glob(["src/main/antlr4/java/**/*.g4"]),
)

antlr(
    name = "python2",
    srcs = glob(["src/main/antlr4/python2/*.g4"]),
)

antlr(
    name = "python3",
    srcs = glob(["src/main/antlr4/python3/*.g4"]),
)

antlr(
    name = "swift",
    srcs = glob(["src/main/antlr4/swift/*.g4"]),
)

java_library(
    name = "DetectLanguage",
    srcs = [":cpp", ":csharp", ":go", ":javascript", ":java", ":python2", ":python3", ":swift"],
    deps = ["@antlr4_runtime//jar"],
)

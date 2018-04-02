package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr4")

antlr4(
    name = "cpp",
    srcs = glob(["src/main/antlr4/cpp/*.g4"]),
)

antlr4(
    name = "csharp",
    srcs = glob(["src/main/antlr4/csharp/*.g4"]),
)

antlr4(
    name = "go",
    srcs = glob(["src/main/antlr4/go/**/*.g4"]),
)

antlr4(
    name = "javascript",
    srcs = glob(["src/main/antlr4/javascript/*.g4"]),
)

antlr4(
    name = "java",
    srcs = glob(["src/main/antlr4/java/**/*.g4"]),
)

antlr4(
    name = "python2",
    srcs = glob(["src/main/antlr4/python2/*.g4"]),
)

antlr4(
    name = "python3",
    srcs = glob(["src/main/antlr4/python3/*.g4"]),
)

antlr4(
    name = "swift",
    srcs = glob(["src/main/antlr4/swift/*.g4"]),
)

java_library(
    name = "DetectLanguage",
    srcs = [":cpp", ":csharp", ":go", ":javascript", ":java", ":python2", ":python3", ":swift"],
    deps = ["@antlr4_runtime//jar"],
)

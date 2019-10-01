package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "cpp",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    language = "Cpp",
    package = "a::b",
)

antlr(
    name = "csharp",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    language = "CSharp",
    package = "override",
)

antlr(
    name = "go",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    language = "Go",
    package = "override",
)

antlr(
    name = "javascript",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    #    package = "override",
    language = "JavaScript",
)

antlr(
    name = "java",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    language = "Java",
    package = "override",
)

antlr(
    name = "python2",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    language = "Python2",
    package = "override",
)

antlr(
    name = "python3",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    language = "Python3",
    package = "override",
)

antlr(
    name = "swift",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
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

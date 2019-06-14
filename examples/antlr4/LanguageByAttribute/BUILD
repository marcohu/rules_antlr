package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "cpp",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "a::b",
    language = "Cpp",
)

antlr(
    name = "csharp",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "CSharp",
)

antlr(
    name = "go",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "Go",
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
    package = "override",
    language = "Java",
)

antlr(
    name = "python2",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "Python2",
)

antlr(
    name = "python3",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "Python3",
)

antlr(
    name = "swift",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "Swift",
)

java_library(
    name = "LanguageByAttribute",
    srcs = [":cpp", ":javascript"],
    deps = ["@antlr4_runtime//jar"],
)

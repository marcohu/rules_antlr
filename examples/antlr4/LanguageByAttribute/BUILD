package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr4")

antlr4(
    name = "cpp",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "a::b",
    language = "Cpp",
)

antlr4(
    name = "csharp",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "CSharp",
)

antlr4(
    name = "go",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "Go",
)

antlr4(
    name = "javascript",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
#    package = "override",
    language = "JavaScript",
)

antlr4(
    name = "java",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "Java",
)

antlr4(
    name = "python2",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "Python2",
)

antlr4(
    name = "python3",
    srcs = glob(["src/main/antlr4/override/**/*.g4"]),
    package = "override",
    language = "Python3",
)

antlr4(
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

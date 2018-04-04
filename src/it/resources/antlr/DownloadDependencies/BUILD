load("@rules_antlr//antlr:antlr4.bzl", "antlr4")
load("@rules_antlr//antlr:antlr3.bzl", "antlr3")
load("@rules_antlr//antlr:antlr2.bzl", "antlr2")

antlr4(
    name = "antlr4",
    srcs = ["src/main/antlr/Hello.g4"],
)

antlr3(
    name = "antlr3",
    srcs = ["src/main/antlr/Hello.g"],
)

antlr2(
    name = "antlr2",
    srcs = ["src/main/antlr/Hello.g"],
)

load("@rules_antlr//antlr:antlr3.bzl", "antlr", "headers", "sources")

antlr(
    name = "generated",
    srcs = glob(["T.g"]),
    language = "C",
)

headers(
    name = "headers",
    rule = ":generated",
)

sources(
    name = "sources",
    rule = ":generated",
)


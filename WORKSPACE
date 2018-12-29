workspace(name="rules_antlr")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_jar", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

local_repository(
  name = "examples",
  path = "./examples",
)

http_jar(
    name = "junit",
    url = "http://central.maven.org/maven2/junit/junit/4.12/junit-4.12.jar",
    sha256 = "59721f0805e223d84b90677887d9ff567dc534d7c502ca903c0c2b17f05c116a",
)
http_jar(
    name = "jimfs",
    url = "http://central.maven.org/maven2/com/google/jimfs/jimfs/1.1/jimfs-1.1.jar",
    sha256 = "c4828e28d7c0a930af9387510b3bada7daa5c04d7c25a75c7b8b081f1c257ddd",
)
http_jar(
    name = "guava",
    url = "http://central.maven.org/maven2/com/google/guava/guava/23.0/guava-23.0.jar",
    sha256 = "",
)

load("//antlr:deps.bzl", "antlr_dependencies")
antlr_dependencies(2, 3, 4)

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "io_bazel_skydoc",
    remote = "https://github.com/bazelbuild/skydoc.git",
    tag = "0.2.0",
)

load("@io_bazel_skydoc//:setup.bzl", "skydoc_repositories")
skydoc_repositories()

load("@io_bazel_rules_sass//:package.bzl", "rules_sass_dependencies")
rules_sass_dependencies()

load("@build_bazel_rules_nodejs//:defs.bzl", "node_repositories")
node_repositories()

load("@io_bazel_rules_sass//:defs.bzl", "sass_repositories")
sass_repositories()


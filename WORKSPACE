workspace(name="rules_antlr")

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_jar")

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

git_repository(
    name = "io_bazel_rules_sass",
    remote = "https://github.com/bazelbuild/rules_sass.git",
    tag = "0.0.3",
)
load("@io_bazel_rules_sass//sass:sass.bzl", "sass_repositories")
sass_repositories()

git_repository(
    name = "io_bazel_skydoc",
    remote = "https://github.com/bazelbuild/skydoc.git",
    tag = "0.1.4",
)
load("@io_bazel_skydoc//skylark:skylark.bzl", "skydoc_repositories")
skydoc_repositories()

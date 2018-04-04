# Setup

To use the ANTLR rules, add the following to your [`WORKSPACE`](https://docs.bazel.build/versions/master/build-ref.html#workspace) file to include
the external repository:

```python
http_archive(
    name = "rules_antlr",
    sha256 = "acd2a25f31aeeea5f58cdb434ae109d03826ae7cc11fe9efce1740102e3f4531",
    strip_prefix = "rules_antlr-0.1.0",
    urls = ["https://github.com/marcohu/rules_antlr/archive/0.1.0.tar.gz"],
)
```

Then you can load the necessary external dependencies in your [`WORKSPACE`](https://docs.bazel.build/versions/master/build-ref.html#workspace) file. For the current ANTLR release:

```python
load("@rules_antlr//antlr:deps.bzl", "antlr_dependencies")

antlr_dependencies()
```

If you need a different version or want to make the version explicit, you can specify the version number:

```python
load("@rules_antlr//antlr:deps.bzl", "antlr_dependencies")

antlr_dependencies(4)
```

If you require several releases, you can specify several versions at once:

```python
load("@rules_antlr//antlr:deps.bzl", "antlr_dependencies")

antlr_dependencies(2, 3, 4)
```
But be careful when updating to a new ANTLR rules version as the bundled dependencies
might change with each release. Alternatively you can pull the necessary dependencies yourself to
avoid coupling. For ANTLR 4.7.1:

```python
native.http_jar(
    name = "antlr4_runtime",
    url = "http://central.maven.org/maven2/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar",
    sha256 = "43516d19beae35909e04d06af6c0c58c17bc94e0070c85e8dc9929ca640dc91d",
)
native.http_jar(
    name = "antlr4_tool",
    url = "http://central.maven.org/maven2/org/antlr/antlr4/4.7.1/antlr4-4.7.1.jar",
    sha256 = "a2cdc2f2f8eb893728832568dc54d080eb5a1495edb3b66e51b97122a60a0d87",
)
native.http_jar(
    name = "antlr3_runtime",
    url = "http://central.maven.org/maven2/org/antlr/antlr-runtime/3.5.2/antlr-runtime-3.5.2.jar",
    sha256 = "ce3fc8ecb10f39e9a3cddcbb2ce350d272d9cd3d0b1e18e6fe73c3b9389c8734",
)
native.http_jar(
    name = "stringtemplate4",
    url = "http://central.maven.org/maven2/org/antlr/ST4/4.0.8/ST4-4.0.8.jar",
    sha256 = "58caabc40c9f74b0b5993fd868e0f64a50c0759094e6a251aaafad98edfc7a3b",
)
native.http_jar(
    name = "javax_json",
    url = "http://central.maven.org/maven2/org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar",
    sha256 = "0e1dec40a1ede965941251eda968aeee052cc4f50378bc316cc48e8159bdbeb4",
)
```

Look at the source code of
[`deps.bzl`](https://github.com/marcohu/rules_antlr/tree/master/antlr/deps.bzl) for the
default dependency names. You are not required to use these exact names. But if you don't, you have to list the dependencies explicitly on rule
invocation.


As a convenience there is also a shortcut for the ["optimized" fork](https://github.com/tunnelvisionlabs/antlr4) maintained by Sam Harwell:

```python
load("@rules_antlr//antlr:deps.bzl", "antlr_optimized_dependencies")

antlr_optimized_dependencies()
```

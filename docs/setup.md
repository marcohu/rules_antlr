# Setup

To use the rules, add the following to your [`WORKSPACE`](https://docs.bazel.build/versions/master/build-ref.html#workspace) file to include
the external repository:

```python
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
...
http_archive(
    name = "rules_antlr",
    sha256 = "932f0637acc20b67d90e68e47d019961105b00c5991a72ffee33bc1e58541734",
    strip_prefix = "rules_antlr-0.2.0",
    urls = ["https://github.com/marcohu/rules_antlr/archive/0.2.0.tar.gz"],
)
```

Then you can load the necessary external dependencies in your [`WORKSPACE`](https://docs.bazel.build/versions/master/build-ref.html#workspace) file.

For the most recent supported ANTLR release:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")

rules_antlr_dependencies()
```

If you need a different version or want to make the version explicit, you can specify the
version number. Either specify just the major version:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")

antlr_dependencies(4)
```

Or better and recommended make the version explicit to avoid coupling:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")

rules_antlr_dependencies(472)
```

> **_NOTE:_**
If you don't use explicit versions, be careful when updating to a new rules_antlr
release as the bundled dependencies might change.

If you require several releases, you can specify several versions at once:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")

rules_antlr_dependencies(277, 352, 472)
```

The examples above only load the default Java dependencies. If you require other runtimes,
you have to provide the language target as well, in no particular order.

To load C++ and Java dependencies for ANTLR 3.5.2 and 4.7.2:

```python
load("@rules_antlr//antlr:deps.bzl", "antlr_dependencies")
load("@rules_antlr//antlr:lang.bzl", "CPP", "JAVA")

antlr_dependencies(352, CPP, 472, JAVA)
```

If you need different releases for different target languages, you can employ multiple
calls:

```python
load("@rules_antlr//antlr:deps.bzl", "antlr_dependencies")
load("@rules_antlr//antlr:lang.bzl", "CPP", "JAVA", "PYTHON")

antlr_dependencies(CPP, 472, JAVA)
antlr_dependencies(352, PYTHON)
```

The currently supported releases are:

| Release  Stream | Supported Versions| Bundled Runtimes |
|-----------------|-------------------|------------------|
| 4               | 471, 472          | C++, Java, Python2, Python3
| 3               | 352               | C++, Java, Python2, Python3
| 2               | 277               | C++, Java, Python2

If your preferred ANTLR release is not supported out-of-the-box, you can pull
the necessary dependencies yourself. E.g. for ANTLR 4.7:

```python
http_jar(
    name = "antlr4_runtime",
    url = "https://jcenter.bintray.com/org/antlr/antlr4-runtime/4.7/antlr4-runtime-4.7.jar",
    sha256 = "2a61943f803bbd1d0e02dffd19b92a418f83340c994346809e3b51e2231aa6c0",
)
http_jar(
    name = "antlr4_tool",
    url = "https://jcenter.bintray.com/org/antlr/antlr4/4.7/antlr4-4.7.jar",
    sha256 = "7867257028b3373af011dee7b6ce9b587a8fd5c7a0b25f68b2ff4cb90be8aa07",
)
http_jar(
    name = "antlr3_runtime",
    url = "https://jcenter.bintray.com/org/antlr/antlr-runtime/3.5.2/antlr-runtime-3.5.2.jar",
    sha256 = "ce3fc8ecb10f39e9a3cddcbb2ce350d272d9cd3d0b1e18e6fe73c3b9389c8734",
)
http_jar(
    name = "stringtemplate4",
    url = "https://jcenter.bintray.com/org/antlr/ST4/4.0.8/ST4-4.0.8.jar",
    sha256 = "58caabc40c9f74b0b5993fd868e0f64a50c0759094e6a251aaafad98edfc7a3b",
)
http_jar(
    name = "javax_json",
    url = "https://jcenter.bintray.com/org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar",
    sha256 = "0e1dec40a1ede965941251eda968aeee052cc4f50378bc316cc48e8159bdbeb4",
)
```

Look at the source code of
[`repositories.bzl`](https://github.com/marcohu/rules_antlr/tree/master/antlr/repositories.bzl) for the
default dependency names. You are not required to use these exact names. But if you don't, you have to provide your dependencies explicitly on rule
invocation via the `deps` parameter.


As a convenience there is also a shortcut for the ["optimized" ANTLR4 fork](https://github.com/tunnelvisionlabs/antlr4) maintained by Sam Harwell:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_optimized_dependencies")

rules_antlr_optimized_dependencies(472)
```

It should support the same versions as the official ANTLR release.


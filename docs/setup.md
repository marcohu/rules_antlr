# Setup

To use the rules, add the following to your [`WORKSPACE`](https://docs.bazel.build/versions/master/build-ref.html#workspace) file to include
the external repository:

```python
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "rules_antlr",
    sha256 = "26e6a83c665cf6c1093b628b3a749071322f0f70305d12ede30909695ed85591",
    strip_prefix = "rules_antlr-0.5.0",
    urls = ["https://github.com/marcohu/rules_antlr/archive/0.5.0.tar.gz"],
)
```

Then you can load the necessary external dependencies in your [`WORKSPACE`](https://docs.bazel.build/versions/master/build-ref.html#workspace) file.

Either specify just the major version:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")

rules_antlr_dependencies(4)
```

Or better and recommended make the version explicit to avoid coupling:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")

rules_antlr_dependencies("4.7.2")
```


> **_NOTE:_**
If you don't use explicit versions, be careful when updating to a new rules_antlr
release as the bundled dependencies might change.


If you require several releases, you can specify several versions at once, but only from
different release streams:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")

rules_antlr_dependencies("2.7.7", "3.5.2", "4.7.2")
```

The examples above only load the default Java dependencies. If you require other runtimes,
you have to provide the target languages as well, in no particular order.

To load C++ and Go dependencies for ANTLR 3.5.2 and 4.7.2:

```python
load("@rules_antlr//antlr:repositories.bzl", "antlr_dependencies")
load("@rules_antlr//antlr:lang.bzl", "CPP", "GO")

rules_antlr_dependencies("3.5.2", CPP, "4.7.2", GO)
```

If you need different releases for different target languages, you can employ multiple
calls:

```python
load("@rules_antlr//antlr:repositories.bzl", "antlr_dependencies")
load("@rules_antlr//antlr:lang.bzl", "CPP", "GO", "PYTHON")

rules_antlr_dependencies(CPP, "4.7.2", GO)
rules_antlr_dependencies("3.5.2", PYTHON)
```

The currently supported releases are:

| Release  Stream | Supported Versions| Bundled Runtimes
|-----------------|-------------------|---
| 4               | 4.7.1, 4.7.2, 4.8 | C++, Go, Java, Python2, Python3
| 3               | 3.5.2             | C++, Java, Python2, Python3
| 2               | 2.7.7             | C++, Java, Python2

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
[`repositories.bzl`](../antlr/repositories.bzl) for the
default dependency names. You are not required to use these exact names. But if you don't, you have to provide your dependencies explicitly on rule
invocation via the [`deps`](antlr4.md#antlr-deps) parameter.


As a convenience there is also a shortcut for the ["optimized" ANTLR4 fork](https://github.com/tunnelvisionlabs/antlr4) maintained by Sam Harwell:

```python
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_optimized_dependencies")

rules_antlr_optimized_dependencies("4.7.2")
```

It should support the same versions as the official ANTLR release.


"""Loads ANTLR dependencies."""

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_jar")
load(":lang.bzl", "C", "CPP", "JAVA", "PYTHON", "PYTHON2", "PYTHON3")

v4 = [4, 471, 472]
v3 = [3, 352]
v2 = [2, 277]

def rules_antlr_dependencies(*versionsAndLanguages):
    """Loads the dependencies for the specified ANTLR releases.

    Args:
      *versionsAndLanguages: the ANTLR release versions to make available for the provided target languages.
    """
    if versionsAndLanguages:
        versions = []
        languages = []

        for versionOrLanguage in versionsAndLanguages:
            if type(versionOrLanguage) == "int":
                versions.append(versionOrLanguage)
            else:
                languages.append(versionOrLanguage)

        # only one version allowed per ANTLR release stream
        _validateVersions(versions)

        # if no language is specified, assume Java
        if not languages:
            languages = [JAVA]

        for version in sorted(versions):
            if (version == 4 or version == 472):
                _antlr472_dependencies(languages)
            elif (version == 471):
                _antlr471_dependencies(languages)
            elif (version == 3 or version == 352):
                _antlr352_dependencies(languages)
            elif (version == 2 or version == 272):
                _antlr277_dependencies(languages)
            else:
                fail("Invalid ANTLR version provided: {0}. Currently supported are: {1}".format(version, v4 + v3 + v2))
    else:
        _antlr472_dependencies([JAVA])

def rules_antlr_optimized_dependencies(*versions):
    """Loads the dependencies for the "optimized" fork of ANTLR 4 maintained by Sam Harwell.

    Args:
      *versions: the ANTLR releases versions to make available.
    """
    if versions:
        versions = sorted(versions)
        for version in versions:
            if (version == 4 or version == 472):
                _antlr472_optimized_dependencies()
            elif (version == 471):
                _antlr471_optimized_dependencies()
            else:
                fail("Invalid ANTLR version provided: {0}. Currently supported are: {1}".format(version, v4))
    else:
        _antlr472_optimized_dependencies()

def _antlr472_dependencies(languages):
    _antlr4_dependencies(
        languages,
        {
            "url": "https://github.com/antlr/antlr4/archive/4.7.2.tar.gz",
            "prefix": "antlr4-4.7.2",
            "sha256": "46f5e1af5f4bd28ade55cb632f9a069656b31fc8c2408f9aa045f9b5f5caad64",
        },
        _merge(
            {
                "antlr4_runtime": {
                    "name": "antlr4_runtime",
                    "path": "org/antlr/antlr4-runtime/4.7.2/antlr4-runtime-4.7.2.jar",
                    "sha256": "4c518b87d4bdff8b44cd8cbc1af816e944b62a3fe5b80b781501cf1f4759bbc4",
                },
                "antlr4_tool": {
                    "name": "antlr4_tool",
                    "path": "org/antlr/antlr4/4.7.2/antlr4-4.7.2.jar",
                    "sha256": "a3811fad1e4cb6dde62c189c204cf931c5fa40e06e43839ead4a9f2e188f2fe5",
                },
            },
            _antlr4_transitive_dependencies(),
        ),
    )

def _antlr471_dependencies(languages):
    _antlr4_dependencies(
        languages,
        {
            "url": "https://github.com/antlr/antlr4/archive/4.7.2.tar.gz",
            "prefix": "antlr4-4.7.2",
            "sha256": "46f5e1af5f4bd28ade55cb632f9a069656b31fc8c2408f9aa045f9b5f5caad64",
        },
        _merge(
            {
                "antlr4_runtime": {
                    "name": "antlr4_runtime",
                    "path": "org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar",
                    "sha256": "43516d19beae35909e04d06af6c0c58c17bc94e0070c85e8dc9929ca640dc91d",
                },
                "antlr4_tool": {
                    "name": "antlr4_tool",
                    "path": "org/antlr/antlr4/4.7.1/antlr4-4.7.1.jar",
                    "sha256": "a2cdc2f2f8eb893728832568dc54d080eb5a1495edb3b66e51b97122a60a0d87",
                },
            },
            _antlr4_transitive_dependencies(),
        ),
    )

def _antlr472_optimized_dependencies():
    _download(
        name = "antlr4_runtime",
        path = "com/tunnelvisionlabs/antlr4-runtime/4.7.2/antlr4-runtime-4.7.2.jar",
        sha256 = "fdec73953ba059034336a8e0b0ea5204f6897900bf0b0fa35347ce8a8bb88816",
    )
    _download(
        name = "antlr4_tool",
        path = "com/tunnelvisionlabs/antlr4/4.7.2/antlr4-4.7.2.jar",
        sha256 = "fcc2a0365de371d8676ab9b45c49aa2e784036a77b76383892887c89c5725ca3",
    )
    _antlr4_transitive_dependencies(False)

def _antlr471_optimized_dependencies():
    _download(
        name = "antlr4_runtime",
        path = "com/tunnelvisionlabs/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar",
        sha256 = "ce4f77ff9dc014feb9a8e700de5c77101d203acb6a1e8fa3446905c391ac72b9",
    )
    _download(
        name = "antlr4_tool",
        path = "com/tunnelvisionlabs/antlr4/4.7.1/antlr4-4.7.1.jar",
        sha256 = "de9a7b94b48ea7c8100663cbb1a54465c37671841c0aefdf4c53a72212555ae8",
    )
    _antlr4_transitive_dependencies()

def _antlr4_dependencies(languages, archive, dependencies):
    if JAVA in languages:
        for name in dependencies:
            _download(
                name = name,
                path = dependencies[name]["path"],
                sha256 = dependencies[name]["sha256"],
            )

    build_script = _antlr4_build_script(languages)

    if build_script:
        http_archive(
            name = "antlr4_runtimes",
            sha256 = archive["sha256"],
            strip_prefix = archive["prefix"],
            url = archive["url"],
            build_file_content = build_script,
        )

def _antlr4_build_script(languages):
    script = ""

    if CPP in languages:
        script += """
cc_library(
    name = "cpp",
    srcs = glob(["runtime/Cpp/runtime/src/**/*.cpp"]),
    hdrs = glob(["runtime/Cpp/runtime/src/**/*.h"]),
    includes = ["runtime/Cpp/runtime/src"],
    visibility = ["//visibility:public"],
)
"""

    if PYTHON2 in languages:
        script += """
py_library(
    name = "python2",
    srcs = glob(["runtime/Python3/src/*.py"]),
    imports = ["runtime/Python3/src"],
    visibility = ["//visibility:public"],
)
"""

    if PYTHON in languages or PYTHON3 in languages:
        script += """
py_library(
    name = "python",
    srcs = glob(["runtime/Python3/src/*.py"]),
    imports = ["runtime/Python3/src"],
    visibility = ["//visibility:public"],
)
"""

    return script

def _antlr4_transitive_dependencies():
    return {
        "antlr3_runtime": {
            "path": "org/antlr/antlr-runtime/3.5.2/antlr-runtime-3.5.2.jar",
            "sha256": "ce3fc8ecb10f39e9a3cddcbb2ce350d272d9cd3d0b1e18e6fe73c3b9389c8734",
        },
        "stringtemplate4": {
            "path": "org/antlr/ST4/4.0.8/ST4-4.0.8.jar",
            "sha256": "58caabc40c9f74b0b5993fd868e0f64a50c0759094e6a251aaafad98edfc7a3b",
        },
        "javax_json": {
            "path": "org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar",
            "sha256": "0e1dec40a1ede965941251eda968aeee052cc4f50378bc316cc48e8159bdbeb4",
        },
    }

def _antlr352_dependencies(languages):
    _antlr3_dependencies(
        languages,
        {
            "url": "https://github.com/marcohu/antlr3/archive/master.tar.gz",
            "prefix": "antlr3-master",
            "sha256": "53cd6c8e41995efa0b7d01c53047ad8a0e2c74e56fe03f6e938d2f0493ee7ace",
        },
        {
            "antlr3_runtime": {
                "path": "org/antlr/antlr-runtime/3.5.2/antlr-runtime-3.5.2.jar",
                "sha256": "ce3fc8ecb10f39e9a3cddcbb2ce350d272d9cd3d0b1e18e6fe73c3b9389c8734",
            },
            # the official release generates problematic C++ code, we therefore use a
            # custom build forked from https://github.com/ibre5041/antlr3.git
            "antlr3_tool": {
                "path": "https://github.com/marcohu/antlr3/raw/master/antlr-3.5.3.jar",
                "sha256": "897d0b914adf2e63899ada179c5f4aeb606d59fdfbb6ccaff5bc87aec300e2ce",
            },
            "stringtemplate4": {
                "path": "org/antlr/ST4/4.0.8/ST4-4.0.8.jar",
                "sha256": "58caabc40c9f74b0b5993fd868e0f64a50c0759094e6a251aaafad98edfc7a3b",
            },
        },
    )

def _antlr3_dependencies(languages, archive, dependencies):
    if JAVA in languages:
        for name in dependencies:
            _download(
                name = name,
                path = dependencies[name]["path"],
                sha256 = dependencies[name]["sha256"],
            )

    build_script = _antlr3_build_script(languages)

    if build_script:
        http_archive(
            name = "antlr3_runtimes",
            sha256 = archive["sha256"],
            strip_prefix = archive["prefix"],
            url = archive["url"],
            build_file_content = build_script,
        )

def _antlr3_build_script(languages):
    script = ""

    if CPP in languages:
        script += """
cc_library(
    name = "cpp",
    hdrs = glob(["runtime/Cpp/include/*.hpp", "runtime/Cpp/include/*.inl"]),
    includes = ["runtime/Cpp/include"],
    visibility = ["//visibility:public"],
)
"""

    if PYTHON2 in languages:
        script += """
py_library(
    name = "python2",
    srcs = glob(["runtime/Python/antlr3/*.py"]),
    imports = ["runtime/Python/antlr3"],
    visibility = ["//visibility:public"],
)
"""

    if PYTHON in languages or PYTHON3 in languages:
        script += """
py_library(
    name = "python",
    srcs = glob(["runtime/Python3/antlr3/*.py"]),
    imports = ["runtime/Python3/antlr3"],
    visibility = ["//visibility:public"],
)
"""
    return script

def _antlr277_dependencies(languages):
    _antlr2_dependencies(
        languages,
        {
            "url": "https://www.antlr2.org/download/antlr-2.7.7.tar.gz",
            "prefix": "antlr-2.7.7",
            "sha256": "853aeb021aef7586bda29e74a6b03006bcb565a755c86b66032d8ec31b67dbb9",
        },
        {
            "antlr2": {
                "path": "antlr/antlr/2.7.7/antlr-2.7.7.jar",
                "sha256": "88fbda4b912596b9f56e8e12e580cc954bacfb51776ecfddd3e18fc1cf56dc4c",
            },
        },
    )

def _antlr2_dependencies(languages, archive, dependencies):
    if JAVA in languages:
        for name in dependencies:
            _download(
                name = name,
                path = dependencies[name]["path"],
                sha256 = dependencies[name]["sha256"],
            )

    build_script = _antlr2_build_script(languages)

    if build_script:
        http_archive(
            name = "antlr2_runtimes",
            sha256 = "853aeb021aef7586bda29e74a6b03006bcb565a755c86b66032d8ec31b67dbb9",
            strip_prefix = "antlr-2.7.7",
            urls = ["https://www.antlr2.org/download/antlr-2.7.7.tar.gz"],
            patches = ["@rules_antlr//external:antlr2_strings.patch"],
            build_file_content = build_script,
        )

def _antlr2_build_script(languages):
    script = ""

    if CPP in languages:
        script += """
cc_library(
    name = "cpp",
    srcs = select({
        "@bazel_tools//src/conditions:windows": glob(["lib/cpp/src/*.cpp"]),
        "//conditions:default": glob(["lib/cpp/src/*.cpp"], exclude=["lib/cpp/src/dll.cpp"]),
    }),
    hdrs = glob(["lib/cpp/antlr/*.hpp"]),
    includes = ["lib/cpp"],
    visibility = ["//visibility:public"],
)
"""

    if PYTHON in languages or PYTHON2 in languages:
        script += """
py_library(
    name = "python",
    srcs = glob(["lib/python/antlr/*.py"]),
    imports = ["lib/python/antlr"],
    visibility = ["//visibility:public"],
)
"""

    return script

def _download(name, path, sha256):
    http_jar(
        name = name,
        urls = [
            path if path.startswith("https") else "https://jcenter.bintray.com/" + path,
            path if path.startswith("https") else "https://repo1.maven.org/maven2/" + path,
        ],
        sha256 = sha256,
    )

def _validateVersions(versions):
    bundled = v4 + v3 + v2
    store = {}
    for version in versions:
        if version not in bundled:
            fail(
                "Invalid ANTLR version provided: {0}. Currently supported are: {1}".format(version, bundled),
                attr = "versionsAndLanguages",
            )
        v = str(version)[0]
        p = store.get(v)
        if p:
            fail(
                "You can only load one version from ANTLR {0}. You specified both {1} and {2}.".format(v, p, version),
                attr = "versionsAndLanguages",
            )
        store[v] = version

def _merge(x, y):
    x.update(y)
    return x

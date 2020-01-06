"""Loads ANTLR dependencies."""

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_jar")
load(":lang.bzl", "C", "CPP", "GO", "JAVA", "PYTHON", "PYTHON2", "PYTHON3", supportedLanguages = "supported")

v4 = [4, "4.7.1", "4.7.2"]
v3 = [3, "3.5.2"]
v2 = [2, "2.7.7"]

def rules_antlr_dependencies(*versionsAndLanguages):
    """Loads the dependencies for the specified ANTLR releases.

    You have to provide at least the version number of the ANTLR release you want to use. To
    load the dependencies for languages besides Java, you have to indicate the languages as well.

    ```python
    load("@rules_antlr//antlr:lang.bzl", "CPP", "PYTHON")
    load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")

    rules_antlr_dependencies("4.7.2", CPP, PYTHON)
    ```

    Args:
      *versionsAndLanguages: the ANTLR release versions to make available for the provided target languages.
    """
    if versionsAndLanguages:
        versions = []
        languages = []
        supportedVersions = v4 + v3 + v2

        for versionOrLanguage in versionsAndLanguages:
            if not versionOrLanguage in supportedVersions:
                if type(versionOrLanguage) == "int" or str(versionOrLanguage).isdigit():
                    fail('Integer version \'{}\' no longer valid. Use semantic version "{}" instead.'.format(versionOrLanguage, ".".join(str(versionOrLanguage).elems())), attr = "versionsAndLanguages")
                elif str(versionOrLanguage).replace(".", "").isdigit():
                    fail('Unsupported ANTLR version provided: "{0}". Currently supported are: {1}'.format(versionOrLanguage, supportedVersions), attr = "versionsAndLanguages")
                elif not versionOrLanguage in supportedLanguages():
                    fail('Invalid language provided: "{0}". Currently supported are: {1}'.format(versionOrLanguage, supportedLanguages()), attr = "versionsAndLanguages")
                languages.append(versionOrLanguage)
            else:
                versions.append(versionOrLanguage)

        if not versions:
            fail("Missing ANTLR version", attr = "versionsAndLanguages")

        # only one version allowed per ANTLR release stream
        _validateVersions(versions)

        # if no language is specified, assume Java
        if not languages:
            languages = [JAVA]

        for version in sorted(versions, key = _toString):
            if version == 4 or version == "4.7.2":
                _antlr472_dependencies(languages)
            elif version == "4.7.1":
                _antlr471_dependencies(languages)
            elif version == 3 or version == "3.5.2":
                _antlr352_dependencies(languages)
            elif version == 2 or version == "2.7.7":
                _antlr277_dependencies(languages)
    else:
        fail("Missing ANTLR version", attr = "versionsAndLanguages")

def rules_antlr_optimized_dependencies(version):
    """Loads the dependencies for the "optimized" fork of ANTLR 4 maintained by Sam Harwell.

    ```python
    load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_optimized_dependencies")

    rules_antlr_optimized_dependencies("4.7.2")
    ```

    Args:
      version: the ANTLR release version to make available.
    """
    if version == 4 or version == "4.7.2":
        _antlr472_optimized_dependencies()
    elif version == "4.7.1":
        _antlr471_optimized_dependencies()
    elif type(version) == "int" or str(version).isdigit():
        fail('Integer version \'{}\' no longer valid. Use semantic version "{}" instead.'.format(version, ".".join(str(version).elems())), attr = "version")
    else:
        fail('Unsupported ANTLR version provided: "{0}". Currently supported are: {1}'.format(version, v4), attr = "version")

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
    for name in dependencies:
        _download(
            name = name,
            path = dependencies[name]["path"],
            sha256 = dependencies[name]["sha256"],
        )

    build_script, workspace = _antlr4_build_script(languages)

    if build_script:
        http_archive(
            name = "antlr4_runtimes",
            sha256 = archive["sha256"],
            strip_prefix = archive["prefix"],
            url = archive["url"],
            build_file_content = build_script,
            workspace_file_content = workspace,
        )

def _antlr4_build_script(languages):
    script = ""
    workspace = ""

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

    if GO in languages:
        workspace += _load_http(workspace) + """
http_archive(
    name = "io_bazel_rules_go",
    urls = [
        "https://storage.googleapis.com/bazel-mirror/github.com/bazelbuild/rules_go/releases/download/v0.20.3/rules_go-v0.20.3.tar.gz",
        "https://github.com/bazelbuild/rules_go/releases/download/v0.20.3/rules_go-v0.20.3.tar.gz",
    ],
    sha256 = "e88471aea3a3a4f19ec1310a55ba94772d087e9ce46e41ae38ecebe17935de7b",
)
load("@io_bazel_rules_go//go:deps.bzl", "go_rules_dependencies", "go_register_toolchains")
go_rules_dependencies()
go_register_toolchains()
"""
        script += """
load("@io_bazel_rules_go//go:def.bzl", "go_library")
go_library(
    name = "go",
    srcs = glob(["runtime/Go/antlr/*.go"]),
    importpath = "github.com/antlr/antlr4/runtime/Go/antlr",
    visibility = ["//visibility:public"],
)
"""

    if PYTHON2 in languages:
        workspace += _load_http(workspace) + _load_rules_python_repositories(workspace)
        script += _load_rules_python_defs(script) + """
py_library(
    name = "python2",
    srcs = glob(["runtime/Python3/src/*.py"]),
    imports = ["runtime/Python3/src"],
    visibility = ["//visibility:public"],
)
"""

    if PYTHON in languages or PYTHON3 in languages:
        workspace += _load_http(workspace) + _load_rules_python_repositories(workspace)
        script += _load_rules_python_defs(script) + """
py_library(
    name = "python",
    srcs = glob(["runtime/Python3/src/*.py"]),
    imports = ["runtime/Python3/src"],
    visibility = ["//visibility:public"],
)
alias(
    name = "python3",
    actual = ":python",
)
"""

    return (script, workspace)

def _load_http(workspace):
    return "" if workspace.find("@bazel_tools//tools/build_defs/repo:http.bzl") > -1 else 'load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")'

def _load_rules_python_repositories(workspace):
    return "" if workspace.find('load("@rules_python//python:repositories.bzl", "py_repositories")') > -1 else """
http_archive(
    name = "rules_python",
    sha256 = "aa96a691d3a8177f3215b14b0edc9641787abaaa30363a080165d06ab65e1161",
    url = "https://github.com/bazelbuild/rules_python/releases/download/0.0.1/rules_python-0.0.1.tar.gz",
)
load("@rules_python//python:repositories.bzl", "py_repositories")
py_repositories()
"""

def _load_rules_python_defs(script):
    return "" if script.find('load("@rules_python//python:defs.bzl"') > -1 else 'load("@rules_python//python:defs.bzl", "py_library")'

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
            patches = ["@rules_antlr//third_party:antlr2_strings.patch"],
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
        v = str(version)[0]
        p = store.get(v)
        if p:
            fail(
                'You can only load one version from ANTLR {0}. You specified both "{1}" and "{2}".'.format(v, p, version),
                attr = "versionsAndLanguages",
            )
        store[v] = version

def _toString(x):
    return str(x)

def _merge(x, y):
    x.update(y)
    return x

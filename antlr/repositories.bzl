"""Loads ANTLR dependencies."""

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_jar")
load(":lang.bzl", "C", "CPP", "GO", "JAVA", "OBJC", "PYTHON", "PYTHON2", "PYTHON3", supportedLanguages = "supported")

v4 = [4, "4.7.1", "4.7.2", "4.8", "4.9.3"]
v4_opt = [4, "4.7.1", "4.7.2", "4.7.3", "4.7.4"]
v3 = [3, "3.5.2"]
v2 = [2, "2.7.7"]

PACKAGES = {
    "antlr": {
        "4.9.3": {
            "url": "https://github.com/antlr/antlr4/archive/4.9.3.tar.gz",
            "prefix": "antlr4-4.9.3",
            "sha256": "efe4057d75ab48145d4683100fec7f77d7f87fa258707330cadd1f8e6f7eecae",
        },
        "4.8": {
            "url": "https://github.com/antlr/antlr4/archive/4.8.tar.gz",
            "prefix": "antlr4-4.8",
            "sha256": "992d52444b81ed75e52ea62f9f38ecb7652d5ce2a2130af143912b3042a6d77e",
        },
        "4.7.2": {
            "url": "https://github.com/antlr/antlr4/archive/4.7.2.tar.gz",
            "prefix": "antlr4-4.7.2",
            "sha256": "46f5e1af5f4bd28ade55cb632f9a069656b31fc8c2408f9aa045f9b5f5caad64",
        },
        "4.7.1": {
            "url": "https://github.com/antlr/antlr4/archive/4.7.1.tar.gz",
            "prefix": "antlr4-4.7.1",
            "sha256": "4d0714f441333a63e50031c9e8e4890c78f3d21e053d46416949803e122a6574",
        },
        "3.5.2": {
            "url": "https://github.com/marcohu/antlr3/archive/master.tar.gz",
            "prefix": "antlr3-master",
            "sha256": "53cd6c8e41995efa0b7d01c53047ad8a0e2c74e56fe03f6e938d2f0493ee7ace",
        },
        "2.7.7": {
            "url": "https://www.antlr2.org/download/antlr-2.7.7.tar.gz",
            "prefix": "antlr-2.7.7",
            "sha256": "853aeb021aef7586bda29e74a6b03006bcb565a755c86b66032d8ec31b67dbb9",
            "patches": ["@rules_antlr//third_party:antlr2_strings.patch"],
        },
    },
    "antlr4_runtime": {
        "4.9.3": {
            "path": "org/antlr/antlr4-runtime/4.9.3/antlr4-runtime-4.9.3.jar",
            "sha256": "131a6594969bc4f321d652ea2a33bc0e378ca312685ef87791b2c60b29d01ea5",
        },
        "4.8": {
            "path": "org/antlr/antlr4-runtime/4.8/antlr4-runtime-4.8.jar",
            "sha256": "2337df5d81e715b39aeea07aac46ad47e4f1f9e9cd7c899f124f425913efdcf8",
        },
        "4.7.2": {
            "path": "org/antlr/antlr4-runtime/4.7.2/antlr4-runtime-4.7.2.jar",
            "sha256": "4c518b87d4bdff8b44cd8cbc1af816e944b62a3fe5b80b781501cf1f4759bbc4",
        },
        "4.7.1": {
            "path": "org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar",
            "sha256": "43516d19beae35909e04d06af6c0c58c17bc94e0070c85e8dc9929ca640dc91d",
        },
        "4.7.4-opt": {
            "path": "com/tunnelvisionlabs/antlr4-runtime/4.7.4/antlr4-runtime-4.7.4.jar",
            "sha256": "c0616e1eb3b7aa6b4de9a304ea458d50cac279f78b0b65bf7a8176701f8402ee",
        },
        "4.7.3-opt": {
            "path": "com/tunnelvisionlabs/antlr4-runtime/4.7.3/antlr4-runtime-4.7.3.jar",
            "sha256": "5f4f0c4031e4b83cb369ef00f4909cdb6f62b11e3d253f83a6184d80c5eb3157",
        },
        "4.7.2-opt": {
            "path": "com/tunnelvisionlabs/antlr4-runtime/4.7.2/antlr4-runtime-4.7.2.jar",
            "sha256": "fdec73953ba059034336a8e0b0ea5204f6897900bf0b0fa35347ce8a8bb88816",
        },
        "4.7.1-opt": {
            "path": "com/tunnelvisionlabs/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar",
            "sha256": "ce4f77ff9dc014feb9a8e700de5c77101d203acb6a1e8fa3446905c391ac72b9",
        },
    },
    "antlr4_tool": {
        "4.9.3": {
            "path": "org/antlr/antlr4/4.9.3/antlr4-4.9.3.jar",
            "sha256": "386fec520b8962fe37f448af383920ea33d7a532314b36d7ba9ccec1ba95eb37",
        },
        "4.8": {
            "path": "org/antlr/antlr4/4.8/antlr4-4.8.jar",
            "sha256": "6e4477689371f237d4d8aa40642badbb209d4628ccdd81234d90f829a743bac8",
        },
        "4.7.2": {
            "path": "org/antlr/antlr4/4.7.2/antlr4-4.7.2.jar",
            "sha256": "a3811fad1e4cb6dde62c189c204cf931c5fa40e06e43839ead4a9f2e188f2fe5",
        },
        "4.7.1": {
            "path": "org/antlr/antlr4/4.7.1/antlr4-4.7.1.jar",
            "sha256": "a2cdc2f2f8eb893728832568dc54d080eb5a1495edb3b66e51b97122a60a0d87",
        },
        "4.7.4-opt": {
            "path": "com/tunnelvisionlabs/antlr4/4.7.4/antlr4-4.7.4.jar",
            "sha256": "f84d71d130f17b13f0934af7575626890a4dab0c588a95b80572a66f7deacca4",
        },
        "4.7.3-opt": {
            "path": "com/tunnelvisionlabs/antlr4/4.7.3/antlr4-4.7.3.jar",
            "sha256": "06cd5f3a9488b32cb1022360df054bbe7aebe8e817c0aa58c8feec05879e0c63",
        },
        "4.7.2-opt": {
            "path": "com/tunnelvisionlabs/antlr4/4.7.2/antlr4-4.7.2.jar",
            "sha256": "fcc2a0365de371d8676ab9b45c49aa2e784036a77b76383892887c89c5725ca3",
        },
        "4.7.1-opt": {
            "path": "com/tunnelvisionlabs/antlr4/4.7.1/antlr4-4.7.1.jar",
            "sha256": "de9a7b94b48ea7c8100663cbb1a54465c37671841c0aefdf4c53a72212555ae8",
        },
    },
    "antlr3_runtime": {
        "3.5.2": {
            "path": "org/antlr/antlr-runtime/3.5.2/antlr-runtime-3.5.2.jar",
            "sha256": "ce3fc8ecb10f39e9a3cddcbb2ce350d272d9cd3d0b1e18e6fe73c3b9389c8734",
        },
    },
    "antlr3_tool": {
        "3.5.2": {
            # the official release generates problematic C++ code, we therefore use a
            # custom build forked from https://github.com/ibre5041/antlr3.git
            "path": "https://github.com/marcohu/antlr3/raw/master/antlr-3.5.3.jar",
            "sha256": "897d0b914adf2e63899ada179c5f4aeb606d59fdfbb6ccaff5bc87aec300e2ce",
        },
    },
    "antlr2": {
        "2.7.7": {
            "path": "antlr/antlr/2.7.7/antlr-2.7.7.jar",
            "sha256": "88fbda4b912596b9f56e8e12e580cc954bacfb51776ecfddd3e18fc1cf56dc4c",
        },
    },
    "stringtemplate4": {
        "4.3": {
            "path": "org/antlr/ST4/4.3/ST4-4.3.jar",
            "sha256": "28547dba48cfceb77b6efbfe069aebe9ed3324ae60dbd52093d13a1d636ed069",
        },
        "4.0.8": {
            "path": "org/antlr/ST4/4.0.8/ST4-4.0.8.jar",
            "sha256": "58caabc40c9f74b0b5993fd868e0f64a50c0759094e6a251aaafad98edfc7a3b",
        },
    },
    "javax_json": {
        "1.0.4": {
            "path": "org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar",
            "sha256": "0e1dec40a1ede965941251eda968aeee052cc4f50378bc316cc48e8159bdbeb4",
        },
    },
}

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
            if version == 4 or version == "4.9.3":
                _antlr493_dependencies(languages)
            elif version == "4.8":
                _antlr48_dependencies(languages)
            elif version == "4.7.2":
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
    if version == 4 or version == "4.7.4":
        _antlr474_optimized_dependencies()
    elif version == "4.7.3":
        _antlr473_optimized_dependencies()
    elif version == "4.7.2":
        _antlr472_optimized_dependencies()
    elif version == "4.7.1":
        _antlr471_optimized_dependencies()
    elif type(version) == "int" or str(version).isdigit():
        fail('Integer version \'{}\' no longer valid. Use semantic version "{}" instead.'.format(version, ".".join(str(version).elems())), attr = "version")
    else:
        fail('Unsupported ANTLR version provided: "{0}". Currently supported are: {1}'.format(version, v4_opt), attr = "version")

def _antlr493_dependencies(languages):
    _antlr4_dependencies(
        "4.9.3",
        languages,
        {
            "antlr4_runtime": "4.9.3",
            "antlr4_tool": "4.9.3",
            "antlr3_runtime": "3.5.2",
            "stringtemplate4": "4.3",
            "javax_json": "1.0.4",
        },
    )

def _antlr48_dependencies(languages):
    _antlr4_dependencies(
        "4.8",
        languages,
        {
            "antlr4_runtime": "4.8",
            "antlr4_tool": "4.8",
            "antlr3_runtime": "3.5.2",
            "stringtemplate4": "4.3",
            "javax_json": "1.0.4",
        },
    )

def _antlr472_dependencies(languages):
    _antlr4_dependencies(
        "4.7.2",
        languages,
        {
            "antlr4_runtime": "4.7.2",
            "antlr4_tool": "4.7.2",
            "antlr3_runtime": "3.5.2",
            "stringtemplate4": "4.0.8",
            "javax_json": "1.0.4",
        },
    )

def _antlr471_dependencies(languages):
    _antlr4_dependencies(
        "4.7.1",
        languages,
        {
            "antlr4_runtime": "4.7.1",
            "antlr4_tool": "4.7.1",
            "antlr3_runtime": "3.5.2",
            "stringtemplate4": "4.0.8",
            "javax_json": "1.0.4",
        },
    )

def _antlr474_optimized_dependencies():
    _dependencies({
        "antlr4_runtime": "4.7.4-opt",
        "antlr4_tool": "4.7.4-opt",
        "antlr3_runtime": "3.5.2",
        "stringtemplate4": "4.0.8",
        "javax_json": "1.0.4",
    })

def _antlr473_optimized_dependencies():
    _dependencies({
        "antlr4_runtime": "4.7.3-opt",
        "antlr4_tool": "4.7.3-opt",
        "antlr3_runtime": "3.5.2",
        "stringtemplate4": "4.0.8",
        "javax_json": "1.0.4",
    })

def _antlr472_optimized_dependencies():
    _dependencies({
        "antlr4_runtime": "4.7.2-opt",
        "antlr4_tool": "4.7.2-opt",
        "antlr3_runtime": "3.5.2",
        "stringtemplate4": "4.0.8",
        "javax_json": "1.0.4",
    })

def _antlr471_optimized_dependencies():
    _dependencies({
        "antlr4_runtime": "4.7.1-opt",
        "antlr4_tool": "4.7.1-opt",
        "antlr3_runtime": "3.5.2",
        "stringtemplate4": "4.0.8",
        "javax_json": "1.0.4",
    })

def _antlr4_dependencies(version, languages, dependencies):
    _dependencies(dependencies)
    archive = PACKAGES["antlr"][version]
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

def _antlr352_dependencies(languages):
    _antlr3_dependencies(
        "3.5.2",
        languages,
        {
            "antlr3_runtime": "3.5.2",
            "antlr3_tool": "3.5.2",
            "stringtemplate4": "4.0.8",
        },
    )

def _antlr3_dependencies(version, languages, dependencies):
    _dependencies(dependencies)
    archive = PACKAGES["antlr"][version]
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
        "2.7.7",
        languages,
        {
            "antlr2": "2.7.7",
        },
    )

def _antlr2_dependencies(version, languages, dependencies):
    _dependencies(dependencies)
    archive = PACKAGES["antlr"][version]
    build_script = _antlr2_build_script(languages)

    if build_script:
        http_archive(
            name = "antlr2_runtimes",
            sha256 = archive["sha256"],
            strip_prefix = "antlr-2.7.7",
            url = archive["url"],
            patches = archive["patches"] if "patches" in archive else [],
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

def _dependencies(dependencies):
    for key in dependencies:
        version = dependencies[key]
        _download(
            name = key,
            path = PACKAGES[key][version]["path"],
            sha256 = PACKAGES[key][version]["sha256"],
        )

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

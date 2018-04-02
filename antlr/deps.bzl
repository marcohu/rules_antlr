"""Loads ANTLR dependencies."""

def antlr_dependencies(*versions):
    skipAntlr3 = False
    versions = sorted(versions)
    for version in versions:
        if (version == 4):
            antlr4_dependencies(skipAntlr3)
        elif (version == 3):
            antlr3_dependencies()
            skipAntlr3 = True
        elif (version == 2):
            antlr2_dependencies()
        else:
            fail("Invalid ANTLR version provided: {0}. Currently supported are 2, 3, 4".format(version))

def antlr4_dependencies(skipAntlr3):
    """ Loads the dependencies for the official ANTLR 4 release. """
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
    _antlr4_dependencies(skipAntlr3)

def antlr4_optimized_dependencies():
    """ Loads the dependencies for the "optimized" fork of ANTLR 4 maintained by Sam Harwell. """
    native.http_jar(
        name = "antlr4_runtime",
        url = "http://central.maven.org/maven2/com/tunnelvisionlabs/antlr4-runtime/4.7/antlr4-runtime-4.7.jar",
        sha256 = "",
    )
    native.http_jar(
        name = "antlr4_tool",
        url = "http://central.maven.org/maven2/com/tunnelvisionlabs/antlr4/4.7/antlr4-4.7.jar",
        sha256 = "",
    )
    _antlr4_dependencies()

def antlr3_dependencies():
    """ Loads the dependencies for the official ANTLR 3 release. """
    native.http_jar(
        name = "antlr3_runtime",
        url = "http://central.maven.org/maven2/org/antlr/antlr-runtime/3.5.2/antlr-runtime-3.5.2.jar",
        sha256 = "ce3fc8ecb10f39e9a3cddcbb2ce350d272d9cd3d0b1e18e6fe73c3b9389c8734",
    )
    native.http_jar(
        name = "antlr3_tool",
        url = "http://central.maven.org/maven2/org/antlr/antlr/3.5.2/antlr-3.5.2.jar",
        sha256 = "",
    )

def antlr2_dependencies():
    """ Loads the dependencies for the official ANTLR 2 release. """
    native.http_jar(
        name = "antlr2",
        url = "http://central.maven.org/maven2/antlr/antlr/2.7.7/antlr-2.7.7.jar",
        sha256 = "88fbda4b912596b9f56e8e12e580cc954bacfb51776ecfddd3e18fc1cf56dc4c",
    )

def _antlr4_dependencies(skipAntlr3):
    if not skipAntlr3:
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

"""Loads ANTLR dependencies."""

def antlr_dependencies(*versions):
    """ Loads the dependencies for the specified ANTLR releases. """
    if versions:
        skipAntlr3 = False
        versions = sorted(versions)
        for version in versions:
            if (version == 4):
                _antlr4_dependencies(skipAntlr3)
            elif (version == 3):
                _antlr3_dependencies()
                skipAntlr3 = True
            elif (version == 2):
                _antlr2_dependencies()
            else:
                fail("Invalid ANTLR version provided: {0}. Currently supported are 2, 3, 4".format(version))
    else:
        _antlr4_dependencies(False)

def antlr_optimized_dependencies(*versions):
    """ Loads the dependencies for the "optimized" fork of ANTLR 4 maintained by Sam Harwell. """
    if versions:
        versions = sorted(versions)
        for version in versions:
            if (version == 4):
                _antlr4_optimized_dependencies()
            else:
                fail("Invalid ANTLR version provided: {0}. Currently supported is 4".format(version))
    else:
        _antlr4_optimized_dependencies()

def _antlr4_dependencies(skipAntlr3=False):
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
    _antlr4_transitive_dependencies(skipAntlr3)

def _antlr4_optimized_dependencies():
    native.http_jar(
        name = "antlr4_runtime",
        url = "http://central.maven.org/maven2/com/tunnelvisionlabs/antlr4-runtime/4.7/antlr4-runtime-4.7.jar",
        sha256 = "729e327795535e62633d15b364a168f07f3239f9a692a98b154623cdec1807c8",
    )
    native.http_jar(
        name = "antlr4_tool",
        url = "http://central.maven.org/maven2/com/tunnelvisionlabs/antlr4/4.7/antlr4-4.7.jar",
        sha256 = "e0133570df02e063b29733ccc1587965c89b0298b58909f6696de9d271671f2f",
    )
    _antlr4_transitive_dependencies(False)

def _antlr4_transitive_dependencies(skipAntlr3):
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

def _antlr3_dependencies():
    native.http_jar(
        name = "antlr3_runtime",
        url = "http://central.maven.org/maven2/org/antlr/antlr-runtime/3.5.2/antlr-runtime-3.5.2.jar",
        sha256 = "ce3fc8ecb10f39e9a3cddcbb2ce350d272d9cd3d0b1e18e6fe73c3b9389c8734",
    )
    native.http_jar(
        name = "antlr3_tool",
        url = "http://central.maven.org/maven2/org/antlr/antlr/3.5.2/antlr-3.5.2.jar",
        sha256 = "5ac36c2acfb0a0f3d37dafe20b5b570f2643e2d000c648d44503c2738be643df",
    )
    native.http_jar(
        name = "stringtemplate4",
        url = "http://central.maven.org/maven2/org/antlr/ST4/4.0.8/ST4-4.0.8.jar",
        sha256 = "58caabc40c9f74b0b5993fd868e0f64a50c0759094e6a251aaafad98edfc7a3b",
    )

def _antlr2_dependencies():
    native.http_jar(
        name = "antlr2",
        url = "http://central.maven.org/maven2/antlr/antlr/2.7.7/antlr-2.7.7.jar",
        sha256 = "88fbda4b912596b9f56e8e12e580cc954bacfb51776ecfddd3e18fc1cf56dc4c",
    )

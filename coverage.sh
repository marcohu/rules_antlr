#!/usr/bin/env bash

set -eu

genhtml=$(which genhtml)
if [[ -z "${genhtml}" ]]; then
    echo "Install 'genhtml' (contained in the 'lcov' package)"
    exit 1
fi

# if no destination directory is provided, use bazel-bin/coverage
if [[ $# -eq 0 ]] ; then
    destdir="$(bazel info bazel-bin)/coverage"
else
    destdir="$1"
fi

# coverage is expensive to run; use --jobs=1 to avoid overloading the machine.
bazel coverage --jobs=${COVERAGE_CPUS:-1} --instrumentation_filter //src/main/... --combined_report=lcov --coverage_report_generator=@bazel_tools//tools/test/CoverageOutputGenerator/java/com/google/devtools/coverageoutputgenerator:Main -- ...
genhtml -p $(pwd) -title rules_antlr -o $destdir --branch-coverage --legend bazel-out/_coverage/_coverage_report.dat

echo "Coverage report at file://$(pwd)/bazel-bin/coverage/index.html"


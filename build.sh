#!/usr/bin/env bash

set -eu

bazel build --jobs 1 //...
cp bazel-bin/antlr/*.md docs/
find docs/*.md -exec chmod u+rw {} +

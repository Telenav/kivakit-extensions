#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

ROOT="$(pwd)"
BRANCH="${GITHUB_REF//refs\/heads\//}"

echo "Building feature branch $BRANCH"

echo "Cloning kivakit-extensions in $ROOT"
cd "$ROOT"
git clone --branch "$BRANCH" --quiet https://github.com/Telenav/kivakit.git

echo "Installing kivakit super POM"
cd "$ROOT"/kivakit/superpom
mvn clean install

echo "Cloning kivakit-extensions in $ROOT"
cd "$ROOT"
git clone --branch "$BRANCH"--quiet https://github.com/Telenav/kivakit-extensions.git

echo "Building kivakit-extensions"
cd "$ROOT"/kivakit-extensions
mvn -Dmaven.javadoc.skip=true -DKIVAKIT_DEBUG="!Debug" -P shade -P tools --no-transfer-progress --batch-mode clean install

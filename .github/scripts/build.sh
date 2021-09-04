#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

ROOT="$(pwd)"
BRANCH="${GITHUB_REF//refs\/heads\//}:"

echo "Building feature branch $BRANCH"

echo "Cloning kivakit-extensions in $ROOT"

git clone --quiet https://github.com/Telenav/kivakit.git
cd "$ROOT"/kivakit
git checkout "$BRANCH"
cd superpom
mvn clean install

echo "Cloning kivakit-extensions in $ROOT"

git clone --quiet https://github.com/Telenav/kivakit-extensions.git
cd "$ROOT"/kivakit-extensions
git checkout "$BRANCH"

cd "$ROOT"/kivakit-extensions
mvn -Dmaven.javadoc.skip=true -DKIVAKIT_DEBUG="!Debug" -P shade -P tools --no-transfer-progress --batch-mode clean install

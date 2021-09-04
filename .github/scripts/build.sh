#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

ROOT="$(pwd)"

echo "Building feature branch $GITHUB_REF"

echo "Cloning KivaKit in $ROOT"

git clone https://github.com/Telenav/kivakit.git
cd "$ROOT"/kivakit
git checkout "$GITHUB_REF"

git clone https://github.com/Telenav/kivakit-extensions.git
cd "$ROOT"/kivakit-extensions
git checkout "$GITHUB_REF"

echo "Installing KivaKit super POM"
cd "$ROOT"/kivakit/superpom
mvn clean install

cd "$ROOT"/kivakit-extensions
mvn -Dmaven.javadoc.skip=true -DKIVAKIT_DEBUG="!Debug" -P shade -P tools --no-transfer-progress --batch-mode clean install

#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

echo "Building in $(pwd)"
pushd ..
echo "Cloning KivaKit into $(pwd)}"
git clone https://github.com/Telenav/kivakit.git
echo "Installing KivaKit super POM"
cd kivakit/superpom
mvn clean install
popd
mvn -Dmaven.javadoc.skip=true -DKIVAKIT_DEBUG="!Debug" -P shade -P tools --no-transfer-progress --batch-mode clean install

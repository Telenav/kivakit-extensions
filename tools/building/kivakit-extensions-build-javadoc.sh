#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

KIVAKIT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

cd $KIVAKIT_WORKSPACE/kivakit-extensions

mvn clean package javadoc:aggregate

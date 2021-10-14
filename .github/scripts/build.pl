#!/usr/bin/perl

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

use strict;
use warnings FATAL => 'all';

#
# Include build script from cactus-build
#

if (!-d "cactus-build")
{
    system("git clone --branch develop --quiet https://github.com/Telenav/cactus-build.git");
}

require "./cactus-build/.github/scripts/build-include.pl";
#require "$ENV{'KIVAKIT_WORKSPACE'}/cactus-build/.github/scripts/build-include.pl";

#
# Clone repositories and build
#

my ($build_type) = @ARGV;
my $github = "https://github.com/Telenav";

clone("$github/kivakit", "dependency");
clone("$github/kivakit-extensions", "build");

bash "$ENV{'KIVAKIT_HOME'}/tools/library/install-merged-jars.sh"

build_kivakit($build_type);
build_kivakit_extensions($build_type);
